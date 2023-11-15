/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.utils.Constants;

/**
 * @author 238209
 *
 */
public class ReactImport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5397086866289498795L;

	private String packageName;
	
	private List<String> importClassList;
	
	private List<String> fileNameList;

	public ReactImport(String packageName, String importClasses, String fileName) {
		this.packageName = packageName;
		
		addImportClasses(importClasses);
		
		addFileName(fileName);
	}
	
	public void addFileName(String fileName) {
		if (fileNameList == null) {
			fileNameList = new ArrayList<>();
		}
		
		if (StringUtils.isNotBlank(fileName)) {
			if (fileNameList.contains(fileName) == false) {
				fileNameList.add(fileName);
			}
		}
	}
	
	public void addImportClasses(String importClasses) {
		if (importClassList == null) {
			importClassList = new ArrayList<>();
		}
		
		if (StringUtils.isNotBlank(importClasses)) {
			String[] imports = StringUtils.split(importClasses, Constants.COMMA);
			for (String imp : imports) {
				imp = StringUtils.trim(imp);
				
				if (importClassList.contains(imp) == false) {
					importClassList.add(imp);
				}
			}
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
	 * @return the importClassList
	 */
	public List<String> getImportClassList() {
		return importClassList;
	}

	/**
	 * @param importClassList the importClassList to set
	 */
	public void setImportClassList(List<String> importClassList) {
		this.importClassList = importClassList;
	}

	/**
	 * @return the fileNameList
	 */
	public List<String> getFileNameList() {
		return fileNameList;
	}

	/**
	 * @param fileNameList the fileNameList to set
	 */
	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}
}
