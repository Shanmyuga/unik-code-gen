/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.dao.LayoutRepository;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class JsonDocument {
	
	private LayoutRepository layoutRepository;
	
	private JsonObject json;
	
	public JsonDocument(JsonObject json) {
		this.json = json;
	}

	public JsonObject getJsonObject(String keyName) {
		JsonElement element = json.get(keyName);
		if (element!= null && element.isJsonObject()) {
			return element.getAsJsonObject();
		}
		
		return null;
	}
	
	public JsonArray getJsonArray(String keyName) {
		JsonElement element = json.get(keyName);
		if (element!= null && element.isJsonArray()) {
			return element.getAsJsonArray();
		}
		
		return null;
	}
	
	public String getAsString(String keyName) {
		JsonElement element = json.get(keyName);
		if (element!= null && element.isJsonPrimitive()) {
			return element.getAsString();
		}
		
		return null;
	}

	public Map<String, Object> getAsMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		json.entrySet().forEach(e -> {
			params.put(e.getKey(), e.getValue().toString());
		});
		
		return params;
	}

	public String getComponentPrefix() {
		JsonElement element = json.get("componentConfig");
		if (element!= null && element.isJsonObject()) {
			JsonElement compPrefix = element.getAsJsonObject().get("componentPrefix");
			if (compPrefix != null) {
				return compPrefix.getAsString();
			}
		}
		
		return null;
	}
	
	public List<Component> getComponentListNew(JsonDocument configJson, CodeGenRequest request) {
		List<Component> componentsList = new ArrayList<>();
		JsonElement layoutElement = json.get("layout");
		
		findAddCompFromLayout(componentsList, null, configJson, layoutElement, request);
		
		if (componentsList.isEmpty() == false) {
			componentsList.get(0).setPrimary(true);
		}
		
		return componentsList;
	}
	
	private void findAddCompFromLayout(List<Component> componentsList, Component parentComponent, JsonDocument configJson,
			JsonElement layoutElement, CodeGenRequest request) {
		if (layoutElement == null) {
			return;
		}
		
		if (layoutElement.isJsonArray()) {
			JsonArray arrays = layoutElement.getAsJsonArray();
			for (JsonElement arrayElem : arrays) {
				findAddCompFromLayout(componentsList, parentComponent, configJson, arrayElem, request);
			}
		} else {
			JsonObject layoutJson = layoutElement.getAsJsonObject();
			
			String panelType = getPanelType(layoutElement);
			Component component = null; 
			
			String componentSelector = null;
			if ("na".equalsIgnoreCase(panelType)) {
				return;
			} 
			
			component = createComponent(panelType, parentComponent, configJson, request, layoutJson);
			component.setComponentSelector(componentSelector);
			if ("true".equalsIgnoreCase(request.getReusableComponent())) {
				component.setReusableComponent(true);
				component.setParams(request.getParams());
			}
			componentsList.add(component);

			if (layoutJson.has("columns")) {
				findAddCompFromLayout(componentsList, component, configJson, layoutJson.get("columns"), request);
			}

			return;
		}
	}

	private void filterForNestedLayout(Component component, JsonDocument configJson, CodeGenRequest request) {
		JsonArray newJsonArray = new JsonArray();
		if (StringUtils.equalsIgnoreCase("childTab", component.getPanelType())
				|| StringUtils.equalsIgnoreCase("leftPanel", component.getPanelType())
				|| StringUtils.equalsIgnoreCase("rightPanel", component.getPanelType())
				|| StringUtils.equalsIgnoreCase("single", component.getPanelType())) {
			JsonObject childTabJson = component.getUiLayout().get(0).getAsJsonObject();
			
			JsonArray childTabContentJson = childTabJson.get("columns").getAsJsonArray();
			for (JsonElement childElement : childTabContentJson) {
				if (childElement.isJsonArray()) {
					for (JsonElement childElem : childElement.getAsJsonArray()) {
						addToFinalJson(newJsonArray, component, childElem, configJson, request);
					}
				} else {
					addToFinalJson(newJsonArray, component, childElement, configJson, request);
				}
			}
			
			if (StringUtils.equalsIgnoreCase("single", component.getPanelType())) {
				JsonArray tmpJsonArray = newJsonArray;
				
				newJsonArray = new JsonArray();
				JsonObject singlePanel = new JsonObject();
				singlePanel.addProperty("id", childTabJson.get("id").getAsString());
				singlePanel.addProperty("htmlID", childTabJson.get("htmlID").getAsString());
				singlePanel.addProperty("label", childTabJson.get("label").getAsString());
				singlePanel.addProperty("panelType", childTabJson.get("panelType").getAsString());
				singlePanel.addProperty("section", childTabJson.get("section").getAsString());
				singlePanel.addProperty("type", childTabJson.get("type").getAsString());
				singlePanel.addProperty("styleType", childTabJson.get("styleType").getAsString());
				
				if (childTabJson.get("collapsible") != null) {
					singlePanel.addProperty("collapsible", childTabJson.get("collapsible").getAsBoolean());
				} else {
					singlePanel.addProperty("collapsible", false);
				}
				singlePanel.add("columns", tmpJsonArray);
				
				newJsonArray.add(singlePanel);
			}
			
			component.setUiLayout(newJsonArray);
		}
	}

	/**
	 * @param newJsonArray
	 * @param component
	 * @param childElement
	 * @param configJson
	 * @param request
	 */
	protected void addToFinalJson(JsonArray newJsonArray, Component component, JsonElement childElement,
			JsonDocument configJson, CodeGenRequest request) {
		if (childElement.isJsonArray()) {
			for (JsonElement childElem : childElement.getAsJsonArray()) {
				addToFinalJson(newJsonArray, component, childElem, configJson, request);
			}
			
			return;
		} 
		
		String type = childElement.getAsJsonObject().get("type").getAsString();
		
		if (StringUtils.equalsIgnoreCase(type, "section")) {
			String panelType = childElement.getAsJsonObject().get("panelType").getAsString();

			JsonObject panelConfigJson = 
					configJson.getJson().get("componentConfig").getAsJsonObject()
										.get("panelTypeConfig").getAsJsonObject()
										.get(panelType).getAsJsonObject();
			
			if (StringUtils.equalsIgnoreCase(panelType, "navbars")
					|| StringUtils.equalsIgnoreCase(panelType, "tabs")
					|| StringUtils.equalsIgnoreCase(panelType, "single")) {
				JsonObject newElem = new JsonObject();
				newElem.addProperty("type", "angComponent");
				newElem.addProperty("componentName", generateComponentName(request, panelType, panelConfigJson, childElement.getAsJsonObject()));
				newElem.addProperty("prefix", component.getComponentPrefix());
				
				newJsonArray.add(newElem);
			} else {
				if (childElement.getAsJsonObject().has("columns")) {
					JsonObject childElementClone = JsonUtils.deepCopy(childElement.getAsJsonObject(), JsonObject.class);
					JsonArray newChildElemArray = new JsonArray();
					
					JsonArray childElemArray = childElementClone.getAsJsonObject().get("columns").getAsJsonArray();
					for (JsonElement jsonElement : childElemArray) {
						addToFinalJson(newChildElemArray, component, jsonElement, configJson, request);
					}
					
					childElementClone.getAsJsonObject().add("columns", newChildElemArray);
					newJsonArray.add(childElementClone);
				} else {
					newJsonArray.add(childElement);
				}
			}
			
		} else {
			newJsonArray.add(childElement);
		}
	}

	/**
	 * @param panelType
	 * @param parentComponent 
	 * @param configJson
	 * @param request
	 * @param uiLayout
	 * @return
	 */
	protected Component createComponent(String panelType, Component parentComponent, JsonDocument configJson, CodeGenRequest request,
			JsonObject layoutJson) {
		JsonObject compConfigJson = 
				configJson.getJson().get("componentConfig").getAsJsonObject();
				
		JsonObject panelConfigJson = 
				configJson.getJson().get("componentConfig").getAsJsonObject()
									.get("panelTypeConfig").getAsJsonObject()
									.get(panelType).getAsJsonObject();

		Component component = new Component();
		component.setBasePath(panelConfigJson.get("path").getAsString());
		component.setPanelType(panelType);
		component.setComponentPrefix(configJson.getComponentPrefix());
		component.setCompStyleExt(compConfigJson.get("styleExt").getAsString());
		
		if (layoutJson.has("label")) {
			component.setSectionName(layoutJson.get("label").getAsString());
		}
		if (layoutJson.has("htmlID")) {
			component.setSectionHtmlId(layoutJson.get("htmlID").getAsString());
		}
		
		String componentName = generateComponentName(request, panelType, panelConfigJson, layoutJson);
		component.setComponentName(componentName);

		if (panelConfigJson.has("generateLayout") 
				&& StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), 
							panelConfigJson.get("generateLayout").getAsString())) {
			
			JsonArray layoutArray = new JsonArray();
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("type", panelType);
			jsonObject.addProperty("htmlID", JsonUtils.parseToCamelCase(componentName));
			
			if (layoutJson.has("label")) {
				jsonObject.addProperty("label", layoutJson.get("label").getAsString());
			} else {
				jsonObject.addProperty("label", "");
			}
			
			layoutArray.add(jsonObject);
			component.setUiLayout(layoutArray);
		} else if (layoutJson.isJsonArray()) {
			component.setUiLayout(layoutJson.getAsJsonArray());
		} else {
			JsonArray layoutArray = new JsonArray();
			layoutArray.add(layoutJson);
			
			component.setUiLayout(layoutArray);
		}

		if (panelConfigJson.get("tsConfig") != null) {
			component.setTsConfig(panelConfigJson.get("tsConfig").getAsJsonObject());
			
			if (component.getTsConfig().get("createModel") != null) {
				component.setCreateModel(component.getTsConfig().get("createModel").getAsBoolean());
				component.setModelFolderName(component.getTsConfig().get("modelFolder").getAsString());
			}
		}
		
		if (parentComponent != null) {
			component.setParentComponent(parentComponent);
		}
		
		filterForNestedLayout(component, configJson, request);

		return component;
	}

	/**
	 * @param request
	 * @param panelType 
	 * @param panelConfigJson 
	 * @param layoutJson 
	 * @return
	 */
	protected String generateComponentName(CodeGenRequest request, String panelType, 
			JsonObject panelConfigJson, JsonObject layoutJson) {
		
		String componentNameTemplate = panelConfigJson.get("name").getAsString();
		
		String compNameTmp = JsonUtils.toComponentName(request.getComponentName());
		String htmlId = JsonUtils.toComponentName(layoutJson.get("htmlID").getAsString());
		
		String componentName = componentNameTemplate.replace("${componentName}", compNameTmp);
		componentName = componentName.replace("${htmlID}", htmlId);

		return componentName;
	}

	private String getPanelType(JsonElement layoutElement) {
		JsonObject layoutJson = layoutElement.getAsJsonObject();
		
		String panelType = "na";
		if (jsonKeyHasValue(layoutJson, "type", "section")) {
			if (layoutJson.has("panelType")) {
				panelType = layoutJson.get("panelType").getAsString();
			}
		} 

		return panelType;
	}

	/**
	 * @param layoutJson
	 * @return
	 */
	protected boolean jsonKeyHasValue(JsonObject layoutJson, String key, String value) {
		return layoutJson.has(key) && 
				StringUtils.equalsIgnoreCase(value, layoutJson.get(key).getAsString());
	}

	public List<Component> getComponentList(JsonDocument configJson, CodeGenRequest request) {
		List<Component> componentsList = new ArrayList<>();
		
		JsonArray uiLayout = null;
		if (hasLeftRightNavPanels()) {
			
			componentsList = getLeftRightNavComponents(configJson, request, "navOverlay");
			
		} else if (hasCollapsiblePanels()) {
			uiLayout = getCollapsiblePanelColumns();
			
			/*JsonObject singleJson = 
					configJson.getJson().get("componentConfig").getAsJsonObject().get("single").getAsJsonObject();
			
			Component component = new Component();
			component.setComponentName(request.getComponentName());
			component.setBasePath(singleJson.get("path").getAsString());
			component.setComponentPrefix(configJson.getComponentPrefix());
			
			
			component.setUiLayout(uiLayout);
			
			componentsList.add(component);*/
			
			componentsList = getLeftRightNavComponents(configJson, request, "single");
			componentsList.get(0).setUiLayout(uiLayout);

		} else if (hasTabbedPanels()) {
		
			componentsList = getLeftRightNavComponents(configJson, request, "tabs");
			Component mainTabComponent = componentsList.get(0);
			
			String tabPrefix = "";
			String tabSuffix = "";
			if (mainTabComponent.getTsConfig() != null) {
				if (mainTabComponent.getTsConfig().get("tabPrefix") != null) {
					tabPrefix = mainTabComponent.getTsConfig().get("tabPrefix").getAsString();
					if (StringUtils.isNotBlank(tabPrefix)) {
						tabPrefix = tabPrefix + "-";
					}
				}
				
				if (mainTabComponent.getTsConfig().get("tabSuffix") != null) {
					tabSuffix = mainTabComponent.getTsConfig().get("tabSuffix").getAsString();
					if (StringUtils.isNotBlank(tabSuffix)) {
						tabSuffix = "-" + tabSuffix;
					}
				}
			}
			
			componentsList = new ArrayList<Component>();
			Component component = null;
			uiLayout = getJsonArray("layout");
			if (uiLayout != null && uiLayout.size() == 1) {
				JsonArray childTabs = uiLayout.get(0).getAsJsonObject().get("columns").getAsJsonArray();
				
				if (childTabs != null && childTabs.size() > 0) {
					if (childTabs.get(0).isJsonArray()) {
						childTabs = childTabs.get(0).getAsJsonArray();
					}
					
					for (JsonElement tabElement : childTabs) {
						JsonObject tabObject = tabElement.getAsJsonObject();
						
						String compName = tabObject.get("htmlID").getAsString();
						compName = compName.replaceAll(" ", "-");
						
						component = new Component();
						component.setComponentName(tabPrefix + compName.toLowerCase() + tabSuffix);
						component.setSectionName(tabObject.get("label").getAsString());
						component.setBasePath(mainTabComponent.getBasePath());
						component.setPanelType(Constants.PANEL_TYPE_TAB);
						component.setComponentPrefix(configJson.getComponentPrefix());
						
						JsonArray tabArray = new JsonArray();
						tabArray.add(tabObject);
						component.setUiLayout(tabArray);
						
						componentsList.add(component);
					}
				}
			}
			
			componentsList.add(mainTabComponent);			
		} else {
			uiLayout = getJsonArray("layout");
			
			JsonObject navOverlayJson = 
					configJson.getJson().get("componentConfig").getAsJsonObject().get("single").getAsJsonObject();
			
			Component component = new Component();
			component.setBasePath(navOverlayJson.get("path").getAsString());
			component.setPanelType("main");
			component.setComponentName(request.getComponentName());
			component.setUiLayout(uiLayout);
			
			JsonArray components = navOverlayJson.get("components").getAsJsonArray();
			for (JsonElement jsonElement : components) {
				String componentName = jsonElement.getAsJsonObject().get("name").getAsString();
				componentName = componentName.replace("${componentName}", request.getComponentName());
				
				if (jsonElement.getAsJsonObject().get("tsConfig") != null) {
					component.setTsConfig(jsonElement.getAsJsonObject().get("tsConfig").getAsJsonObject());
				}
				
				if (component.getTsConfig().get("createModel") != null) {
					component.setCreateModel(component.getTsConfig().get("createModel").getAsBoolean());
					component.setModelFolderName(component.getTsConfig().get("modelFolder").getAsString());
				}
			}
			
			componentsList.add(component);
		}

		return componentsList;
	}

	/**
	 * @param configJson
	 * @param request
	 * @param componentsList
	 * @return 
	 */
	protected List<Component> getLeftRightNavComponents(JsonDocument configJson, CodeGenRequest request, String compConfig) {
		List<Component> componentsList = new ArrayList<>();

		JsonArray uiLayout;
		JsonObject navOverlayJson = 
				configJson.getJson().get("componentConfig").getAsJsonObject().get(compConfig).getAsJsonObject();
		
		Component component = null;
		
		JsonArray components = navOverlayJson.get("components").getAsJsonArray();

		Map<String,String> panelMap= new HashMap<>();
		JsonObject jsonObject = null;
		for (JsonElement jsonElement : components) {
			jsonObject = jsonElement.getAsJsonObject();
			String panelType = jsonObject.get("panelType").getAsString();
			String componentName = jsonObject.get("name").getAsString();
			componentName = componentName.replace("${componentName}", request.getComponentName());
			
			component = new Component();
			component.setBasePath(navOverlayJson.get("path").getAsString());
			component.setPanelType(panelType);
			component.setComponentName(componentName);
			if (jsonElement.getAsJsonObject().get("tsConfig") != null) {
				component.setTsConfig(jsonElement.getAsJsonObject().get("tsConfig").getAsJsonObject());
				
				if (component.getTsConfig().get("createModel") != null) {
					component.setCreateModel(component.getTsConfig().get("createModel").getAsBoolean());
					component.setModelFolderName(component.getTsConfig().get("modelFolder").getAsString());
				}
			}
			
			if (Constants.PANEL_TYPE_MAIN_LEFT_RIGHT.equalsIgnoreCase(panelType)
					|| Constants.PANEL_TYPE_MAIN_TAB.equalsIgnoreCase(panelType)) {
				uiLayout = new JsonArray();
				
				jsonObject = new JsonObject();
				jsonObject.addProperty("type", compConfig);
				jsonObject.addProperty("htmlID", JsonUtils.parseToCamelCase(componentName));
				
				for(Map.Entry<String, String> map:panelMap.entrySet()) {
					jsonObject.addProperty(map.getKey(), map.getValue());
				}
				uiLayout.add(jsonObject);
				
				component.setUiLayout(uiLayout);
			} else {
				component.setUiLayout(getPanelAsArrayByKeyValue(panelType, Boolean.TRUE.toString(), true));

				jsonObject.addProperty(panelType + "Nav", configJson.getComponentPrefix() + "-" + componentName);
				panelMap.put(panelType + "Nav", configJson.getComponentPrefix() + "-" + componentName);
			}
			
			componentsList.add(component);
		}
		
		return componentsList;
	}

	public JsonArray getCollapsiblePanelColumns() {
		JsonArray childPanels = null;
		
		JsonElement layout = json.get("layout");
		if (layout != null && layout.isJsonArray()) {
			JsonArray layoutArray = layout.getAsJsonArray();
			
			if (layoutArray.get(0) != null && layoutArray.get(0).isJsonObject()) {
				JsonObject mainPanel = layoutArray.get(0).getAsJsonObject();

					if (mainPanel.get("columns") != null) {
						childPanels = mainPanel.get("columns").getAsJsonArray();
						if (childPanels != null && childPanels.size() > 0 && childPanels.get(0).isJsonArray()) {
							childPanels = childPanels.get(0).getAsJsonArray();
						}
					}
			}
		}
		
		if (childPanels != null) {
			for (JsonElement childElement : childPanels) {
				JsonObject childObj = childElement.getAsJsonObject();
				childObj.addProperty("tabbedPanel", "false");
			}
		}
		
		return childPanels;
	}

	public boolean hasLeftRightNavPanels() {
		boolean hasLeftPanel = false;
		boolean hasRightPanel = false;
		
		if (getPanelByKeyValue("leftPanel", Boolean.TRUE.toString(), true) != null) {
			hasLeftPanel = true;
		}
		
		if (getPanelByKeyValue("rightPanel", Boolean.TRUE.toString(), true) != null) {
			hasRightPanel = true;
		}
		
		return hasLeftPanel && hasRightPanel;
	}
	
	public JsonArray getPanelAsArrayByKeyValue(String panelType, String value, boolean checkChildSections) {
		JsonObject obj = getPanelByKeyValue(panelType, value, checkChildSections);
		
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(obj);
		
		return jsonArray;
	}
	
	public JsonObject getPanelByKeyValue(String key, String value, boolean checkChildSections) {
		JsonObject retVal = null;
				
		JsonElement layout = json.get("layout");
		if (layout != null && layout.isJsonArray()) {
			JsonArray layoutArray = layout.getAsJsonArray();
			
			if (layoutArray.get(0) != null && layoutArray.get(0).isJsonObject()) {
				JsonObject mainPanel = layoutArray.get(0).getAsJsonObject();

				if (checkChildSections) {
					if (mainPanel.get("columns") != null) {
						JsonArray childPanels = mainPanel.get("columns").getAsJsonArray();
						if (childPanels != null && childPanels.size() > 0 && childPanels.get(0).isJsonArray()) {
							childPanels = childPanels.get(0).getAsJsonArray();
						}
						
						for (JsonElement jsonElement : childPanels) {
							JsonObject obj = jsonElement.getAsJsonObject();
							if (obj.get(key) != null 
									&& value.equalsIgnoreCase(obj.get(key).getAsString())) {
								retVal = obj;
							}
						}

					}
				} else {
					if (mainPanel.get(key) != null 
							&& value.equalsIgnoreCase(mainPanel.get(key).getAsString())) {
						retVal = mainPanel;
					}
				}
			}
		}
		
		return retVal;
	}

	public boolean hasCollapsiblePanels() {
		
		boolean hasTabbedPanel = false;
		JsonElement layout = json.get("layout");
		if (layout != null && layout.isJsonArray()) {
			JsonArray layoutArray = layout.getAsJsonArray();
			
			if (layoutArray.get(0) != null && layoutArray.get(0).isJsonObject()) {
				JsonObject mainPanel = layoutArray.get(0).getAsJsonObject();
				if (mainPanel.has("tabbedPanel")) {
					hasTabbedPanel = true;
				}
			}
		}
		
		if (hasTabbedPanel) {
			return !hasTabbedPanels();
		} else {
			return false;
		}
	}

	public boolean hasTabbedPanels() {
		boolean hasTabbedPanel = false;
		
		if (getPanelByKeyValue("tabbedPanel", Boolean.TRUE.toString(), false) != null) {
			hasTabbedPanel = true;
		}
		
		return hasTabbedPanel;
	}

	/**
	 * @return the json
	 */
	public JsonObject getJson() {
		return json;
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(JsonObject json) {
		this.json = json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (json != null) {
			return json.toString();
		}
		
		return "Null JSON object";
	}

	/**
	 * @return the layoutRepository
	 */
	public LayoutRepository getLayoutRepository() {
		return layoutRepository;
	}

	/**
	 * @param layoutRepository the layoutRepository to set
	 */
	public void setLayoutRepository(LayoutRepository layoutRepository) {
		this.layoutRepository = layoutRepository;
	}
}
