/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.StringUtils;

import com.cognizant.fecodegen.model.Params;
import com.cognizant.fecodegen.utils.Constants;

/**
 * @author 238209
 *
 */
public class CodeGenRequest implements Serializable {

	/**
	 * Serial Version Number
	 */
	private static final long serialVersionUID = 6889270458578142486L;

	private String uiTechnology;

	private String appName;

	private String uiName;

	private String uiLayout;

	private String codeGenPath;

	private String configName;

	private String springBootConfigName;

	private String genLoginScreen;

	private String projectName;

	private String moduleName;

	private String componentName;

	private String moduleFlag;

	private String css;

	private String userDefinedPath;

	private String searchModule;
	
	private String userId;
	
	private String reusableComponent;
	
	private List<Params> params;
	
	public String getProjectPath() {
		return codeGenPath + Constants.FORWARD_SLASH + projectName;
	}
	
	public String getProjectAppBasePath() {
		return getProjectPath() + Constants.BASE_PATH;
	}
	
	public String getModulePath() {
		//if (StringUtils.isEmpty(userDefinedPath)) {
			return getProjectAppBasePath() + Constants.FORWARD_SLASH + moduleName;
		/*} else {
			return getProjectAppBasePath() + Constants.FORWARD_SLASH + userDefinedPath;
		}*/
	}
	
	public String getComponentPath() {
		if (StringUtils.isEmpty(userDefinedPath)) {
			return getModulePath() + Constants.FORWARD_SLASH + componentName;
		} else {
			return getProjectAppBasePath() + Constants.FORWARD_SLASH + userDefinedPath + Constants.FORWARD_SLASH
					+ componentName;
		}
		// return getModulePath() + Constants.FORWARD_SLASH + componentName;
	}

	/**
	 * @return the searchModule
	 */
	public String getSearchModule() {
		return searchModule;
	}

	/**
	 * @param searchModule the searchModule to set
	 */
	public void setSearchModule(String searchModule) {
		this.searchModule = searchModule;
	}

	/**
	 * @return the css
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css the css to set
	 */
	public void setCss(String css) {
		this.css = css;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * @return the moduleFlag
	 */
	public String getModuleFlag() {
		return moduleFlag;
	}

	/**
	 * @param moduleFlag the moduleFlag to set
	 */
	public void setModuleFlag(String moduleFlag) {
		this.moduleFlag = moduleFlag;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the uiTechnology
	 */
	public String getUiTechnology() {
		return uiTechnology;
	}

	/**
	 * @param uiTechnology the uiTechnology to set
	 */
	public void setUiTechnology(String uiTechnology) {
		this.uiTechnology = uiTechnology;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the uiLayout
	 */
	public String getUiLayout() {
		return uiLayout;
	}

	/**
	 * @param uiLayout the uiLayout to set
	 */
	public void setUiLayout(String uiLayout) {
		this.uiLayout = uiLayout;
	}

	/**
	 * @return the codeGenPath
	 */
	public String getCodeGenPath() {
		return codeGenPath;
	}

	/**
	 * @param codeGenPath the codeGenPath to set
	 */
	public void setCodeGenPath(String codeGenPath) {
		this.codeGenPath = codeGenPath;
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * @return the springBootConfigName
	 */
	public String getSpringBootConfigName() {
		return springBootConfigName;
	}

	/**
	 * @param springBootConfigName the springBootConfigName to set
	 */
	public void setSpringBootConfigName(String springBootConfigName) {
		this.springBootConfigName = springBootConfigName;
	}

	/**
	 * @return the genLoginScreen
	 */
	public String getGenLoginScreen() {
		return genLoginScreen;
	}

	/**
	 * @param genLoginScreen the genLoginScreen to set
	 */
	public void setGenLoginScreen(String genLoginScreen) {
		this.genLoginScreen = genLoginScreen;
	}

	/**
	 * @return the uiName
	 */
	public String getUiName() {
		return uiName;
	}

	/**
	 * @param uiName the uiName to set
	 */
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public String getUserDefinedPath() {
		return userDefinedPath;
	}

	public void setUserDefinedPath(String userDefinedPath) {
		this.userDefinedPath = userDefinedPath;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the reusableComponent
	 */
	public String getReusableComponent() {
		return reusableComponent;
	}

	/**
	 * @param reusableComponent the reusableComponent to set
	 */
	public void setReusableComponent(String reusableComponent) {
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
