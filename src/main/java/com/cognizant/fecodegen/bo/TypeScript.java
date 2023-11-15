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
public class TypeScript implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946701058214232529L;

	private List<TypeScriptImport> imports;

	private Map<String, String> components;

	private String className;
	
	private List<String> implementClasses;
	
	private List<TypeScriptMethod> methods;
	
	private List<TypeScriptDeclarations> declarations;
	
	private List<TypeScriptModel> models;
	
	private Map<String, Boolean> methodNames;
	private Map<String, Boolean> variableNames;
	
	public void postProcess() {
		if (methodNames == null) {
			methodNames = new HashMap<>();
		}
		
		if (methods != null) {
			for (TypeScriptMethod typeScriptMethod : methods) {
				methodNames.put(typeScriptMethod.getMethodName(), Boolean.TRUE);
			}
		}
		
		if (variableNames == null) {
			variableNames = new HashMap<>();
		}
		
		if (declarations != null) {
			for (TypeScriptDeclarations typeScriptDecl : declarations) {
				variableNames.put(typeScriptDecl.getVariableName(), Boolean.TRUE);
			}
		}
	}

	public boolean hasModel(String modelName) {
		boolean contains = false;

		if (models == null) {
			models = new ArrayList<>();
		}
		
		for (TypeScriptModel typeScripsModel : models) {
			if (typeScripsModel.getModelName().equalsIgnoreCase(modelName)) {
				contains = true;
			}
		}

		return contains;
	}

	
	public void addTypeScriptModel(TypeScriptModel tsModel) {
		if (models == null) {
			models = new ArrayList<>();
		}
		
		boolean contains = hasModel(tsModel.getModelName());
		if (!contains) {
			models.add(tsModel);
		}
	}
	
	public TypeScriptDeclarations getDeclarationByName(String variableName) {
		TypeScriptDeclarations tsDeclaration = null;
		if (declarations != null && declarations.isEmpty() == false) {
			for (TypeScriptDeclarations typeScriptDecl : declarations) {
				if (StringUtils.equalsIgnoreCase(variableName, typeScriptDecl.getVariableName())) {
					tsDeclaration = typeScriptDecl;
					break;
				}
			}
		}

		return tsDeclaration;
	}
	
	public void addTypeScriptDeclaration(TypeScriptDeclarations tsDeclaration) {
		if (declarations == null) {
			declarations = new ArrayList<>();
		}
		
		boolean contains = false;
		for (TypeScriptDeclarations typeScriptDeclarations : declarations) {
			if (tsDeclaration.getVariableName().equalsIgnoreCase(typeScriptDeclarations.getVariableName())) {
				contains = true;
			}
		}
		
		if (!contains) {
			declarations.add(tsDeclaration);
		}
	}

	public TypeScriptMethod getMethodByName(String methodName) {
		TypeScriptMethod tsMethod = null;
		if (methods != null && methods.isEmpty() == false) {
			for (TypeScriptMethod typeScriptMethod : methods) {
				if (StringUtils.equalsIgnoreCase(methodName, typeScriptMethod.getMethodName())) {
					tsMethod = typeScriptMethod;
					break;
				}
			}
		}

		return tsMethod;
	}
	
	public void addTypeScriptMethod(String methodName, String methodCode, String methodArgs) {
		TypeScriptMethod tsMethod = getMethodByName(methodName);
		
		if (tsMethod == null) {
			tsMethod = new TypeScriptMethod();
			tsMethod.setMethodName(methodName);
			addTypeScriptMethod(tsMethod);
		}
		
		tsMethod.setMethodContent(methodCode);
		tsMethod.setMethodArgs(methodArgs);
	}

	public void addTypeScriptMethod(TypeScriptMethod tsMethod) {
		if (methods == null) {
			methods = new ArrayList<>();
		}
		
		methods.add(tsMethod);
	}
	
	public void addImplementClassName(String className) {
		if (implementClasses == null) {
			implementClasses = new ArrayList<>();
		}
		
		implementClasses.add(className);
	}
	
	public void addComponentKey (String key, String value) {
		if (components == null) {
			components = new HashMap<>();
		}
		
		components.put(key, value);
	}
	
	public void addTypeScriptImport(String packageFull, String importObj) {
		TypeScriptImport imp = null;
		if (imports != null && imports.isEmpty() == false) {
			for (TypeScriptImport tsImport : imports) {
				if (StringUtils.equalsIgnoreCase(tsImport.getPackageNameFull(), packageFull)) {
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
		
		if (importObj.contains(",")) {
			String[] importObjArr = importObj.split(",");
			for (String importObjVal : importObjArr) {
				imp.addImportObject(StringUtils.trim(importObjVal));
			}
		} else {
			imp.addImportObject(importObj);
		}
	}
	
	public void addTypeScriptImport(TypeScriptImport tsImport) {
		if (imports == null) {
			imports = new ArrayList<>();
		}
		
		imports.add(tsImport);
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
	 * @return the components
	 */
	public Map<String, String> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(Map<String, String> components) {
		this.components = components;
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
	 * @return the implementClasses
	 */
	public List<String> getImplementClasses() {
		return implementClasses;
	}

	/**
	 * @param implementClasses the implementClasses to set
	 */
	public void setImplementClasses(List<String> implementClasses) {
		this.implementClasses = implementClasses;
	}

	/**
	 * @return the methods
	 */
	public List<TypeScriptMethod> getMethods() {
		return methods;
	}

	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<TypeScriptMethod> methods) {
		this.methods = methods;
	}

	/**
	 * @return the declarations
	 */
	public List<TypeScriptDeclarations> getDeclarations() {
		return declarations;
	}

	/**
	 * @param declarations the declarations to set
	 */
	public void setDeclarations(List<TypeScriptDeclarations> declarations) {
		this.declarations = declarations;
	}

	/**
	 * @return the models
	 */
	public List<TypeScriptModel> getModels() {
		return models;
	}

	/**
	 * @param models the models to set
	 */
	public void setModels(List<TypeScriptModel> models) {
		this.models = models;
	}

	/**
	 * @return the methodNames
	 */
	public Map<String, Boolean> getMethodNames() {
		return methodNames;
	}

	/**
	 * @param methodNames the methodNames to set
	 */
	public void setMethodNames(Map<String, Boolean> methodNames) {
		this.methodNames = methodNames;
	}

	/**
	 * @return the variableNames
	 */
	public Map<String, Boolean> getVariableNames() {
		return variableNames;
	}

	/**
	 * @param variableNames the variableNames to set
	 */
	public void setVariableNames(Map<String, Boolean> variableNames) {
		this.variableNames = variableNames;
	}
}
