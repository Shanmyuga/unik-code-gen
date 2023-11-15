/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 238209
 *
 */
public class Module implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946701058214232529L;

	private List<TypeScriptImport> imports;

	private String className;

	private List<String> ngModuleImports;

	private List<String> ngModuleExports;

	private List<String> ngModuleDeclarations;

	private List<String> ngModuleSchemas;

	private List<Route> routes;

	private String isApp = "false";

	public void addNgModuleImport(String importName) {
		if (ngModuleImports == null) {
			ngModuleImports = new ArrayList<>();
		}

		if (StringUtils.contains(importName, "forRoot")) {
			boolean added = false;
			String impModuleName = StringUtils.trim(StringUtils.substringBefore(importName, "."));
			for (String modImports : ngModuleImports) {
				if (StringUtils.contains(modImports, impModuleName)) {
					added = true;
					break;
				}
			}
			
			if (added == false && importName.equalsIgnoreCase(className) == false) {
				ngModuleImports.add(importName);
			}
		} else if (!ngModuleImports.contains(importName) && importName.equalsIgnoreCase(className) == false) {
			ngModuleImports.add(importName);
		}
	}

	public void addNgModuleExport(String exportName) {
		if (ngModuleExports == null) {
			ngModuleExports = new ArrayList<>();
		}

		if (!ngModuleExports.contains(exportName)) {
			ngModuleExports.add(exportName);
		}
	}

	public void addNgModuleSchema(String schema) {
		if (ngModuleSchemas == null) {
			ngModuleSchemas = new ArrayList<>();
		}

		if (!ngModuleSchemas.contains(schema)) {
			ngModuleSchemas.add(schema);
		}
	}

	public void addNgModuleDeclaration(String declaration) {
		if (StringUtils.isNotBlank(declaration)) {
			if (ngModuleDeclarations == null) {
				ngModuleDeclarations = new ArrayList<>();
			}
			if (!ngModuleDeclarations.contains(declaration)) {
				ngModuleDeclarations.add(declaration);
			}
		}
	}

	public void addTypeScriptImport(String packageFull, String importObj) {
		if (StringUtils.isNotBlank(importObj) && importObj.equalsIgnoreCase(className) == false) {
			TypeScriptImport imp = null;
			if (imports != null && imports.isEmpty() == false) {
				for (TypeScriptImport tsImport : imports) {
					if (StringUtils.equalsIgnoreCase(tsImport.getPackageNameFull(), packageFull)
							|| tsImport.getImportObjects().contains(importObj)) {
						imp = tsImport;
						break;
					}
				}
			}

			if (imp == null) {
				imp = new TypeScriptImport();
				imp.setPackageNameFull(packageFull);

				if (packageFull.startsWith("@")) {
					imp.setPackageName(packageFull);
				} else if (packageFull.contains("/")) {
					imp.setPackageName(StringUtils.substringAfterLast(packageFull, "/"));
				} else {
					imp.setPackageName(packageFull);
				}

				addTypeScriptImport(imp);
			}

			imp.addImportObject(importObj);
		}
	}

	public void addRoutePath(String path, String componentName) {
		Route route = null;
		if (routes != null && routes.isEmpty() == false) {
			for (Route rte : routes) {
				if (StringUtils.equalsIgnoreCase(rte.getPath(), path)) {
					route = rte;
					break;
				}
			}
		}

		if (route == null) {
			route = new Route();
			route.setPath(path);
			route.setComponent(componentName);

			if (routes == null) {
				routes = new ArrayList<>();
			}

			routes.add(route);
		}
	}

	public void addTypeScriptImport(TypeScriptImport tsImport) {
		if (imports == null) {
			imports = new ArrayList<>();
		}

		imports.add(tsImport);
	}

	public Map<String, String> getNgImportsWithRoot() {
		Map<String, String> ngImportWithRoot = new HashMap<String, String>();
		
		for (String ngImp : ngModuleImports) {
			if (ngImp.contains(".")) {
				ngImportWithRoot.put(StringUtils.substringBefore(ngImp, "."), ngImp);
			}
		}
		
		return ngImportWithRoot;
	}
	
	public void setNgImportsWithRoot(Map<String, String> ngImportsWithRoot) {
		
	}
	
	/**
	 * @return the imports
	 */
	public List<TypeScriptImport> getImports() {
		return imports;
	}

	/**
	 * @param imports the imports to set
	 */
	public void setImports(List<TypeScriptImport> imports) {
		this.imports = imports;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the ngModuleImports
	 */
	public List<String> getNgModuleImports() {
		return ngModuleImports;
	}

	/**
	 * @param ngModuleImports the ngModuleImports to set
	 */
	public void setNgModuleImports(List<String> ngModuleImports) {
		this.ngModuleImports = ngModuleImports;
	}

	/**
	 * @return the ngModuleDeclarations
	 */
	public List<String> getNgModuleDeclarations() {
		return ngModuleDeclarations;
	}

	/**
	 * @param ngModuleDeclarations the ngModuleDeclarations to set
	 */
	public void setNgModuleDeclarations(List<String> ngModuleDeclarations) {
		this.ngModuleDeclarations = ngModuleDeclarations;
	}

	/**
	 * @return the ngModuleSchemas
	 */
	public List<String> getNgModuleSchemas() {
		return ngModuleSchemas;
	}

	/**
	 * @param ngModuleSchemas the ngModuleSchemas to set
	 */
	public void setNgModuleSchemas(List<String> ngModuleSchemas) {
		this.ngModuleSchemas = ngModuleSchemas;
	}

	/**
	 * @return the isApp
	 */
	public String getIsApp() {
		return isApp;
	}

	/**
	 * @param isApp the isApp to set
	 */
	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}

	/**
	 * @return the routes
	 */
	public List<Route> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	/**
	 * @return the ngModuleExports
	 */
	public List<String> getNgModuleExports() {
		return ngModuleExports;
	}

	/**
	 * @param ngModuleExports the ngModuleExports to set
	 */
	public void setNgModuleExports(List<String> ngModuleExports) {
		this.ngModuleExports = ngModuleExports;
	}

}
