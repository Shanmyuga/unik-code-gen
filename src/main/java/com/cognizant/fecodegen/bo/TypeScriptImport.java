/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author 238209
 *
 */
public class TypeScriptImport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5216241321459699697L;

	private String packageName;
	
	private String packageNameFull;
	
	private List<String> importObjects;

	public void addImportObject(String importObject) {
		if (importObjects == null) {
			importObjects = new ArrayList<>();
		}
		
		if (importObjects.contains(importObject) == false) {
			importObjects.add(importObject);
		}
	}
	
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the importObjects
	 */
	public List<String> getImportObjects() {
		return importObjects;
	}

	/**
	 * @param importObjects the importObjects to set
	 */
	public void setImportObjects(List<String> importObjects) {
		this.importObjects = importObjects;
	}

	/**
	 * @return the packageNameFull
	 */
	public String getPackageNameFull() {
		return packageNameFull;
	}

	/**
	 * @param packageNameFull the packageNameFull to set
	 */
	public void setPackageNameFull(String packageNameFull) {
		this.packageNameFull = packageNameFull;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
