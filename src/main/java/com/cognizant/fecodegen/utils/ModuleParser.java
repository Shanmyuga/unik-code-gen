/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.io.Serializable;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.bo.Module;
import com.cognizant.fecodegen.bo.TypeScriptImport;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class ModuleParser extends Parser implements Serializable {

	private Module modObject;

	public ModuleParser(String fileName) throws CodeGenException {
		super(fileName);

		if (modObject == null) {
			modObject = new Module();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6701531450721929575L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String typeScriptFile =
		// "D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\angular-demo\\src\\app\\pharmacy\\client\\client-search-test1-left-nav\\client-search-test1-left-nav.component.ts";
		String typeScriptFile = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\angular-demo\\src\\app\\app.module.ts";

		try {
			ModuleParser parser = new ModuleParser(typeScriptFile);
			parser.parse();

			Module module = parser.getModObject();

			System.out.println(module);
		} catch (CodeGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void postProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseContent() {
		parseTsImports();

		parseRoutes();
		
		parseNgModExports();

		parseNgModImports();

		parseNgModDeclarations();

		parseNgModSchemas();

		parseClassContent();
	}

	/**
	 * 
	 */
	protected void parseNgModImports() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.MODULE_IMPORT_PATTERN.matcher(getFileContent());
		
		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group(1);

				Matcher forRootMatcher = RegexPatterns.MODULE_FORROOT_PATTERN.matcher(matchedLine);
				String matchedRoot = null;
				if (forRootMatcher.find()) {
					matchedRoot = forRootMatcher.group();
				}
				if (StringUtils.isNotBlank(matchedRoot)) {
					String newMatchedLine = StringUtils.substring(matchedLine, 0, forRootMatcher.start());
					String restOfLine = StringUtils.trim(StringUtils.substring(matchedLine, forRootMatcher.end() + 1));
					if (StringUtils.contains(restOfLine, ",")) {
						newMatchedLine = newMatchedLine + StringUtils.substringAfter(restOfLine, ",");
					} else {
						newMatchedLine = newMatchedLine + restOfLine;
					}
					
					matchedLine = newMatchedLine;
				}
				
				String[] compArray = matchedLine.split(",");
				for (String comp : compArray) {
					if (StringUtils.isNotBlank(comp)) {
						modObject.addNgModuleImport(comp.trim());
					}
				}
				
				if (StringUtils.isNotBlank(matchedRoot)) {
					modObject.addNgModuleImport(matchedRoot);
				}

				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		System.out.println(modObject.getNgModuleImports());
	}
	
	/**
	 * 
	 */
	protected void parseNgModExports() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.MODULE_EXPORT_PATTERN.matcher(getFileContent());

		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group(1);

				String[] compArray = matchedLine.split(",");
				for (String comp : compArray) {

					modObject.addNgModuleExport(comp.trim());
				}

				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		System.out.println(modObject.getNgModuleExports());
	}

	/**
	 * 
	 */
	protected void parseRoutes() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.MODULE_ROUTE_PATTERN.matcher(getFileContent());

		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group(1);

				Matcher matcher = RegexPatterns.MODULE_ROUTE_ELEMENT_PATTERN.matcher(matchedLine);

				while (matcher.find()) {
					String match = matcher.group();
					if (StringUtils.isNotEmpty(match)) {

						String[] pathArr = match.split(",");
						String path = StringUtils.substringAfter(pathArr[0], "path:");
						String component = StringUtils.substringBetween(pathArr[1], "component:", "}");

						modObject.addRoutePath(path.trim(), component.trim());
					}
				}

				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		System.out.println(modObject.getRoutes());
	}

	/**
	 * 
	 */
	protected void parseNgModDeclarations() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.MODULE_DECLARATIONS_PATTERN.matcher(getFileContent());

		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group(1);

				String[] compArray = matchedLine.split(",");
				for (String comp : compArray) {

					modObject.addNgModuleDeclaration(comp.trim());
				}

				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		System.out.println(modObject.getNgModuleDeclarations());
	}

	/**
	 * 
	 */
	protected void parseNgModSchemas() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.MODULE_SCHEMA_PATTERN.matcher(getFileContent());

		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group(1);

				String[] compArray = matchedLine.split(",");
				for (String comp : compArray) {

					modObject.addNgModuleSchema(comp.trim());
				}

				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		System.out.println(modObject.getNgModuleSchemas());
	}

	/**
	 * 
	 */
	protected void parseTsImports() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.TS_IMPORT_PATTERN.matcher(getFileContent());

		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group();
				matchedLine = matchedLine.replaceAll("\\n", "");

				// Package Name
				String packageName = StringUtils.substringBetween(matchedLine, "from", ";");
				packageName = StringUtils.trim(packageName.replaceAll("'", ""));

				TypeScriptImport tsImport = new TypeScriptImport();
				tsImport.setPackageNameFull(packageName);
				if (packageName.startsWith("@")) {
					tsImport.setPackageName(packageName);
				} else if (packageName.contains("/")) {
					tsImport.setPackageName(StringUtils.substringAfterLast(packageName, "/"));
				} else {
					tsImport.setPackageName(packageName);
				}

				// Import Objects
				String objectsList = StringUtils.substringBetween(matchedLine, "{", "}");
				String[] objectsArray = objectsList.split(",");
				if (objectsArray != null) {
					for (String obj : objectsArray) {
						modObject.addTypeScriptImport(packageName, StringUtils.trim(obj));
					}
				}

				currDocPosition = importMatcher.end();
				System.out.println(tsImport);
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}
	}

	/**
	 * 
	 */
	protected void parseClassContent() {
		currDocPosition = 0;
		String matchedLine = null;

		Matcher importMatcher = RegexPatterns.TS_CLASSNAME_PATTERN.matcher(getFileContent());

		if (importMatcher.find(currDocPosition)) {
			matchedLine = importMatcher.group();

			String className = StringUtils.substringBetween(matchedLine, "class ", " ");
			modObject.setClassName(StringUtils.trim(className));

			currDocPosition = importMatcher.end();
		}
	}

	@Override
	protected void filter() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the modObject
	 */
	public Module getModObject() {
		return modObject;
	}

	/**
	 * @param modObject the modObject to set
	 */
	public void setModObject(Module modObject) {
		this.modObject = modObject;
	}

}
