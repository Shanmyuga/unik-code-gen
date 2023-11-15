package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.ArrayList;
import java.util.List;

public class RoutingModule {

	private List<String> imports;
	private List<String> routes = new ArrayList<>();
	private List<String> moduleImports;
	private List<String> moduleExports;
	private String routeName;
	private String moduleName;

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the routeName
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * @param routeName
	 *            the routeName to set
	 */
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	/**
	 * @return the imports
	 */
	public List<String> getImports() {
		return imports;
	}

	/**
	 * @param imports
	 *            the imports to set
	 */
	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	/**
	 * @return the routes
	 */
	public List<String> getRoutes() {
		return routes;
	}

	/**
	 * @param routes
	 *            the routes to set
	 */
	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}

	/**
	 * @return the moduleImports
	 */
	public List<String> getModuleImports() {
		return moduleImports;
	}

	/**
	 * @param moduleImports
	 *            the moduleImports to set
	 */
	public void setModuleImports(List<String> moduleImports) {
		this.moduleImports = moduleImports;
	}

	/**
	 * @return the moduleExports
	 */
	public List<String> getModuleExports() {
		return moduleExports;
	}

	/**
	 * @param moduleExports
	 *            the moduleExports to set
	 */
	public void setModuleExports(List<String> moduleExports) {
		this.moduleExports = moduleExports;
	}

}
