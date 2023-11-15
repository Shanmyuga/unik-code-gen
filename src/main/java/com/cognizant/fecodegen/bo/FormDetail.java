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
public class FormDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939493490554996192L;

	private List<FormElement> formElementList;
	
	private List<DataTable> dataTables;
	
	private Component component;

	private Map<String, ReactImport> importMap;
	
	public void addImport(String packageName, String importClasses, String fileName) {
		if  (importMap == null) {
			importMap = new HashMap<>();
		}
		
		String key = "files";
		if (StringUtils.isNotBlank(packageName)) {
			key = packageName;
		} 
		
		ReactImport reactImport = importMap.get(key);
		if (reactImport == null) {
			reactImport = new ReactImport(packageName, importClasses, fileName);
			importMap.put(key, reactImport);
		} else {
			reactImport.addFileName(fileName);
			reactImport.addImportClasses(importClasses);
		}
	}
	
	/**
	 * @return the formElementList
	 */
	public List<FormElement> getFormElementList() {
		if (formElementList == null) {
			formElementList = new ArrayList<>();
		}
		
		return formElementList;
	}

	/**
	 * @param formElementList the formElementList to set
	 */
	public void setFormElementList(List<FormElement> formElementList) {
		this.formElementList = formElementList;
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * @return the importMap
	 */
	public Map<String, ReactImport> getImportMap() {
		return importMap;
	}

	/**
	 * @param importMap the importMap to set
	 */
	public void setImportMap(Map<String, ReactImport> importMap) {
		this.importMap = importMap;
	}

	/**
	 * @return the dataTables
	 */
	public List<DataTable> getDataTables() {
		if (dataTables == null) {
			dataTables = new ArrayList<>();
		}
		
		return dataTables;
	}

	/**
	 * @param dataTables the dataTables to set
	 */
	public void setDataTables(List<DataTable> dataTables) {
		this.dataTables = dataTables;
	}
}
