/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.ChildComponent;
import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.ComponentOptions;
import com.cognizant.fecodegen.bo.DataTable;
import com.cognizant.fecodegen.bo.FormDetail;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.StateVariable;
import com.cognizant.fecodegen.bo.TabComponent;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class ReactComponentRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactComponentRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactComponentRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		
		LOGGER.info("Creating UI Component... ");
		LOGGER.debug (jsonDoc);

		String sectionName = (String) properties.get(Constants.UI_NAME);
		String componentName = StringUtils.replace(sectionName, " ", "");
		if (StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), 
				(String) properties.get(Constants.GENERATE_LOGIN_SCREEN))) {
			sectionName = Constants.LOGIN;
			componentName = Constants.LOGIN;
		} 
		CodeGenProperties.putCacheValue(Constants.CURRENT_COMPONENT_NAME, componentName);

		JsonArray uiLayout = jsonDoc.getJsonArray("layout");
		StringBuilder content = new StringBuilder();
		List<ChildComponent> childContentList = new ArrayList<>();
		
		FormDetail formDetail = new FormDetail();
		
		uiLayout.forEach(element -> {

			// Generate code for child tabs
			generateChildTabs(content, childContentList, formDetail, element);

			if ("Y".equals(CodeGenProperties.getCacheValueAsString("isTab"))) {

				// Generate code for parent tab
				printParentTab(element);
			} else {
				generateCode(null, element, content, childContentList, uiLayout.size(), formDetail);
			}
			// generateCode(null, element, content, childContentList, uiLayout.size(),
			// formDetail);
		});
		
		if (!"Y".equals(CodeGenProperties.getCacheValueAsString("isTab"))) {

			Component currentComponent = CodeGenProperties.getCurrentReactComponent();
			String finalContent = getContentToWrite(sectionName, currentComponent, content, formDetail);

			content.delete(0, content.length());
			content.append(finalContent);

			Map<String, Object> contextVariables = getComponentParametersMap(content, sectionName, componentName,
					formDetail);

			contextVariables.put(Constants.STATE_VARIABLES, currentComponent.getVariables());
			contextVariables.put("formName", JsonUtils.toCamelCase(sectionName));
			contextVariables.put("component", currentComponent);

			try {
				write(parser.parse(templateName, contextVariables), componentName);

				CodeGenProperties.putCacheCompValue(Constants.COMPONENT_FILENAME, getSavedFileName());
			} catch (CodeGenException e) {
				LOGGER.error("Error while rendering Component: ", e);
			}
		} else {
			CodeGenProperties.putCacheValue("isTab", null);
		}
		
		return false;
	}

	/**
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private void printParentTab(JsonElement element) {
		String parentTabName = StringUtils.EMPTY;
		String htmlID = StringUtils.EMPTY;
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();
			parentTabName = obj.get("label").getAsString();
			htmlID = obj.get("htmlID").getAsString();
		}
		String sectName = parentTabName;
		String compName = StringUtils.replace(sectName, " ", "");
		
		CodeGenProperties.putCacheValue(Constants.CURRENT_COMPONENT_NAME, compName);
		CodeGenProperties.putCacheValue("TabComponentName", compName);
		CodeGenProperties.putCacheValue("TabSectionName", sectName);

		TabComponent tabComp = new TabComponent();
		tabComp.setComponentName(compName);
		tabComp.setSectionName(sectName);
		tabComp.setHtmlID(htmlID);

		Map<String, String> tabMap = (Map<String, String>) CodeGenProperties.getCacheValue("tabs");
		tabComp.setTabCompMap(tabMap);

		Map<String, Object> contextVariables = new HashMap<>();

		contextVariables.put("tabComp", tabComp);
		try {

			CodeGenProperties.putCacheCompValue(Constants.SECTION_NAME, sectName);
			CodeGenProperties.putCacheCompValue(Constants.COMPONENT_NAME, compName);

			write(parser.parse("fm/react/default/reacttabcomponent.ftl", contextVariables), compName);
			
			CodeGenProperties.putCacheCompValue(Constants.COMPONENT_FILENAME, getSavedFileName());
		} catch (CodeGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param content
	 * @param childContentList
	 * @param formDetail
	 * @param element
	 */
	private void generateChildTabs(StringBuilder content, List<ChildComponent> childContentList, FormDetail formDetail,
			JsonElement element) {
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();

			if (obj.get("panelType") != null && "tabs".equalsIgnoreCase(obj.get("panelType").getAsString())) {
				JsonElement childTabElement = obj.get("columns");
				for (JsonElement childElem : childTabElement.getAsJsonArray()) {
					JsonArray childTabArray = childElem.getAsJsonArray();
					Map<String, String> tabMap = new HashMap<>();
					childTabArray.forEach(child -> {
						String tabSectionName = child.getAsJsonObject().get("label").getAsString();
						String tabComponentName = StringUtils.replace(tabSectionName, " ", "");
						tabMap.put(tabComponentName, tabSectionName);

						CodeGenProperties.putCacheValue(Constants.CURRENT_COMPONENT_NAME, tabComponentName);
						CodeGenProperties.putCacheValue("isTab", "Y");

						generateCode(null, child, content, childContentList, childTabArray.size(), formDetail);

						Component currentComponent = CodeGenProperties.getCurrentReactComponent();
						String finalContent = null;
						try {
							finalContent = getContentToWrite(tabComponentName, currentComponent, content, formDetail);
						} catch (CodeGenException e1) {
							e1.printStackTrace();
						}

						content.delete(0, content.length());
						content.append(finalContent);

						Map<String, Object> contextVariables = getComponentParametersMap(content, tabSectionName,
								tabComponentName, formDetail);

						contextVariables.put(Constants.STATE_VARIABLES, currentComponent.getVariables());
						contextVariables.put("formName", JsonUtils.toCamelCase(tabSectionName));
						contextVariables.put("component", currentComponent);

						try {
							write(parser.parse(templateName, contextVariables), tabComponentName);

							CodeGenProperties.putCacheCompValue("isTab", "Y");
							CodeGenProperties.putCacheCompValue(Constants.COMPONENT_FILENAME, getSavedFileName());
						} catch (CodeGenException e) {
							LOGGER.error("Error while rendering Component: ", e);
						}
					});
					CodeGenProperties.putCacheValue("tabs", tabMap);
				}
			}
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
					getComponentParametersMap(content, sectionName, component.getComponentName(), formDetail);

			Component currentComponent = CodeGenProperties.getCurrentReactComponent();
			contextVariables.put(Constants.STATE_VARIABLES, currentComponent.getVariables());

			contextVariables.put("formContent", content.toString());
			contextVariables.put("htmlID", JsonUtils.toCamelCase(sectionName));
			contextVariables.put("component", component);
			
			finalContent = parser.parse(getTemplateName(Constants.FORM_TYPE), contextVariables);
			
			addImports(Constants.FORM_TYPE, formDetail);
		}
		return finalContent;
	}
	
	/**
	 * @param content
	 * @param sectionName
	 * @param componentName
	 * @param formDetail 
	 * @return
	 */
	private Map<String, Object> getComponentParametersMap(StringBuilder content, String sectionName,
			String componentName, FormDetail formDetail) {
		Map<String, Object> contextVariables = new HashMap<>();
		
		contextVariables.put("formDetail", formDetail);
		
		CodeGenProperties.putCacheCompValue(Constants.SECTION_NAME, sectionName);
		CodeGenProperties.putCacheCompValue(Constants.COMPONENT_NAME, componentName);
		
		contextVariables.put(Constants.FTL_LABEL_STRIPWHITESPACE, componentName);
		contextVariables.put(Constants.FTL_CHILD_COMPONENT, content.toString());
		return contextVariables;
	}

	/**
	 * @param element
	 * @param content
	 * @param childContentList 
	 */
	private void generateCode(JsonElement parentElement, JsonElement element, 
			StringBuilder content, List<ChildComponent> childContentList, int columnCount, FormDetail formDetail) {
		
		if (hasChildren(element)) {
			JsonElement childElement = getChild(element);
			
			List<ChildComponent> subChildContentList = new ArrayList<>();
			generateCode (element, childElement, content, subChildContentList, columnCount, formDetail);

			if (element.isJsonObject()) {
				addStateVariable(element.getAsJsonObject(), true);
			}
			
			printChildContent(element, childElement, content, subChildContentList, formDetail);

			if (parentElement != null) {
				childContentList.add(new ChildComponent(content.toString(), 1, 1));

				content.delete(0, content.length());
			}
			
			return;
		}
		
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();

			addStateVariable(obj, false);
			
			addFormElement(formDetail.getFormElementList(), obj);
			
			String type = obj.get("type").getAsString();
			String tempName = getTemplateName(type);
			LOGGER.debug("Component Type=" + type + " ; Template Name="+tempName);

			DataTable dataTable = new DataTable();
			if (StringUtils.equalsIgnoreCase("datatable", type)) {
				dataTable.setDataTableHtmlId(getCamelCase(obj.get("htmlID").getAsString()));
				addDatatableElement(dataTable.getDataTableList(), obj);
				
				formDetail.getDataTables().add(dataTable);
			}
			
			addImports(type, formDetail);

			try {
				Map<String, Object> contextVariables = getParametersMap(content, obj, tempName, false);
				loadComponentOptions(obj, contextVariables);

				Component currentComponent = CodeGenProperties.getCurrentReactComponent();
				contextVariables.put("component", currentComponent);
				contextVariables.put("formDetail", formDetail);
				
				String elemContent = parser.parse(tempName, contextVariables);
				if (parentElement != null) {
					childContentList.add(new ChildComponent(elemContent));
				} else {
					content.append(elemContent);
				}
			} catch (CodeGenException e) {
				LOGGER.error("Error while parsing and rendering template: " + tempName, e);
			}
		} else if (element.isJsonArray()) {
			JsonArray childArray = element.getAsJsonArray();
			
			childArray.forEach(child -> {
				generateCode(parentElement, child, content, childContentList, childArray.size(), formDetail);
			});
		}
	}
	
	protected void addStateVariable(JsonObject obj, boolean isContainerVariable) {
		String id = null;
		if (obj.get("htmlID") != null) {
			id = obj.get("htmlID").getAsString();
		} else if (obj.get("htmlids") != null) {
			id = obj.get("htmlids").getAsString();
		}
		
		if (StringUtils.isNotBlank(id)) {
			Component component = CodeGenProperties.getCurrentReactComponent();
			
			if (component != null) {
				StateVariable var = null;
				
				if (isContainerVariable) {
					var = component.addContainerVariables(id, "null");
				} else {
					var = component.addVariables(id, "null", obj.get("type").getAsString());
				}
				
				if (obj.get("mandatory") != null) {
					var.setMandatory(obj.get("mandatory").getAsString());
				} else {
					var.setMandatory("false");
				}
				
				if (obj.get("minLength") != null && obj.get("minLength").getAsInt() > 0) {
					var.setMinLength(obj.get("minLength").getAsString());
				} else {
					var.setMinLength(null);
				}
				
				if (obj.get("maxLength") != null && obj.get("maxLength").getAsInt() > 0) {
					var.setMaxLength(obj.get("maxLength").getAsString());
				} else {
					var.setMaxLength(null);
				}
				
				if (obj.get("email") != null) {
					var.setEmail(obj.get("email").getAsString());
				} else {
					var.setEmail(null);
				}
				
				if (obj.get("pattern") != null) {
					var.setPattern(obj.get("pattern").getAsString());
				} else {
					var.setPattern(null);
				}
				
				if (obj.get("label") != null) {
					var.setLabel(obj.get("label").getAsString());
				} 
				
				if (obj.get("htmlids") != null && obj.get("display") != null) {
					var.setLabel(obj.get("display").getAsString());
				}
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
					if("htmlids".equals(entry.getKey())) {
						option.setHtmlID(entry.getValue().getAsString());
					}
				}
				optionsList.add(option);
				
				addStateVariable(childElement.getAsJsonObject(), true);
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
		String tempName = getTemplateName(type);
		LOGGER.debug("Component Type=" + type + " ; Template Name="+tempName);

		addImports(type, formDetail);

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
			Map<String, Object> contextVariables = getParametersMap(content, parentObj, tempName, false);
			contextVariables.put(Constants.CHILDREN_ELEMENTS, childComponentList);
			contextVariables.put(Constants.ROW_COUNT, totalRowCount);
			contextVariables.put(Constants.COL_COUNT, totalColCount);
			
			content.delete(0, content.length());
			content.append(parser.parse(tempName, contextVariables));
		} catch (CodeGenException e) {
			LOGGER.error("Error while parsing and rendering child components: " + tempName, e);
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
