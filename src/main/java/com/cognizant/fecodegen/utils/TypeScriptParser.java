/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.TypeScript;
import com.cognizant.fecodegen.bo.TypeScriptDeclarations;
import com.cognizant.fecodegen.bo.TypeScriptImport;
import com.cognizant.fecodegen.bo.TypeScriptMethod;
import com.cognizant.fecodegen.bo.TypeScriptModel;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class TypeScriptParser extends Parser implements Serializable {

	private static Logger LOGGER = Logger.getLogger(TypeScriptParser.class);
	
	private TypeScript tsObject;
	
	public TypeScriptParser(String fileName) throws CodeGenException {
		super(fileName);
		
		if (tsObject == null) {
			tsObject = new TypeScript();
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
		//String typeScriptFile = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\angular-demo\\src\\app\\pharmacy\\client\\client-search-test1-left-nav\\client-search-test1-left-nav.component.ts";
		String typeScriptFile = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\claim-demo\\src\\app\\appcommon\\headerlabelsection\\headerlabelsection.component.ts";
		
		try {
			TypeScriptParser parser = new TypeScriptParser(typeScriptFile);
			parser.parse();
		} catch (CodeGenException e) {
			e.printStackTrace();
		}
		
		String  TS_DECLARATION_REGEX = "[\\@a-zA-Z0-9\\(\\) \\[\\{=]+: [.\\s\\S]*?;";
		Pattern TS_DECLARATION_PATTERN = Pattern.compile(TS_DECLARATION_REGEX);
		
		String test = "@Input() phoneLabel1 : string;\r\n" + 
				"@Input() phoneTypeLabel : string;\r\n" + 
				"@Input() phoneAreaLabel : string;\r\n" + 
				"@Input() phoneNumber1 : string;\r\n" + 
				"@Input() phoneNumber2 : string;\r\n" + 
				"@Input() phoneExtension : string;\r\n" + 
				"\r\n" + 
				"   phoneLabel: string; 	\r\n" + 
				"   headerLabelSection: FormGroup; 	";
		
		Matcher declarationMatcher = TS_DECLARATION_PATTERN.matcher(test);
		while (declarationMatcher.find()) {
			String declaration = StringUtils.trim(declarationMatcher.group());
			System.out.println(declaration);
		}
		
	}

	@Override
	protected void postProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parseContent() {
		parseTsImports();
		
		parseTsComponent();
		
		parseClassContent();
		
		parseMethodContent();
		
		parseDeclaration();
		
		parseTypes();
	}
	
	/**
	 * 
	 */
	protected void parseTypes() {
		currDocPosition = 0;

		Matcher importMatcher = RegexPatterns.TS_TYPES_PATTERN.matcher(getFileContent());
		while (currDocPosition < fileSize) {
			if (importMatcher.find()) {
				String modelClass = importMatcher.group();

				String modelName = StringUtils.trim(StringUtils.substringBetween(modelClass, "class", "{"));

				String modelContent = StringUtils.trim(StringUtils.substringBetween(modelClass, "{", "}"));

				TypeScriptModel tsModel = new TypeScriptModel();
				tsModel.setModelName(modelName);
				
				String[] modelContentArray = modelContent.trim().split("");
				for (String modelCont : modelContentArray) {
					String[] keyValArray = modelCont.split(":");
					
					if (keyValArray.length == 2) {
						tsModel.addAttribute(StringUtils.trim(keyValArray[0]), StringUtils.trim(keyValArray[1]));
					}
				}
				
				tsObject.addTypeScriptModel(tsModel);
				currDocPosition = importMatcher.end();
			} else {
				break;
			}
		}
		
		LOGGER.info(tsObject.getModels());
	}
	
	/**
	 * 
	 */
	protected void parseDeclaration() {
		String methodContent = StringUtils.trim(StringUtils.substring(getFileContent(), currDocPosition - 1));

		Matcher importMatcher = RegexPatterns.TS_METHODNAME_PATTERN.matcher(methodContent);
		if (importMatcher.find()) {
			int startPos = importMatcher.start();
			
			String declarations = StringUtils.trim(StringUtils.substring(methodContent, 1, startPos));
			int declarationsSize = declarations.length();
			int declarationsStart = 0;
			
			Matcher declarationMatcher = RegexPatterns.TS_DECLARATION_PATTERN.matcher(declarations);
			while (declarationsStart < declarationsSize) {
				if (declarationMatcher.find(declarationsStart)) {
					TypeScriptDeclarations tsDecl = new TypeScriptDeclarations();
					
					String declaration = StringUtils.trim(declarationMatcher.group());
					
					if (declaration.startsWith("@")) {
						tsDecl.setAnnotation(StringUtils.trim(StringUtils.substringBetween(declaration, "@", "(")));
						
						String variableSeparator = "=";
						if (StringUtils.contains(declaration, ":")) {
							variableSeparator = ":";
						}
						tsDecl.setVariableName(StringUtils.trim(StringUtils.substringBetween(declaration, ")", variableSeparator)));
						
						String dataTypeStart = "new";
						String dataTypeEnd = "(";
						if (StringUtils.contains(declaration, "new") == false) {
							dataTypeStart = ":";
							dataTypeEnd = ";";
						}
						tsDecl.setDataType(StringUtils.trim(StringUtils.substringBetween(declaration, dataTypeStart, dataTypeEnd)));
					} else if (hasDataType(declaration)) {
						tsDecl.setVariableName(StringUtils.trim(StringUtils.substringBefore(declaration, ":")));
						
						String tmp = StringUtils.trim(StringUtils.substringAfter(declaration, ":"));
						tsDecl.setDataType(StringUtils.trim(StringUtils.substringBefore(tmp, "=")));
						tsDecl.setDefaultValue(StringUtils.trim(StringUtils.substringAfter(tmp, "=")));
					} else {
						String declTmp = declaration.replaceAll("var ", "");
						declTmp = declTmp.replaceAll("let ", "");
						declTmp = declTmp.replaceAll("const ", "");
						tsDecl.setVariableName(StringUtils.trim(StringUtils.substringBefore(declTmp, "=")));
						tsDecl.setDefaultValue(StringUtils.trim(StringUtils.substringAfter(declTmp, "=")));
					}
					
					if (tsDecl.getDataType() != null) {
						tsDecl.setDataType(tsDecl.getDataType().replaceAll(";", ""));
					}
					
					if (tsDecl.getDefaultValue() != null) {
						tsDecl.setDefaultValue(tsDecl.getDefaultValue().replaceAll(";", ""));
					}
					
					tsObject.addTypeScriptDeclaration(tsDecl);
					declarationsStart = declarationMatcher.end();
				} else {
					break;
				}
			}
		}

		LOGGER.info(tsObject.getDeclarations());
	}
	
	private boolean hasDataType(String declaration) {
		int colonIndex = declaration.indexOf(":");
		int equalToIndex = declaration.indexOf("=");
		
		boolean hasDataType = false;
		if (colonIndex > 0) {
			if (equalToIndex > 0 && colonIndex < equalToIndex) {
				hasDataType = true;
			}
			
			if (equalToIndex == -1) {
				hasDataType = true;
			}
		}
		
		return hasDataType;
	}

	/**
	 * 
	 */
	protected void parseMethodContent() {
		String matchedLine = null;

		String methodContent = StringUtils.trim(StringUtils.substring(getFileContent(), currDocPosition - 1));
		int methodContentSize = methodContent.length();

		Matcher importMatcher = RegexPatterns.TS_METHODNAME_PATTERN.matcher(methodContent);
		if (importMatcher.find()) {
			matchedLine = StringUtils.trim(importMatcher.group());
			int prevMethodStartPos = importMatcher.start();
			int methodStartPos = importMatcher.end();
			
			while (methodStartPos < methodContentSize) {

				if (importMatcher.find(methodStartPos)) {
					String methodName = StringUtils.substringBefore(matchedLine, "(");
					String methodArgs = StringUtils.substringBetween(matchedLine, "(", ")");

					if (isMethodName(importMatcher.group())) {
						methodStartPos = importMatcher.end();
						continue;
					} else {	
						matchedLine = StringUtils.trim(importMatcher.group());
					}
					methodStartPos = importMatcher.start();
					addMethod(methodName, methodContent, methodArgs, prevMethodStartPos, methodStartPos);
					
					prevMethodStartPos = methodStartPos;
					methodStartPos = importMatcher.end();
				} else {
					break;
				}
			}
			
			String methodName = StringUtils.substringBefore(matchedLine, "(");
			String methodArgs = StringUtils.substringBetween(matchedLine, "(", ")");
			addMethod(methodName, methodContent, methodArgs, prevMethodStartPos, methodContentSize - 1);
		}
		LOGGER.info(tsObject.getMethods());
	}

	/**
	 * @param matchedLine
	 * @param methodContent
	 * @param methodEndPos
	 * @param methodStartPos
	 */
	protected void addMethod(String methodName, String methodContent, String methodArgs,
			int methodStartPos, int methodEndPos) {
		String methodContentTmp = StringUtils.substring(methodContent, methodStartPos, methodEndPos);
		
		TypeScriptMethod tsMethod = new TypeScriptMethod();
		tsMethod.setMethodName(methodName);
		tsMethod.setMethodArgs(methodArgs);
		
		methodContentTmp = StringUtils.substringAfter(methodContentTmp, "{");
		methodContentTmp = StringUtils.substringBeforeLast(methodContentTmp, "}");
		methodContentTmp = StringUtils.trim(methodContentTmp);
		tsMethod.setMethodContent(methodContentTmp.length() == 0? null: methodContentTmp);
		tsObject.addTypeScriptMethod(tsMethod);
	}
	
	private boolean isMethodName(String group) {
		group = StringUtils.trim(group);
		
		if (group.startsWith("if") || group.startsWith("for") || group.startsWith("while")) {
			return true;
		}
		return false;
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
			tsObject.setClassName(StringUtils.trim(className));
			
			String implementClasses = StringUtils.substringBetween(matchedLine, "implements", "{");
			if (implementClasses != null) {
				String[] implementClassArray = implementClasses.split(",");

				for (String implClass : implementClassArray) {
					tsObject.addImplementClassName(StringUtils.trim(implClass));
				}
			}
			currDocPosition = importMatcher.end();
		} 

		LOGGER.info(tsObject.getImplementClasses());
	}
	
	/**
	 * 
	 */
	protected void parseTsComponent() {
		currDocPosition = 0;
		String matchedLine = null;
		
		Matcher importMatcher = RegexPatterns.TS_COMPONENT_PATTERN.matcher(getFileContent());
		
		while (currDocPosition < fileSize) {
			if (importMatcher.find(currDocPosition)) {
				matchedLine = importMatcher.group();
				
				matchedLine = StringUtils.substringBetween(matchedLine, "{", "}");
				
				String[] compArray = matchedLine.split(",");
				for (String comp : compArray) {
					String[] compObjArray = comp.split(":");
					if (compObjArray.length == 2) {
						tsObject.addComponentKey(StringUtils.trim(compObjArray[0]), StringUtils.trim(compObjArray[1]));
					}
				}
				
				currDocPosition = importMatcher.end();
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}
		
		LOGGER.info(tsObject.getComponents());
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
						tsImport.addImportObject(StringUtils.trim(obj));
					}
				}
				
				currDocPosition = importMatcher.end();
				
				tsObject.addTypeScriptImport(tsImport);
			}

			if (importMatcher.hitEnd()) {
				break;
			}
		}

		LOGGER.info(tsObject.getImports());
	}

	@Override
	protected void filter() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the tsObject
	 */
	public TypeScript getTsObject() {
		return tsObject;
	}

	/**
	 * @param tsObject the tsObject to set
	 */
	public void setTsObject(TypeScript tsObject) {
		this.tsObject = tsObject;
	}
	
}
