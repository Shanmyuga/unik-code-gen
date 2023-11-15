package com.cognizant.fecodegen.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.Module;
import com.cognizant.fecodegen.bo.TypeScript;
import com.cognizant.fecodegen.bo.TypeScriptDeclarations;
import com.cognizant.fecodegen.bo.jsonoutput.RoutingModules;
import com.cognizant.fecodegen.components.render.IRenderer;
import com.cognizant.fecodegen.dao.LayoutRepository;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.service.RendererFactory;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.HelperUtil;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.cognizant.fecodegen.utils.ModuleParser;
import com.cognizant.fecodegen.utils.TypeScriptParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;


public class AngularJsGenerator extends CodeGenerator {
	
	@Autowired
	private LayoutRepository layoutRepository;

	@Autowired
	private HelperUtil helperUtil;
	
	/* (non-Javadoc)
	 * @see com.cognizant.fecodegen.components.CodeGenerator#generate()
	 */
	@Override
	public boolean generate() throws CodeGenException {
		boolean isNewProject = false;
		Constants.labelValues.clear();
		
		JsonObject componentTypes = configJson.getJsonObject("componentConfig");

		isNewProject = createProject(); 
		
		generateCode();
		
		if (isNewProject) {
			String postProjCmdTemplate = componentTypes.get("postCreation").getAsString();

			if (StringUtils.isNotBlank(postProjCmdTemplate)) {
				Map<String, Object> contextVariables = new HashMap<String, Object>();
				contextVariables.put("projectPath", request.getProjectPath());

				executeCommands(postProjCmdTemplate, contextVariables);
				LOGGER.info("Post Project commands executed successfully.");
			}
		}
		
		if(Constants.labelValues.size() > 0)
		{
			JsonArray languages = 
					componentTypes.get("i18nConfig").getAsJsonObject().get("languages").getAsJsonArray();
			try {
				for (JsonElement lang : languages) {
					writeJsonForLocalization(request.getProjectPath() + File.separator + "/src/assets/i18n/lang/" , lang.getAsString());
				}
			} catch (IOException e) {
				LOGGER.info("Error while writing the Locale json file. Error=" + e.getMessage());
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void writeJsonForLocalization(String filePath, String lang) throws IOException {
		String fileName = lang + ".json";
		JsonObject json = null;

		File file = new File(filePath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.mkdirs();
			file = new File(filePath + fileName);
			file.createNewFile();
		} else if (new File(filePath + fileName).exists()) {
			try {
				String existingFile = parser.readFileExternal(filePath + fileName);
				Map<String, String> existingMap = new Gson().fromJson(existingFile, HashMap.class);
				if (existingMap != null && existingMap.isEmpty() == false) {
					Constants.labelValues.putAll(existingMap);
				}
			} catch (JsonSyntaxException e) {
				LOGGER.info("Json Syntax while writing localization file: " + filePath);
			} catch (CodeGenException e) {
				LOGGER.info("Error while writing localization file: " + filePath);
			}
		}

		if(Constants.labelValues.size() > 0) {
			Map<String, String> appendLang = new HashMap<String, String>();
			Constants.labelValues.forEach((key, val) -> {
				String value = val;
				if (lang.equalsIgnoreCase("en") == false 
						&& val.endsWith(lang) == false) {
					value = val + "_" + lang;
				}
				appendLang.put(key, value);
			});
			
			String jsonStr =  new Gson().toJson(appendLang);
			json = new Gson().fromJson(jsonStr, JsonObject.class);
		}

		LOGGER.info("filePath : " +filePath);

		FileWriter writer = new FileWriter(filePath + fileName);
		writer.write(json.toString());
		writer.close();

		LOGGER.info("Written successfully " +filePath);
	}
	
	/**
	 * 
	 */
	protected void generateCode() {
		
		JsonArray layout = configJson.getJsonArray("layout");
		layout.forEach(je -> {
			JsonObject config = je.getAsJsonObject();
			String type = config.get("type").getAsString();
			
			LOGGER.debug("The type is :" + type);
			
			Map<String, Object> inputProps = new HashMap<String, Object>();
			inputProps.put(Constants.UI_NAME, request.getUiName());
			inputProps.put(Constants.APPNAME, request.getAppName());
			if (StringUtils.isNotEmpty(request.getComponentName())) {
				inputProps.put(Constants.COMPONENT_NAME, request.getComponentName());
			}
			if (StringUtils.isNotEmpty(request.getModuleName())) {
				inputProps.put(Constants.MODULE_NAME, request.getModuleName());
				inputProps.put(Constants.MODULE_PATH, request.getModulePath());
			}
			if (StringUtils.isNotEmpty(request.getUserDefinedPath())) {
				inputProps.put(Constants.USER_DEFINED_PATH, request.getUserDefinedPath());
			}
			
			try {
				IRenderer renderer = 
						RendererFactory.getRenderer(type,  request.getProjectPath(), 
							new JsonDocument(config), inputProps, configJson);
				
				renderer.initializeParams(new JsonDocument(config));
				renderer.render(new JsonDocument(config.get("params").getAsJsonObject()));
			} catch (CodeGenException e) {
				LOGGER.error("Error while processing: ", e);
			}
		});
	}

	/**
	 * @return 
	 * @throws CodeGenException 
	 * 
	 */
	protected boolean createProject() throws CodeGenException {
		JsonObject componentTypes = configJson.getJsonObject("componentConfig");

		boolean isNewProject = generateProjectStructure(componentTypes);
		
		generateAngularModule(componentTypes);
		
		JsonDocument jsonDoc = JsonUtils.getJsonObject(request.getUiLayout());
		jsonDoc.setLayoutRepository(layoutRepository);
		
		List<Component> componentList = jsonDoc.getComponentListNew(configJson, request);
		
		CodeGenProperties.putCacheValue(Constants.COMPONENTS_LIST, componentList);

		for (Component component : componentList) {
			generateAngularComponent(componentTypes, component);
		}
		
		return isNewProject;

	}
	
	/**
	 * This method is used to generate the angular module using the angular cli
	 * 
	 * @param projectPath
	 * @param moduleName
	 * @throws IOException
	 * @throws CodeGenException
	 */
	public void generateAngularModule(JsonObject componentConfig) throws CodeGenException {
		File modulePath = new File(request.getModulePath());
		if (modulePath.exists()) {
			LOGGER.info("Module: " + request.getModuleName() + " already exists. Skipped creation.");
			return;
		}
		
		String createModCmdTemplate = componentConfig.get("createModule").getAsString();
		
		Map<String, Object> contextVariables = new HashMap<String, Object>();
		contextVariables.put("projectPath", request.getProjectAppBasePath());
		contextVariables.put("moduleName", request.getModuleName());

		executeCommands(createModCmdTemplate, contextVariables);
		LOGGER.info("Module: " + request.getModuleName() + " created successfully.");
	}
	
	/**
	 * This method is used to generate the angular component using the angular cli
	 * @param component 
	 * 
	 * @param projectPath
	 * @param componentName
	 * @param cssName
	 * @throws CodeGenException
	 * @throws IOException
	 */
	public void generateAngularComponent(JsonObject componentConfig, Component component) throws CodeGenException {
		String formFieldComponent = componentConfig.get("formFieldComponent").getAsString();
		String compPrefix = configJson.getComponentPrefix();
							
		String modulePath = request.getModulePath();

		try {
			Path path = Paths.get(modulePath);
			Files.createDirectories(path);
		} catch (IOException e2) {
			LOGGER.error("Error while creating directories for component", e2);	
			LOGGER.info("Error while creating directories for component: " +  e2.getMessage());
		}

		
		component.setAppTypeScriptPath(request.getProjectAppBasePath() + Constants.FORWARD_SLASH + Constants.APP + Constants.APP_COMPONENT_TS);
		component.setAppTypeScript(parseAndGetTypeScript(component.getAppTypeScriptPath()));
		
		component.setAppModulePath(request.getProjectAppBasePath() + Constants.FORWARD_SLASH + Constants.APP + Constants.APP_MODULE_TS);
		component.setAppModule(parseAndGetModule(component.getAppModulePath()));
		
		component.setCompModulePath(modulePath + Constants.FORWARD_SLASH + request.getModuleName() + Constants.APP_MODULE_TS);
		component.setCompModule(parseAndGetModule(component.getCompModulePath()));
		
		File componentPath = new File(modulePath + Constants.FORWARD_SLASH + component.getComponentName());
		if (componentPath.exists()) {
			LOGGER.info("Component: " + component.getComponentName() + " already exists. Skipped creation.");
			return;
		}
		
		String createCompCmdTemplate = componentConfig.get("createComponent").getAsString();
		
		Map<String, Object> contextVariables = new HashMap<String, Object>();
		contextVariables.put("modulePath", modulePath);
		contextVariables.put("componentName", component.getComponentName());

		executeCommands(createCompCmdTemplate, contextVariables);
		LOGGER.info("Component: " + request.getModuleName() + " created successfully.");
		

		// Update the Routing Module for Primary Component
		if (component.isPrimary()) {
			RoutingModules routingModule = getRoutingModule();

			// add entries for component in app-routing.ts
			helperUtil.parseRoutingFile(request.getProjectAppBasePath(), "app-routing.module.ts",
					request.getProjectPath(), routingModule, true, request.getModuleName() + "/" + component.getComponentName(), 
					request.getUserDefinedPath(), formFieldComponent);
		}
		
		// add entries for component in app.module.ts if user defined path is empty
		helperUtil.parseModuleFile(request.getModuleName() + "/" + component.getComponentName(), request.getUserDefinedPath(),
				request.getModuleName(), component, true);

		// add entries for component in module.module.ts if userDefined path is given
		helperUtil.parseModuleFile(request.getModuleName() + "/" + component.getComponentName(), 
				request.getUserDefinedPath(), request.getModuleName(), component, false);

		// CSS Configuration:: Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource("css/" + request.getCss());
		String urlPath = url.getPath().replace("target\\classes", "src\\main\\resources");
		File file = new File(urlPath);
		if (file.exists() == false) {
			String resourcePath = System.getProperty("resource.path");
			if (StringUtils.isNotBlank(resourcePath)) {
				file = new File(resourcePath + "/css/" + request.getCss());
			}
		}
		
		// Component Specific CSS file
		try {
			Files.copy(file.toPath(), new File(request.getModulePath() + File.separator + component.getComponentName()
					+ File.separator + component.getComponentName() + ".component." + component.getCompStyleExt()).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Module parseAndGetModule(String modulePath) throws CodeGenException {
		ModuleParser modParser = new ModuleParser(modulePath);
		modParser.parse();

		Module module = modParser.getModObject();
		
		return module;
	}

	private TypeScript parseAndGetTypeScript(String typeScriptPath) throws CodeGenException {
		TypeScriptParser typeScriptParser = new TypeScriptParser(typeScriptPath);
		typeScriptParser.parse();

		TypeScript typeScript = typeScriptParser.getTsObject();
		
		return typeScript;
	}
	
	/**
	 * @return
	 */
	protected RoutingModules getRoutingModule() {
		Gson gson = null;
		BufferedReader br = null;
		RoutingModules routingModule = null;
		try {
			File routingFile = new File(request.getProjectPath() + File.separator + Constants.ROUTING_CONF_JSON);
			if (routingFile.exists()) {
				gson = new Gson();
				br = new BufferedReader(
						new FileReader(routingFile));
				routingModule = gson.fromJson(br, RoutingModules.class);
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("Error while loading RoutingModule", e1);	
			LOGGER.info("Error while loading RoutingModule: " +  e1.getMessage());
		} finally {
			IOUtils.closeQuietly(br);
		}
		return routingModule;
	}

	/**
	 * @param componentConfig 
	 * @throws CodeGenException 
	 * 
	 */
	protected boolean generateProjectStructure(JsonObject componentConfig) throws CodeGenException {
		String createProjCmdTemplate = componentConfig.get("createProject").getAsString();
		String formFieldComponent = componentConfig.get("formFieldComponent").getAsString();

		File projectPathFile = new File(request.getProjectPath());
		File angularFile = new File(request.getProjectPath() + "/angular.json");
		if (projectPathFile.exists() && angularFile.exists()) {
			LOGGER.info("Project folder already exists. Skipping creation.");
			return false;
		}
		
		Map<String, Object> contextVariables = new HashMap<String, Object>();
		contextVariables.put("projectPath", request.getCodeGenPath());
		contextVariables.put("projectName", request.getProjectName());
		contextVariables.put("styleExt", componentConfig.get("styleExt").getAsString());
		contextVariables.put("componentPrefix", componentConfig.get("componentPrefix").getAsString());

		executeCommands(createProjCmdTemplate, contextVariables);
		LOGGER.info("Project Structure created successfully.");

		updateInitConfig(componentConfig);
		
		try {
			helperUtil.parseRoutingFile(request.getProjectAppBasePath(), "app-routing.module.ts",
					request.getProjectPath(), null, false, Constants.APP, StringUtils.EMPTY, formFieldComponent);
		} catch (CodeGenException e) {
			LOGGER.info("Error while parsing the app routing file. e=" + e.getMessage());
		}
		
		LOGGER.info("App Module and Routing configuration updated.");
		
		String currentModuleName = request.getModuleName();
		String currentComponentName = request.getComponentName();
		Component component = new Component();
		component.setComponentName(componentConfig.get("formFieldComponent").getAsString());

		request.setModuleName(componentConfig.get("commonModule").getAsString());
		request.setComponentName(componentConfig.get("formFieldComponent").getAsString());

		generateAngularModule(componentConfig);
		generateAngularComponent(componentConfig, component);
		
		parseCommonModule(componentConfig);
		parseErrorComponentTs(componentConfig);
		
		request.setModuleName(currentModuleName);
		request.setComponentName(currentComponentName);
		
		return true;
	}

	private void updateInitConfig(JsonObject componentConfig) {
		if (componentConfig.get("initConfig") != null) {
			try {
				JsonObject initConfig = componentConfig.get("initConfig").getAsJsonObject();
				
				String filePath = request.getProjectPath() + Constants.FORWARD_SLASH + "angular.json";
				String angJsonString = parser.readFileExternal(filePath);
				JsonDocument angJson = JsonUtils.getJsonObject(angJsonString);
				
				JsonObject optionsObj = angJson.getJsonObject("projects").getAsJsonObject()
						.get(request.getProjectName()).getAsJsonObject()
						.get("architect").getAsJsonObject()
						.get("build").getAsJsonObject()
						.get("options").getAsJsonObject();

				if (initConfig.get("js") != null) {
					JsonArray jsArray = initConfig.get("js").getAsJsonArray();
					optionsObj.get("scripts").getAsJsonArray().addAll(jsArray);
					
				}
				
				if (initConfig.get("css") != null) {
					JsonArray cssArray = initConfig.get("css").getAsJsonArray();
					
					JsonArray newCssArray = new JsonArray();
					for (JsonElement jsonElement : cssArray) {
						if (StringUtils.contains(jsonElement.getAsString(), "styles")) {
							String css = StringUtils.substringBeforeLast(jsonElement.getAsString(), ".");
							newCssArray.add(css + "." + componentConfig.get("styleExt").getAsString());
						} else {
							newCssArray.add(jsonElement);
						}
					}
					
					optionsObj.get("styles").getAsJsonArray().remove(0);
					
					optionsObj.get("styles").getAsJsonArray().addAll(newCssArray );
				}
				
				FileWriter writer = new FileWriter(filePath);
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				writer.write(gson.toJson(angJson.getJson()));
				writer.close();
				
			} catch (CodeGenException e) {
				LOGGER.error("Error while updating angular.json: " + e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error("Error while updating angular.json: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * @param componentConfig
	 * @throws CodeGenException
	 */
	private void parseErrorComponentTs(JsonObject componentConfig) throws CodeGenException {
		Map<String, Object> contextVariables;
		String tsFilePath = request.getComponentPath() + File.separator + request.getComponentName() + ".component.ts";
		TypeScriptParser tsParser = new TypeScriptParser(tsFilePath);
		tsParser.parse();

		TypeScript fieldErrorTs = tsParser.getTsObject();

		JsonObject tsConfig = componentConfig.get("tsConfig").getAsJsonObject();
		
		JsonArray importArray = tsConfig.get("imports").getAsJsonArray();
		importArray.forEach(element -> {
			if (element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				
				String packageName = obj.get("packageName").getAsString();
				String importClasses = obj.get("importClasses").getAsString();
				
				fieldErrorTs.addTypeScriptImport(packageName, importClasses);
			}
		});
		
		JsonArray declarationsArray = tsConfig.get("declarations").getAsJsonArray();
		declarationsArray.forEach(element -> {
			TypeScriptDeclarations tsd = new TypeScriptDeclarations();
			if (element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				
				String annotation = obj.get("annotation").getAsString();
				String variableName = obj.get("variableName").getAsString();
				String datatype = obj.get("datatype").getAsString();
				
				tsd.setAnnotation(annotation);
				tsd.setVariableName(variableName);
				tsd.setDataType(datatype);
				fieldErrorTs.addTypeScriptDeclaration(tsd);
			}
		});
		
		String componentTemplateName = componentConfig.get("componentTemplateName").getAsString();

		contextVariables = new HashMap<>();
		contextVariables.put("ts", fieldErrorTs);
		String text = parser.parse(componentTemplateName, contextVariables);

		try {
			FileWriter writer = new FileWriter(tsFilePath);
			writer.write(text + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param componentConfig
	 * @throws CodeGenException
	 */
	private void parseCommonModule(JsonObject componentConfig) throws CodeGenException {
		Map<String, Object> contextVariables;
		String tsFilePath = request.getModulePath() + File.separator + componentConfig.get("commonModule").getAsString()
				+ ".module.ts";
		ModuleParser modParser = new ModuleParser(tsFilePath);
		modParser.parse();

		Module moduleTs = modParser.getModObject();

		moduleTs.addNgModuleExport(moduleTs.getNgModuleDeclarations().get(0));

		String moduleTemplateName = componentConfig.get("moduleTemplateName").getAsString();

		contextVariables = new HashMap<>();
		contextVariables.put("ts", moduleTs);
		String text = parser.parse(moduleTemplateName, contextVariables);

		FileWriter writer;
		try {
			writer = new FileWriter(tsFilePath);
			writer.write(text + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param createProjCmdTemplate
	 * @param contextVariables
	 */
	protected void executeCommands(String createProjCmdTemplate, Map<String, Object> contextVariables) {
		try {
			String commands = parser.parse(createProjCmdTemplate, contextVariables);

			String[] cmds = commands.split("\n");
			for (String cmd : cmds) {
				HelperUtil.codeGenerate(cmd);
			}
		} catch (CodeGenException e1) {
			e1.printStackTrace();
		}
	}
}
