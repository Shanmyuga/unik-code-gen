/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.ChildComponent;
import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.ComponentOptions;
import com.cognizant.fecodegen.bo.DataTable;
import com.cognizant.fecodegen.bo.FormDetail;
import com.cognizant.fecodegen.bo.FormElement;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.TypeScript;
import com.cognizant.fecodegen.bo.TypeScriptDeclarations;
import com.cognizant.fecodegen.bo.TypeScriptMethod;
import com.cognizant.fecodegen.bo.TypeScriptModel;
import com.cognizant.fecodegen.dao.LayoutRepository;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.model.LayoutEntity;
import com.cognizant.fecodegen.model.Params;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.cognizant.fecodegen.utils.TypeScriptParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class AngularComponentRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(AngularComponentRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	@Autowired
	private LayoutRepository layoutRepository;
	
	public AngularComponentRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	String sectionHtmlId = StringUtils.EMPTY;

	@SuppressWarnings("unchecked")
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {

		LOGGER.info("Creating UI Component... ");
		LOGGER.debug (jsonDoc);

		String sectionName = (String) properties.get(Constants.UI_NAME);
		String moduleName=(String) properties.get(Constants.MODULE_NAME);
		String componentName=(String) properties.get(Constants.COMPONENT_NAME);
		String userDefinedPath=(String)properties.get(Constants.USER_DEFINED_PATH);
		CodeGenProperties.putCacheValue(Constants.CURRENT_COMPONENT_NAME, componentName);

		List<Component> componentsList = 
				(List<Component>) CodeGenProperties.getCacheValue(Constants.COMPONENTS_LIST);

		for (Component component : componentsList) {
			componentName = component.getComponentName();
			JsonArray uiLayout = component.getUiLayout();			
			
			generateCodeForComponent(sectionName, moduleName, component, userDefinedPath, uiLayout);
		}
		
		return false;
	}

	/**
	 * @param sectionName
	 * @param moduleName
	 * @param componentName
	 * @param userDefinedPath
	 * @param uiLayout
	 * @throws CodeGenException 
	 */
	protected void generateCodeForComponent(String sectionName, String moduleName, Component component,
			String userDefinedPath, JsonArray uiLayout) throws CodeGenException {
		StringBuilder compContent = null;
		StringBuilder content = new StringBuilder();
		List<ChildComponent> childContentList = new ArrayList<>();
		FormDetail formDetail = new FormDetail();
		formDetail.setComponent(component);
		
		for (JsonElement element : uiLayout) {
			compContent = new StringBuilder();

			generateCode(null, element, compContent, childContentList, uiLayout.size(), formDetail);
			content.append(compContent.toString());
		}
		sectionHtmlId = component.getSectionHtmlId();

		CodeGenProperties.putCacheValue(component.getPanelType()+Constants.SECTION_HTML_ID, sectionHtmlId);
		
		generateTypeScript(moduleName, component, userDefinedPath, formDetail);
		
		generateCss(moduleName, component, userDefinedPath, formDetail);
		
		generateTestCases(component, formDetail);

		try {
			String finalContent = getContentToWrite(sectionName, component, content, formDetail);
			
			writeAngular(finalContent, moduleName+"/"+component.getComponentName(),userDefinedPath);

			CodeGenProperties.putCacheCompValue(Constants.COMPONENT_FILENAME, getSavedFileName());
		} catch (CodeGenException e) {
			LOGGER.error("Error while rendering Component: ", e);
		}
	}

	private void generateTestCases(Component component, FormDetail formDetail) {

		Map<String, Object> contextVariables = new HashMap<>();
		contextVariables.put("ts", component.getCompTypeScript());
		contextVariables.put("appModule", component.getAppModule());
		contextVariables.put("compModule", component.getCompModule());
		contextVariables.put("component", component);
		contextVariables.put("formDetail", formDetail);
		contextVariables.put("isApp", false);
		contextVariables.put("htmlID", sectionHtmlId);
		contextVariables.put("htmlIDCamCase", getCamelCase(sectionHtmlId));
		
		component.getCompModule().addTypeScriptImport("@angular/core", "NO_ERRORS_SCHEMA");
		component.getCompModule().addTypeScriptImport("@angular/core", "DebugElement");
		component.getCompModule().addTypeScriptImport("@angular/platform-browser", "By");
		component.getCompModule().addNgModuleSchema("NO_ERRORS_SCHEMA");
		
		writeSpec(component, contextVariables, component.getCompTypeScriptPath());
		
		contextVariables.put("isApp", true);
		contextVariables.put("ts", component.getAppTypeScript());
		writeSpec(component, contextVariables, component.getAppModulePath());
	}

	/**
	 * @param component
	 * @param specTemplate
	 * @param contextVariables
	 */
	protected void writeSpec(Component component, Map<String, Object> contextVariables, String path) {
		String specTemplate = config.getJson().get("params").getAsJsonObject()
				.get("specTemplate").getAsString();

		try {
			String text = parser.parse(specTemplate, contextVariables);
			
			if (path.contains("module.ts")) {
				path = path.replace("module.ts", "component.spec.ts");
			} else {
				path = path.replace(".ts", ".spec.ts");
			}
			
			FileWriter writer = new FileWriter(path);
			writer.write(text + "\n");
			writer.close();
		
		} catch (CodeGenException e) {
			LOGGER.info("Error while parsing spec template: error=" + e.getMessage());
		} catch (IOException e) {
			LOGGER.info("IO Error while parsing spec template: error=" + e.getMessage());
		}
	}

	/**
	 * @param sectionName
	 * @param component
	 * @param content
	 * @param formDetail
	 * @return
	 * @throws CodeGenException
	 */
	protected String getContentToWrite(String sectionName, Component component, StringBuilder content,
			FormDetail formDetail) throws CodeGenException {
		String finalContent = content.toString();
		if (formDetail.getFormElementList().isEmpty() == false) {
			Map<String, Object> contextVariables = 
					getComponentParametersMap(content, sectionName, component.getComponentName());

			Component currentComponent = CodeGenProperties.getCurrentReactComponent();
			contextVariables.put(Constants.STATE_VARIABLES, currentComponent.getVariables());

			contextVariables.put("formContent", content.toString());
			contextVariables.put("htmlID", sectionHtmlId);
			contextVariables.put("component", component);
			contextVariables.put("formDetail", formDetail);
			contextVariables.put("htmlIDCamCase", getCamelCase(sectionHtmlId));
			
			finalContent = parser.parse(Constants.FORM_TYPE, contextVariables);
		}
		return finalContent;
	}

	private TypeScriptModel generateComponentModel(TypeScript typeScript, Component component) throws CodeGenException, IOException {
		String modelPath = properties.get(Constants.MODULE_PATH) + Constants.FORWARD_SLASH + component.getModelFolderName();
		
		Path path1 = Paths.get(modelPath);
		if(!Files.isDirectory(path1)) {
			Files.createDirectories(path1);
		}
		
		String moduleName = (String) properties.get(Constants.MODULE_NAME);
		String fileName = modelPath + Constants.FORWARD_SLASH + moduleName + ".types.ts";
		File file = new File(fileName);
		
		TypeScript modelTs = parseExistingModelObject(fileName, file);

		String htmlId = (String) CodeGenProperties.getCacheValue(component.getPanelType()+Constants.SECTION_HTML_ID);
		if (null == htmlId) {
			htmlId = (String) CodeGenProperties.getCacheValue("leftPanel" + Constants.SECTION_HTML_ID);
		}
		if (null == htmlId) {
			htmlId = (String) CodeGenProperties.getCacheValue("childTab" + Constants.SECTION_HTML_ID);
		}
		if (null == htmlId) {
			htmlId = (String) CodeGenProperties.getCacheValue("single" + Constants.SECTION_HTML_ID);
		}
		
		String modelName = StringUtils.capitalize(getCamelCase(htmlId)) + "Model";
		CodeGenProperties.putCacheValue(Constants.MODEL_NAME, modelName);
		
		TypeScriptModel tsModel = new TypeScriptModel();
		tsModel.setModelName(modelName);
		if (modelTs.hasModel(modelName) == false) {
			modelTs.addTypeScriptModel(tsModel);
			modelTs.postProcess();

			Map<String, Object> contextVariables = new HashMap<>();
			contextVariables.put("ts", modelTs);
			
			String text = parser.parse(Constants.MODEL_TEMPLATE, contextVariables);
			
			FileUtils.writeStringToFile(file, text);
		}
		
		String userDefinedPath = (String) properties.get(Constants.USER_DEFINED_PATH);
		String moduleCompName = moduleName + Constants.FORWARD_SLASH + component.getComponentName();
		String componentPath = getAngularOuputFileName(moduleCompName, userDefinedPath);
		String componentFolder = StringUtils.substringBeforeLast(componentPath, Constants.FORWARD_SLASH);
		
		String modelFileName = modelPath + Constants.FORWARD_SLASH + moduleName + ".types";
		String relativePath = JsonUtils.getRelativePath(componentFolder, modelFileName);
		
		typeScript.addTypeScriptImport(relativePath, tsModel.getModelName());

		return tsModel;
	}

	/**
	 * @param fileName
	 * @param file
	 * @return
	 * @throws CodeGenException
	 */
	protected TypeScript parseExistingModelObject(String fileName, File file) throws CodeGenException {
		TypeScript modelTs = null;
		if (file.exists()) {
			TypeScriptParser tsParser = new TypeScriptParser(fileName);		
			tsParser.parse();

			modelTs = tsParser.getTsObject();
		} else {
			modelTs = new TypeScript();
		}
		return modelTs;
	}
	
	/**
	 * @param componentName
	 * @param componentCreationPath
	 * @throws IOException
	 */
	private void generateCss(String moduleName, Component component, String userDefinedPath, FormDetail formDetail) throws CodeGenException {
		
		String filePath = getAngularOuputFileName(moduleName, component.getComponentName(), userDefinedPath, "CSS", component.getCompStyleExt());
		
		JsonObject tsConfig = component.getTsConfig();
		if (tsConfig != null) {
			if (tsConfig.get("css") != null) {
				String defaultCss = tsConfig.get("css").getAsString();
				String cssContent = parser.parse(defaultCss, new HashMap<String, Object>());

				try {
					FileUtils.write(new File(filePath), cssContent);
				} catch (IOException e) {
					LOGGER.info("Exception while writing Css: e=" + e.getMessage());
					throw new CodeGenException(e);
				}
			}
		}
	}

	private void generateTypeScript(String moduleName, Component component, String userDefinedPath, FormDetail formDetail) {
		String filePath = getAngularOuputFileName(moduleName, component.getComponentName(), userDefinedPath, "TS", "");
		
		String tsTemplateName = "defaultTs";
		try {
			TypeScriptParser tsParser = new TypeScriptParser(filePath);
			tsParser.parse();
			
			TypeScript typeScript = tsParser.getTsObject();
			component.setCompTypeScript(typeScript);
			component.setCompTypeScriptPath(filePath);
			
			setFormElementToTypeScript(formDetail, typeScript);
			
			JsonObject tsConfig = component.getTsConfig();
			if (tsConfig != null) {
				if (tsConfig.get("imports") != null) {
					JsonArray imports = tsConfig.get("imports").getAsJsonArray();
					for (JsonElement jsonElement : imports) {
						typeScript.addTypeScriptImport(jsonElement.getAsJsonObject().get("packageName").getAsString(), 
														jsonElement.getAsJsonObject().get("importClasses").getAsString());
					}
				}
				
				if (tsConfig.get("templateName") != null) {
					tsTemplateName = tsConfig.get("templateName").getAsString();
				}
			}
			
			if (component.isCreateModel()) {
				generateComponentModel(typeScript, component);
			}
			
			if (component.getParams() != null && component.getParams().isEmpty() == false) {
				List<Params> paramsList = component.getParams();
				
				for (Params params : paramsList) {
					TypeScriptDeclarations tsDeclaration = new TypeScriptDeclarations();
					if (component.isReusableComponent()) {
						tsDeclaration.setAnnotation("Input");
					}
					
					tsDeclaration.setVariableName(getCamelCase(params.getParameterName()));
					tsDeclaration.setDataType(params.getParameterType() == null? "string": params.getParameterType());
					tsDeclaration.setDefaultValue(params.getDefaultValue());
					tsDeclaration.setActualValue(params.getActualValue());
					typeScript.addTypeScriptDeclaration(tsDeclaration);
				}
			}

			typeScript.postProcess();
			Map<String, Object> contextVariables = new HashMap<>();
			contextVariables.put("ts", typeScript);
			contextVariables.put("panelType", component.getPanelType());
			contextVariables.put("htmlID", sectionHtmlId);
			contextVariables.put("htmlIDCamCase", getCamelCase(sectionHtmlId));
			contextVariables.put("leftPanel"+Constants.SECTION_HTML_ID, CodeGenProperties.getCacheValue("leftPanel"+Constants.SECTION_HTML_ID));
			contextVariables.put("rightPanel"+Constants.SECTION_HTML_ID, CodeGenProperties.getCacheValue("rightPanel"+Constants.SECTION_HTML_ID));
			contextVariables.put(Constants.MODEL_NAME, CodeGenProperties.getCacheValue(Constants.MODEL_NAME));
			contextVariables.put("component", component);
			contextVariables.put("formDetail", formDetail);
			
			if (formDetail.getDataTables().isEmpty() == false) {
				contextVariables.put("dataTables", formDetail.getDataTables());
			} else {
				contextVariables.put("dataTables", null);
			}
			
			if (formDetail.getFormElementList().isEmpty() == false) {
				contextVariables.put("formElements", formDetail.getFormElementList());
			}
			
			String text = parser.parse(tsTemplateName, contextVariables);

			FileUtils.writeStringToFile(new File(filePath), text + "\n");
			
			// Parse Typescript after writing to get the final generated file
			// This is used for test case generation
			tsParser = new TypeScriptParser(filePath);
			tsParser.parse();
			tsParser.getTsObject().postProcess();
			
			component.setCompTypeScript(tsParser.getTsObject());
		} catch (CodeGenException e) {
			LOGGER.info("Error while generating TS. e=" + e.getMessage());
		} catch (IOException e) {
			LOGGER.info("Error while writing content to the file. e=" + e.getMessage());
		}

	}

	private void setFormElementToTypeScript(FormDetail formDetail, TypeScript typeScript) {
		List<FormElement> formElementList = formDetail.getFormElementList();
		if (formElementList != null && formElementList.isEmpty() == false) {
			addToNgOnInit(formElementList, typeScript);
			
			TypeScriptMethod tsMethod = typeScript.getMethodByName("constructor");
			
			if (tsMethod == null) {
				tsMethod = new TypeScriptMethod();
				tsMethod.setMethodName("constructor");
				
				typeScript.addTypeScriptMethod(tsMethod);
			}
			
			tsMethod.setMethodArgs("private formBuilder: FormBuilder");
			
			typeScript.addTypeScriptMethod("isFieldValid", sectionHtmlId, "");
			
			TypeScriptDeclarations dec = new TypeScriptDeclarations();
			dec.setVariableName(getCamelCase(sectionHtmlId));
			dec.setDataType("FormGroup");
			typeScript.addTypeScriptDeclaration(dec);
		}
		
		List<DataTable> dataTables = formDetail.getDataTables();
		
		for (DataTable dataTable : dataTables) {
			List<FormElement> dataTableList = dataTable.getDataTableList();
			if (dataTableList.isEmpty() == false) { 
				typeScript.addTypeScriptMethod("fetch", "", "");
				TypeScriptDeclarations tsDeclaration = typeScript.getDeclarationByName("columns");
				
				if (tsDeclaration == null) {
					tsDeclaration = new TypeScriptDeclarations();
					tsDeclaration.setVariableName("columns");
					
					typeScript.addTypeScriptDeclaration(tsDeclaration);
				}
				
				StringBuilder variableContent = new StringBuilder();
				
				for (FormElement formElement : dataTableList) {
					variableContent.append("{ ");
					variableContent.append(dataTable.getKeyField()); 
					variableContent.append(": '");
					variableContent.append(formElement.getPropValue());
					variableContent.append("', ");
					variableContent.append(dataTable.getValueField());
					variableContent.append(": '");
					variableContent.append(formElement.getDisplayColumnValue());
					variableContent.append("'},\n");
				}
				
				if (variableContent.length() > 2) {
					variableContent.deleteCharAt(variableContent.length() - 2);
				}
				
				variableContent.append("]");
				tsDeclaration.setDataTableProps(variableContent.toString());
			}
		}
	}

	/**
	 * @param formElementList
	 * @param typeScript
	 */
	protected void addToNgOnInit(List<FormElement> formElementList, TypeScript typeScript) {
		TypeScriptMethod tsMethod = typeScript.getMethodByName("ngOnInit");
		
		if (tsMethod == null) {
			tsMethod = new TypeScriptMethod();
			tsMethod.setMethodName("ngOnInit");
			
			typeScript.addTypeScriptMethod(tsMethod);
		}
		
		List<String> validatorList = new ArrayList<>();
		StringBuilder methodContent = new StringBuilder();
		methodContent.append("this.");
		methodContent.append(getCamelCase(sectionHtmlId));
		methodContent.append(" = this.formBuilder.group ({\n");
		
		for (FormElement formElement : formElementList) {
			validatorList.clear();
			
			methodContent.append("    ");
			methodContent.append(formElement.getHtmlId());
			methodContent.append(": [null");
			
			if (formElement.isMandatory() 
					&& validatorList.contains("Validators.required") == false) {
				validatorList.add("Validators.required");
			}
			
			if (formElement.getMinLength() > 0) {
				validatorList.add("Validators.minLength(" + formElement.getMinLength() + ")");
			}
			
			if (formElement.getMaxLength() > 0) {
				validatorList.add("Validators.maxLength(" + formElement.getMaxLength() + ")");
			}
			
			if (formElement.isEmail() 
					&& validatorList.contains("Validators.email") == false) {
				validatorList.add("Validators.email");
			}
			
			if (StringUtils.isNotBlank(formElement.getPattern())) {
				validatorList.add("Validators.pattern('" + formElement.getPattern() + "')");
			}
			
			if (validatorList.isEmpty() == false) {
				methodContent.append(", " + validatorList.toString());
			}
			
			
			
			methodContent.append("],\n");
		}
		
		methodContent.deleteCharAt(methodContent.length() - 2);
		methodContent.append("});");
		tsMethod.setMethodContent(methodContent.toString());
	}

	/**
	 * @param content
	 * @param sectionName
	 * @param componentName
	 * @return
	 */
	private Map<String, Object> getComponentParametersMap(StringBuilder content, String sectionName,
			String componentName) {
		Map<String, Object> contextVariables = new HashMap<>();
		
		// Adding the import based on the components present in the rendered content
		StringBuilder bootStrapStr = new StringBuilder();
		
		appendImport(content, bootStrapStr, "<PanelGroup", "PanelGroup");
		appendImport(content, bootStrapStr, "<Panel", "Panel");
		appendImport(content, bootStrapStr, "<Form", "Form");
		appendImport(content, bootStrapStr, "<FormGroup", "FormGroup");
		appendImport(content, bootStrapStr, "<ControlLabel", "ControlLabel");
		appendImport(content, bootStrapStr, "<FormControl", "FormControl");
		appendImport(content, bootStrapStr, "<DropdownButton", "DropdownButton");
		appendImport(content, bootStrapStr, "<Checkbox", "Checkbox");
		appendImport(content, bootStrapStr, "<MenuItem", "MenuItem");
		appendImport(content, bootStrapStr, "<Radio", "Radio");
		appendImport(content, bootStrapStr, "<Button", "Button");
		appendImport(content, bootStrapStr, "<Grid", "Grid");
		appendImport(content, bootStrapStr, "<Row", "Row");
		appendImport(content, bootStrapStr, "<Col", "Col");
		
		// Removing the trailing comma
		if (!bootStrapStr.toString().isEmpty()) {
			bootStrapStr.deleteCharAt(bootStrapStr.lastIndexOf(Constants.COMMA));
		}
		
		// Adding the date picker component to the import based on the rendered content
		String datePickStr = StringUtils.EMPTY;
		if (content.toString().contains("<DatePicker")) {
			datePickStr = "\n import DatePicker from 'react-datepicker';"
					+ "\n import 'react-datepicker/dist/react-datepicker.css';" 
					+ "\n import moment from 'moment';";
		}
		
		// Adding the bootstrap table component to the import based on the rendered
		// content
		StringBuilder bootStrapTblStr= new StringBuilder();
		appendImport(content, bootStrapTblStr, "<BootstrapTable", "BootstrapTable");
		appendImport(content, bootStrapTblStr, "<TableHeaderColumn", "TableHeaderColumn");
		
		String bootStrapTbl = StringUtils.EMPTY;
		if (!bootStrapTblStr.toString().isEmpty()) {
			bootStrapTblStr.deleteCharAt(bootStrapTblStr.lastIndexOf(Constants.COMMA));
			bootStrapTbl = "\n import {" + bootStrapTblStr.toString() + "} from 'react-bootstrap-table';"
					+ "\n import 'react-bootstrap-table/css/react-bootstrap-table.css';";
		}
		
		// Constructing the imports required for the rendered component
		StringBuilder imports = new StringBuilder("import {"+bootStrapStr.toString()+"} from 'react-bootstrap';"
				+ datePickStr
				+ bootStrapTbl);
		contextVariables.put("imports", imports.toString());
		
		CodeGenProperties.putCacheCompValue(Constants.SECTION_NAME, sectionName);
		CodeGenProperties.putCacheCompValue(Constants.COMPONENT_NAME, componentName);
		
		contextVariables.put(Constants.FTL_LABEL_STRIPWHITESPACE, componentName);
		contextVariables.put(Constants.FTL_CHILD_COMPONENT, content.toString());
		return contextVariables;
	}
	
	/**
	 * This method is used to append the string builder with the input components if
	 * the component is present in the rendered content
	 * 
	 * @param content
	 * @param importComponents
	 * @param componentName
	 * @param importStat
	 */
	private void appendImport(StringBuilder content, StringBuilder importComponents, String componentName,
			String importStat) {
		if (content.toString().contains(componentName)) {
			importComponents.append(importStat);
			importComponents.append(Constants.COMMA);
		}
	}
	
	/**
	 * @param element
	 * @param content
	 * @param childContentList 
	 * @param formElementList 
	 */
	@SuppressWarnings("unchecked")
	private void generateCode(JsonElement parentElement, JsonElement element, 
			StringBuilder content, List<ChildComponent> childContentList, int columnCount, FormDetail formDetail) {
		
		if (hasChildren(element)) {
			JsonElement childElement = getChild(element);
			
			List<ChildComponent> subChildContentList = new ArrayList<>();
			generateCode (element, childElement, content, subChildContentList, columnCount, formDetail);

			printChildContent(element, childElement, content, subChildContentList, formDetail);

			if (parentElement != null) {
				if (StringUtils.isNotBlank(content.toString())) {
					childContentList.add(new ChildComponent(content.toString(), 1, 1));
				} else if (subChildContentList.isEmpty() == false) {
					childContentList.addAll(subChildContentList);
				}
				
				content.delete(0, content.length());
			}
			
			return;
		}
		
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();

			addStateVariable(obj);
			
			String type = obj.get("type").getAsString();
			LOGGER.info("Component Type=" + type);

			addFormElement(formDetail.getFormElementList(), obj);
			
			DataTable dataTable = new DataTable();
			if (StringUtils.equalsIgnoreCase("datatable", type)) {
				dataTable.setDataTableHtmlId(getCamelCase(obj.get("htmlID").getAsString()));
				dataTable.setKeyField(obj.get("keyField").getAsString());
				dataTable.setValueField(obj.get("valueField").getAsString());
				
				addDatatableElement(dataTable.getDataTableList(), obj);
				
				formDetail.getDataTables().add(dataTable);
			}
			
			if (StringUtils.equalsIgnoreCase("section", type)) {
				System.out.println("Type=" + type);
			}
			
			try {
				Map<String, Object> contextVariables = getParametersMap(content, obj, type, false);
				loadComponentOptions(obj, contextVariables);
				contextVariables.put("formDetail", formDetail);
				contextVariables.put("datatable", dataTable);
				contextVariables.put("component", formDetail.getComponent());
				contextVariables.put("htmlIDCamCase", getCamelCase((String) contextVariables.get("htmlID")));
				
				if (type.equalsIgnoreCase("component")) {
					List<LayoutEntity> entities = layoutRepository.findByLayoutName(obj.get("componentName").getAsString());
					String layoutString = entities.get(0).getLayout();
					
					JsonArray layoutArray = new Gson().fromJson(layoutString, JsonObject.class).getAsJsonObject().getAsJsonArray("layout");
					String htmlID = layoutArray.get(0).getAsJsonObject().get("htmlID").getAsString();
					
					String componentSelector = formDetail.getComponent().getComponentPrefix() + "-" + htmlID.toLowerCase();
					formDetail.getComponent().setComponentSelector(componentSelector);
					LOGGER.info("Component Selector=" + componentSelector);
					
					List<Params> params = entities.get(0).getParams();
					JsonArray arrays = obj.get("componentParams").getAsJsonArray();
					if (arrays != null && arrays.size() > 0) {
						for (JsonElement arrayElement : arrays) {
							String name = arrayElement.getAsJsonObject().get("name").getAsString();
							for (Params compParam : params) {
								if (compParam.getParameterName().equalsIgnoreCase(name)) {
									compParam.setActualValue(arrayElement.getAsJsonObject().get("value").getAsString());
								}
							}
						}
					}
					
					
					formDetail.getComponent().setParams(params);
				}
				
				List<Component> componentsList = 
						(List<Component>) CodeGenProperties.getCacheValue(Constants.COMPONENTS_LIST);
				contextVariables.put("componentsList", componentsList);
				
				String elemContent = parser.parse(type, contextVariables);
				if (parentElement != null) {
					childContentList.add(new ChildComponent(elemContent));
				} else {
					content.append(elemContent);
				}
			} catch (CodeGenException e) {
				LOGGER.error("Error while parsing and rendering template: " + type, e);
			}
		} else if (element.isJsonArray()) {
			JsonArray childArray = element.getAsJsonArray();
			
			childArray.forEach(child -> {
				generateCode(parentElement, child, content, childContentList, childArray.size(), formDetail);
			});
		}
	}

	protected void addStateVariable(JsonObject obj) {
		String id = null;
		if (obj.get("htmlID") != null) {
			id = obj.get("htmlID").getAsString();
		}
		
		if (StringUtils.isNotBlank(id)) {
			Component component = CodeGenProperties.getCurrentReactComponent();
			
			if (component != null) {
				component.addVariables(id, "null", obj.get("type").getAsString());
			}
		}		
	}

	private void loadComponentOptions(JsonObject obj, Map<String, Object> contextVariables) {
		JsonElement element = obj.get("options");
		
		if (element != null && !"[]".equals(element.toString())) {
			List<ComponentOptions> optionsList = new ArrayList<>();
			
			JsonArray compOptions = element.getAsJsonArray();
			
			for (JsonElement childElement : compOptions) {
				System.out.println(childElement);
				JsonObject jsonObj = childElement.getAsJsonObject();
				
				Set<Entry<String, JsonElement>> optionValuesSet = jsonObj.entrySet();
				ComponentOptions option = new ComponentOptions();
				for (Entry<String, JsonElement> entry : optionValuesSet) {
					if("display".equals(entry.getKey())) {
						option.setValue(entry.getValue().getAsString());
					}
					if("value".equals(entry.getKey())) {
						option.setKey(entry.getValue().getAsString());
					}
				}
				optionsList.add(option);
			}
			
			contextVariables.put(Constants.COMPONENT_OPTIONS, optionsList);
		} 
	}

	/**
	 * @param element
	 * @param childElement 
	 * @param content
	 * @param childContentList
	 * @param formDetail 
	 */
	private void printChildContent(JsonElement element, JsonElement childElement, 
			StringBuilder content, List<ChildComponent> childContentList, FormDetail formDetail) {
		JsonObject parentObj = element.getAsJsonObject();
		
		String type = parentObj.get("type").getAsString();
		LOGGER.debug("Component Type=" + type);

		List<List<ChildComponent>> childComponentList = new ArrayList<>();

		int childContentIndex = 0;
		int totalRowCount = 1;
		int totalColCount = 0;
		ChildComponent childComp = null;
		if (childElement.isJsonArray()) {
			JsonArray childArray = childElement.getAsJsonArray();
			for (JsonElement colElement : childArray) {
				if (colElement.isJsonArray()) {
					JsonArray colArray = colElement.getAsJsonArray();
					for (int rowCount = 1; rowCount <= colArray.size(); rowCount++) {
						if (childComponentList.size() < rowCount) {
							childComponentList.add(new ArrayList<ChildComponent>());
						}
						
						childComp = childContentList.get(childContentIndex++);
						childComp.setRowNumber(rowCount);
						childComp.setColumnNumber(totalColCount + 1);
						
						if (parentObj.has("col" + childComp.getColumnNumber())) {
							String colPercent = parentObj.get("col" + childComp.getColumnNumber()).getAsString();
							colPercent = StringUtils.trim(colPercent.replace("%", ""));
							
							childComp.setColWidth(Double.parseDouble(colPercent));
						}
						
						childComp.setMd((int) Math.round((childComp.getColWidth() * 12) / 100));
						
						childComponentList.get(rowCount - 1).add(childComp);
					}
					
					totalRowCount = Math.max(totalRowCount, colElement.getAsJsonArray().size());
				} else {
					childComp = childContentList.get(childContentIndex++);
					childComp.setRowNumber(totalColCount + 1);
					childComp.setColumnNumber(1);
					
					if (parentObj.has("col" + childComp.getColumnNumber())) {
						String colPercent = parentObj.get("col" + childComp.getColumnNumber()).getAsString();
						colPercent = StringUtils.trim(colPercent.replace("%", ""));
						
						childComp.setColWidth(Double.parseDouble(colPercent));
					}
					childComp.setMd((int) Math.round((childComp.getColWidth() * 12) / 100));
					
					List<ChildComponent> newList = new ArrayList<ChildComponent>();
					newList.add(childComp);
					childComponentList.add(newList);
				}

				totalColCount ++;
			}
			
			if (totalColCount == 0) {
				totalColCount = 1;
				totalRowCount = childArray.size();
			}
			
			adjustRowWidth(childComponentList);
		}
		
		try {
			Map<String, Object> contextVariables = getParametersMap(content, parentObj, type, false);
			contextVariables.put(Constants.CHILDREN_ELEMENTS, childComponentList);
			contextVariables.put(Constants.ROW_COUNT, totalRowCount);
			contextVariables.put(Constants.COL_COUNT, totalColCount);
			contextVariables.put("component", formDetail.getComponent());
			contextVariables.put("htmlIDCamCase", getCamelCase((String) contextVariables.get("htmlID")));
			
			if (type.contains("section")) {
				System.out.println("test panel");
			}
			
			content.delete(0, content.length());
			content.append(parser.parse(type, contextVariables));
			/*if (StringUtils.isNotBlank(CodeGenProperties.getCacheValueAsString("singleSectionHtmlId"))
					&& formDetail.getDataTableList().isEmpty()) {
				content.append(parser.parse(tempName, contextVariables));
			} else {
				content.append(childComponentList.get(0).get(0).getElemContent());
			}*/
		} catch (CodeGenException e) {
			LOGGER.error("Error while parsing and rendering child components: " + type, e);
		}
	}

	private void adjustRowWidth(List<List<ChildComponent>> childComponentList) {
		if (childComponentList != null && childComponentList.isEmpty() == false) {
			for (List<ChildComponent> row : childComponentList) {
				int bootstrapCount = 0;
				if (row != null && row.isEmpty() == false) {
					for (ChildComponent column : row) {
						bootstrapCount += column.getMd();
					}
					
					if (bootstrapCount > 12) {
						int excessCount = bootstrapCount - 12;
						int ec = 0;
						int colIndex = row.size() - 1;
						while (ec < excessCount) {
							ChildComponent comp = row.get(colIndex);
							if (comp.getMd() > 1) {
								comp.setMd(comp.getMd() - 1);
								ec++;
							}
							
							colIndex--;
							if (colIndex < 0) {
								break;
							}
						}
						
					}
				}
			}
		}
		
	}

	private boolean hasChildren(JsonElement element) {
		boolean hasChild = false;
		
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			
			if (obj.get("columns") != null) {
				hasChild = true;
			}
		}
		
		return hasChild;
	}
	
	private JsonElement getChild(JsonElement element) {
		JsonElement child = null;
		
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			
			child = obj.get("columns");
		}
		
		return child;
	}
}
