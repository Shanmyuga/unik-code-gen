/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.model.Params;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class Component implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2722693471942434440L;

	private String sectionName;
	private String sectionHtmlId;

	private String reducerName;
	private String reducerFileName;
	private String reducerRelativePath;
	private String reducerType;
	
	private String componentName;
	private String componentFileName;
	private String componentRelativePath;
	
	private String containerName;
	private String containerFileName;
	private String containerRelativePath;

	private String serviceFileName;
	
	private String actionMethodName;
	private String actionFileName;
	private String actionName;
	private String actionRelativePath;
	private String actionType;
	private String actionConstant;
	
	private List<StateVariable> variables;
	
	private List<StateVariable> containerVariables;
	
	private String panelType;
	private String basePath;

	private JsonArray uiLayout;
	private JsonObject tsConfig;
	
	private boolean createModel;
	private String modelFolderName;
	
	private String componentPrefix;
	
	private Component parentComponent;
	
	private boolean primary;
	
	private String isTab;
	
	private TypeScript appTypeScript;
	private TypeScript compTypeScript;

	private Module appModule;
	private Module compModule;
	
	private String appModulePath;
	private String compModulePath;
	private String appTypeScriptPath;
	private String compTypeScriptPath;
	
	private String compStyleExt;
	
	private String componentSelector;
	
	private boolean reusableComponent;
	private List<Params> params;
	
	public List<StateVariable> getAllVariables() {
		List<StateVariable> allVariables = null;
		if (variables != null && variables.isEmpty() == false) {
			if (allVariables == null) {
				allVariables = new ArrayList<StateVariable>();
			}
			allVariables.addAll(variables);
		}
		
		if (containerVariables != null && containerVariables.isEmpty() == false) {
			if (allVariables == null) {
				allVariables = new ArrayList<StateVariable>();
			}
			allVariables.addAll(containerVariables);
		}
		return allVariables;
	}
	
	public void setAllVariables(List<StateVariable> allVariables) {
		
	}
	
	public StateVariable addVariables (String key, String value, String type) {
		if (variables == null) {
			variables = new ArrayList<StateVariable>();
		}
		
		StateVariable var = null;
		for (StateVariable stateVariable : variables) {
			if (StringUtils.equalsIgnoreCase(key, stateVariable.getKey())) {
				var = stateVariable;
				break;
			}
		}
		
		if (var == null) {
			var = new StateVariable(key, value);
			variables.add(var);
		}
		
		setType(var, type);
		
		return var;
	}
	
	private void setType(StateVariable var, String type) {
		if (StringUtils.equalsIgnoreCase(type, "textbox")
				|| StringUtils.equalsIgnoreCase(type, "textarea")
				|| StringUtils.equalsIgnoreCase(type, "checkbox")
				|| StringUtils.equalsIgnoreCase(type, "radiobutton")
				|| StringUtils.equalsIgnoreCase(type, "datepicker")) {
			var.setHtmlTag("input");
		}
		
		if (StringUtils.equalsIgnoreCase(type, "button")
				|| StringUtils.equalsIgnoreCase(type, "dropdownbutton")) {
			var.setHtmlTag("button");
		}
		
		if (StringUtils.equalsIgnoreCase(type, "dropdown")) {
			var.setHtmlTag("select");
		}
	}

	public StateVariable addContainerVariables (String key, String value) {
		if (containerVariables == null) {
			containerVariables = new ArrayList<StateVariable>();
		}
		
		StateVariable var = null;
		for (StateVariable stateVariable : containerVariables) {
			if (StringUtils.equalsIgnoreCase(key, stateVariable.getKey())) {
				var = stateVariable;
				break;
			}
		}
		
		if (var == null) {
			var = new StateVariable(key, value);
			containerVariables.add(var);
		}
				
		return var;
	}
	
	public List<StateVariable> getValidationVariables() {
		List<StateVariable> validationVar = null;
		if (variables != null) {
			for (StateVariable stateVariable : variables) {
				if (StringUtils.equalsIgnoreCase("true", stateVariable.getMandatory())
						|| StringUtils.isNotBlank(stateVariable.getMinLength())
						|| StringUtils.isNotBlank(stateVariable.getMaxLength())
						|| StringUtils.equalsIgnoreCase("true", stateVariable.getEmail())
						|| StringUtils.isNotBlank(stateVariable.getPattern())) {
					if (validationVar == null) {
						validationVar = new ArrayList<>();
					}
					
					validationVar.add(stateVariable);
				}
			}
		}
		
		return validationVar;
	}
	
	public void setValidationVariables(List<StateVariable> validationVar) {
		
	}
	
	public void calculateRelativePath(String currentPath, String targetPath) {
		
		currentPath = JsonUtils.standardizePath(currentPath);
		targetPath = JsonUtils.standardizePath(targetPath);
		
		String contfileName = targetPath + "/" + containerFileName;
		String redfileName = targetPath + "/" + reducerFileName;
		String actionfileName = targetPath + "/" + actionFileName;

		containerRelativePath = JsonUtils.getRelativePath(currentPath, contfileName);
		reducerRelativePath = JsonUtils.getRelativePath(currentPath, redfileName);
		actionRelativePath = JsonUtils.getRelativePath(currentPath, actionfileName);
	}
	
	public String getComponentFolder() {
		return StringUtils.substringBeforeLast(componentFileName, Constants.FORWARD_SLASH);
	}
	
	public void setComponentFolder(String componentFolder) {

	}
	
	public String getReducerFolder() {
		return StringUtils.substringBeforeLast(reducerFileName, Constants.FORWARD_SLASH);
	}

	public void setReducerFolder(String reducerFolder) {

	}

	public String getActionFolder() {
		return StringUtils.substringBeforeLast(actionFileName, Constants.FORWARD_SLASH);
	}

	public void setActionFolder(String actionFolder) {

	}
	
	
	


	/**
	 * @return the reducerType
	 */
	public String getReducerType() {
		return reducerType;
	}

	/**
	 * @param reducerType the reducerType to set
	 */
	public void setReducerType(String reducerType) {
		this.reducerType = reducerType;
	}

	/**
	 * @return the actionRelativePath
	 */
	public String getActionRelativePath() {
		return actionRelativePath;
	}

	/**
	 * @param actionRelativePath the actionRelativePath to set
	 */
	public void setActionRelativePath(String actionRelativePath) {
		this.actionRelativePath = actionRelativePath;
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the actionConstant
	 */
	public String getActionConstant() {
		return actionConstant;
	}

	/**
	 * @param actionConstant the actionConstant to set
	 */
	public void setActionConstant(String actionConstant) {
		this.actionConstant = actionConstant;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}
	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	/**
	 * @return the reducerName
	 */
	public String getReducerName() {
		return reducerName;
	}
	/**
	 * @param reducerName the reducerName to set
	 */
	public void setReducerName(String reducerName) {
		this.reducerName = reducerName;
	}
	/**
	 * @return the reducerFileName
	 */
	public String getReducerFileName() {
		return reducerFileName;
	}
	/**
	 * @param reducerFileName the reducerFileName to set
	 */
	public void setReducerFileName(String reducerFileName) {
		this.reducerFileName = reducerFileName;
	}
	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}
	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	/**
	 * @return the componentFileName
	 */
	public String getComponentFileName() {
		return componentFileName;
	}
	/**
	 * @param componentFileName the componentFileName to set
	 */
	public void setComponentFileName(String componentFileName) {
		this.componentFileName = componentFileName;
	}
	/**
	 * @return the containerName
	 */
	public String getContainerName() {
		return containerName;
	}
	/**
	 * @param containerName the containerName to set
	 */
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	/**
	 * @return the containerFileName
	 */
	public String getContainerFileName() {
		return containerFileName;
	}
	/**
	 * @param containerFileName the containerFileName to set
	 */
	public void setContainerFileName(String containerFileName) {
		this.containerFileName = containerFileName;
	}
	/**
	 * @return the serviceFileName
	 */
	public String getServiceFileName() {
		return serviceFileName;
	}
	/**
	 * @param serviceFileName the serviceFileName to set
	 */
	public void setServiceFileName(String serviceFileName) {
		this.serviceFileName = serviceFileName;
	}
	/**
	 * @return the actionMethodName
	 */
	public String getActionMethodName() {
		return actionMethodName;
	}
	/**
	 * @param actionMethodName the actionMethodName to set
	 */
	public void setActionMethodName(String actionMethodName) {
		this.actionMethodName = actionMethodName;
	}
	/**
	 * @return the actionFileName
	 */
	public String getActionFileName() {
		return actionFileName;
	}
	/**
	 * @param actionFileName the actionFileName to set
	 */
	public void setActionFileName(String actionFileName) {
		this.actionFileName = actionFileName;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the variables
	 */
	public List<StateVariable> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(List<StateVariable> variables) {
		this.variables = variables;
	}

	/**
	 * @return the panelType
	 */
	public String getPanelType() {
		return panelType;
	}

	/**
	 * @param panelType the panelType to set
	 */
	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}

	/**
	 * @return the uiLayout
	 */
	public JsonArray getUiLayout() {
		return uiLayout;
	}

	/**
	 * @param uiLayout the uiLayout to set
	 */
	public void setUiLayout(JsonArray uiLayout) {
		this.uiLayout = uiLayout;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * @return the tsConfig
	 */
	public JsonObject getTsConfig() {
		return tsConfig;
	}

	/**
	 * @param tsConfig the tsConfig to set
	 */
	public void setTsConfig(JsonObject tsConfig) {
		this.tsConfig = tsConfig;
	}

	/**
	 * @return the createModel
	 */
	public boolean isCreateModel() {
		return createModel;
	}

	/**
	 * @param createModel the createModel to set
	 */
	public void setCreateModel(boolean createModel) {
		this.createModel = createModel;
	}

	/**
	 * @return the modelFolderName
	 */
	public String getModelFolderName() {
		return modelFolderName;
	}

	/**
	 * @param modelFolderName the modelFolderName to set
	 */
	public void setModelFolderName(String modelFolderName) {
		this.modelFolderName = modelFolderName;
	}

	/**
	 * @return the componentPrefix
	 */
	public String getComponentPrefix() {
		return componentPrefix;
	}

	/**
	 * @param componentPrefix the componentPrefix to set
	 */
	public void setComponentPrefix(String componentPrefix) {
		this.componentPrefix = componentPrefix;
	}

	/**
	 * @return the parentComponent
	 */
	public Component getParentComponent() {
		return parentComponent;
	}

	/**
	 * @param parentComponent the parentComponent to set
	 */
	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}

	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return the containerVariables
	 */
	public List<StateVariable> getContainerVariables() {
		return containerVariables;
	}

	/**
	 * @param containerVariables the containerVariables to set
	 */
	public void setContainerVariables(List<StateVariable> containerVariables) {
		this.containerVariables = containerVariables;
	}

	/**
	 * @return the isTab
	 */
	public String getIsTab() {
		return isTab;
	}

	/**
	 * @param isTab the isTab to set
	 */
	public void setIsTab(String isTab) {
		this.isTab = isTab;
	}

	/**
	 * @return the componentRelativePath
	 */
	public String getComponentRelativePath() {
		return componentRelativePath;
	}

	/**
	 * @param componentRelativePath the componentRelativePath to set
	 */
	public void setComponentRelativePath(String componentRelativePath) {
		this.componentRelativePath = componentRelativePath;
	}

	/**
	 * @return the containerRelativePath
	 */
	public String getContainerRelativePath() {
		return containerRelativePath;
	}

	/**
	 * @param containerRelativePath the containerRelativePath to set
	 */
	public void setContainerRelativePath(String containerRelativePath) {
		this.containerRelativePath = containerRelativePath;
	}

	/**
	 * @return the reducerRelativePath
	 */
	public String getReducerRelativePath() {
		return reducerRelativePath;
	}

	/**
	 * @param reducerRelativePath the reducerRelativePath to set
	 */
	public void setReducerRelativePath(String reducerRelativePath) {
		this.reducerRelativePath = reducerRelativePath;
	}

	/**
	 * @return the sectionHtmlId
	 */
	public String getSectionHtmlId() {
		return sectionHtmlId;
	}

	/**
	 * @param sectionHtmlId the sectionHtmlId to set
	 */
	public void setSectionHtmlId(String sectionHtmlId) {
		this.sectionHtmlId = sectionHtmlId;
	}

	/**
	 * @return the compTypeScript
	 */
	public TypeScript getCompTypeScript() {
		return compTypeScript;
	}

	/**
	 * @param compTypeScript the compTypeScript to set
	 */
	public void setCompTypeScript(TypeScript compTypeScript) {
		this.compTypeScript = compTypeScript;
	}

	/**
	 * @return the appModule
	 */
	public Module getAppModule() {
		return appModule;
	}

	/**
	 * @param appModule the appModule to set
	 */
	public void setAppModule(Module appModule) {
		this.appModule = appModule;
	}

	/**
	 * @return the compModule
	 */
	public Module getCompModule() {
		return compModule;
	}

	/**
	 * @param compModule the compModule to set
	 */
	public void setCompModule(Module compModule) {
		this.compModule = compModule;
	}

	/**
	 * @return the appModulePath
	 */
	public String getAppModulePath() {
		return appModulePath;
	}

	/**
	 * @param appModulePath the appModulePath to set
	 */
	public void setAppModulePath(String appModulePath) {
		this.appModulePath = appModulePath;
	}

	/**
	 * @return the compModulePath
	 */
	public String getCompModulePath() {
		return compModulePath;
	}

	/**
	 * @param compModulePath the compModulePath to set
	 */
	public void setCompModulePath(String compModulePath) {
		this.compModulePath = compModulePath;
	}

	/**
	 * @return the appTypeScript
	 */
	public TypeScript getAppTypeScript() {
		return appTypeScript;
	}

	/**
	 * @param appTypeScript the appTypeScript to set
	 */
	public void setAppTypeScript(TypeScript appTypeScript) {
		this.appTypeScript = appTypeScript;
	}

	/**
	 * @return the appTypeScriptPath
	 */
	public String getAppTypeScriptPath() {
		return appTypeScriptPath;
	}

	/**
	 * @param appTypeScriptPath the appTypeScriptPath to set
	 */
	public void setAppTypeScriptPath(String appTypeScriptPath) {
		this.appTypeScriptPath = appTypeScriptPath;
	}

	/**
	 * @return the compTypeScriptPath
	 */
	public String getCompTypeScriptPath() {
		return compTypeScriptPath;
	}

	/**
	 * @param compTypeScriptPath the compTypeScriptPath to set
	 */
	public void setCompTypeScriptPath(String compTypeScriptPath) {
		this.compTypeScriptPath = compTypeScriptPath;
	}

	/**
	 * @return the compStyleExt
	 */
	public String getCompStyleExt() {
		return compStyleExt;
	}

	/**
	 * @param compStyleExt the compStyleExt to set
	 */
	public void setCompStyleExt(String compStyleExt) {
		this.compStyleExt = compStyleExt;
	}

	/**
	 * @return the componentSelector
	 */
	public String getComponentSelector() {
		return componentSelector;
	}

	/**
	 * @param componentSelector the componentSelector to set
	 */
	public void setComponentSelector(String componentSelector) {
		this.componentSelector = componentSelector;
	}

	/**
	 * @return the reusableComponent
	 */
	public boolean isReusableComponent() {
		return reusableComponent;
	}

	/**
	 * @param reusableComponent the reusableComponent to set
	 */
	public void setReusableComponent(boolean reusableComponent) {
		this.reusableComponent = reusableComponent;
	}

	/**
	 * @return the params
	 */
	public List<Params> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<Params> params) {
		this.params = params;
	}
}
