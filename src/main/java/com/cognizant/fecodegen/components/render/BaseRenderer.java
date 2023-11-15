/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.ComponentOptions;
import com.cognizant.fecodegen.bo.FormDetail;
import com.cognizant.fecodegen.bo.FormElement;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.Module;
import com.cognizant.fecodegen.bo.jsonoutput.RoutingModules;
import com.cognizant.fecodegen.dao.LayoutRepository;
import com.cognizant.fecodegen.dao.ResourceRepository;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.cognizant.fecodegen.utils.ModuleParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class BaseRenderer implements IRenderer {
	
	private static Logger LOGGER = Logger.getLogger(BaseRenderer.class);
	
	protected String prefix;
	protected String templateName;
	protected String outFilename;
	protected String outFilePath;
	protected String relativePath;
	protected String reducerTemplate;
	protected String actionTemplate;
	
	protected Map<String, Object> properties;
	
	protected Map<String, Object> subParams;
	
	protected JsonDocument config;
	
	protected JsonDocument codeGenConfig;
	
	@Autowired
	protected CodeGenTemplateParser parser;
	
	@Autowired
	protected LayoutRepository layoutRepository;
	
	@Autowired
	protected ResourceRepository resourceRepository;

	@Value("${unik.templates.source}")
	protected String configSource;
	
	public BaseRenderer(String prefix, Map<String, Object> properties) {
		this.prefix = prefix;
		this.properties = properties;
		
		subParams = new HashMap<String, Object>();
	}

	public void initializeParams(JsonDocument jsonDoc) {
		this.templateName = jsonDoc.getAsString(Constants.TEMPLATE_NAME);
		this.outFilename = jsonDoc.getAsString(Constants.OUTPUT_FILE_NAME);
		this.relativePath = jsonDoc.getAsString(Constants.RELATIVE_PATH);
		this.reducerTemplate = jsonDoc.getAsString(Constants.REDUCER_TEMPLATE);
		this.actionTemplate = jsonDoc.getAsString(Constants.ACTION_TEMPLATE);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		String fileContent = readTemplate(jsonDoc);
		
		boolean response = false;
		if (StringUtils.isNotBlank(fileContent)) {
			
			if (validate()) {
				response = write(fileContent);;
			}
		}

		return response;
	}

	public String readTemplate(JsonDocument jsonDoc) throws CodeGenException {
		subParams = jsonDoc.getAsMap();
		
		return parser.parse(templateName, subParams);
	}

	protected boolean isKeyAttribute(String attrName) {
		JsonArray keyAttributes = null;
		
		if (config != null 
				&& config.getJsonObject("config") != null 
				&& config.getJsonObject("config").get("keyAttributes") != null) {
			keyAttributes = config.getJsonObject("config").get("keyAttributes").getAsJsonArray();
		} 
		
		boolean hasKeyAttribute = false;
		
		if (keyAttributes != null) {
			for (JsonElement je : keyAttributes) {
				String confAttrName = je.getAsString();
				if (StringUtils.equalsIgnoreCase(attrName, confAttrName)) {
					hasKeyAttribute = true;
				}
			}
		}
		
		return hasKeyAttribute;
	}
	
	public boolean validate() {
		return true;
	}
	
	public boolean write(String fileContent) throws CodeGenException {
		return write(fileContent, null);
	}
	
	public boolean write(String fileContent, String fileName) throws CodeGenException {
		String fullFilePath = getOuputFileName(fileName);
		
		boolean response = false;
		try {
			fileContent = formatContent(fileContent);
			
			OutputStream os = new FileOutputStream(new File(fullFilePath));
			IOUtils.write(fileContent, os);
			
			IOUtils.closeQuietly(os);
			response = true;
		} catch (FileNotFoundException e) {
			LOGGER.error("File not Found. Path=" + fullFilePath, e);
			throw new CodeGenException(e);
		} catch (IOException e) {
			LOGGER.error("IO Exception. Path=" + fullFilePath, e);
			throw new CodeGenException(e);
		}
		
		return response;
	}
	
	private String formatContent(String fileContent) {
		String formattedContent = fileContent;

		if (PackageJsonRenderer.PREFIX.equals(prefix)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			formattedContent = gson.toJson(new Gson().fromJson(fileContent, JsonObject.class));
		}
		
		return formattedContent;
	}

	public boolean writeAngular(String fileContent, String fileName,String userDefinedPath) throws CodeGenException {
		String fullFilePath = getAngularOuputFileName(fileName,userDefinedPath);

		boolean response = false;
		try {
			OutputStream os = new FileOutputStream(new File(fullFilePath));
			IOUtils.write(fileContent, os);

			IOUtils.closeQuietly(os);
			response = true;
		} catch (FileNotFoundException e) {
			LOGGER.error("File not Found. Path=" + fullFilePath, e);
			throw new CodeGenException(e);
		} catch (IOException e) {
			LOGGER.error("IO Exception. Path=" + fullFilePath, e);
			throw new CodeGenException(e);
		}

		return response;
	}

	/**
	 * @param fileName
	 * @return
	 */
	protected String getOuputFileName(String fileName) {
		String fullFilePath = getOutputDirectory();
		
		if ("Y".equals(CodeGenProperties.getCacheValueAsString("isTab"))) {
			outFilename = "*.js";
		}
		
		if (StringUtils.contains(outFilename, "*") && fileName != null) {
			outFilename = StringUtils.replace(outFilename, "*", fileName);
		} else if (fileName!= null && fileName.equalsIgnoreCase("index.html")){
			fullFilePath = fullFilePath + fileName;
			return fullFilePath;
		}else if (StringUtils.isNotBlank(fileName)) {
			outFilename = fileName;
		} 
		fullFilePath = fullFilePath + outFilename;
		return fullFilePath;
	}
	
	/**
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected String getAngularOuputFileName(String fileName, String userDefinedPath) {
		String componentFilePath = StringUtils.EMPTY;
		String componentName = StringUtils.EMPTY;
		String moduleName = StringUtils.EMPTY;

		String fullFilePath = getOutputDirectory();

		BufferedReader br = null;
		try {

			if (StringUtils.contains(outFilename, "*") && fileName != null) {
				outFilename = StringUtils.replace(outFilename, "*", fileName);
			} else if (StringUtils.contains(fileName, "/") && fileName != null) {
				String[] componentNameArray = fileName.split("/");
				moduleName = componentNameArray[0];
				componentName = componentNameArray[1];
			}

			Gson gson = new Gson();
			br = new BufferedReader(new FileReader(outFilePath + File.separator + Constants.ROUTING_CONF_JSON));

			// convert the json string back to object
			RoutingModules routingModulesMap = gson.fromJson(br, RoutingModules.class);

			JsonObject supportedLangs = config.getJson().get("params").getAsJsonObject()
					.get("languages").getAsJsonObject();
			Map<String, String> languages = new Gson().fromJson(supportedLangs, HashMap.class);
			
			Map<String, Object> contextVariables = new HashMap<>();
			contextVariables.put("routerLinks", routingModulesMap);
			contextVariables.put("brandName", "Cognizant");
			contextVariables.put("languages", languages);
			
			String appContent = parser.parse(Constants.APP_COMPONENT_HTML, contextVariables);

			FileWriter writer = new FileWriter(fullFilePath + "/app.component.html");
			writer.write(appContent);
			writer.close();

			if (StringUtils.isEmpty(userDefinedPath)) {
				componentFilePath = fullFilePath + "/" + moduleName + "/" + componentName + "/" + componentName
						+ ".component.html";
			} else {
				componentFilePath = fullFilePath + "/" + userDefinedPath + "/" + componentName + "/" + componentName
						+ ".component.html";
			}
			
			String modulePath = fullFilePath + "/" + moduleName + "/" + moduleName + ".module.ts";
			ModuleParser tsParser = new ModuleParser(modulePath);
			tsParser.parse();

			Module moduleTs = tsParser.getModObject();
			
			moduleTs.addTypeScriptImport("../appcommon/appcommon.module", "AppcommonModule");
			moduleTs.addNgModuleImport("AppcommonModule");
			
			contextVariables = new HashMap<>();
			contextVariables.put("ts", moduleTs);
			String text = parser.parse("module_ts_template", contextVariables);
			
			writer = new FileWriter(modulePath);
			writer.write(text + "\n");
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CodeGenException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		return componentFilePath;
	}

	protected String getAngularOuputFileName(String moduleName, String componentName, String userDefinedPath, String fileType, 
											 String compStyleExt) {
		String componentFilePath = StringUtils.EMPTY;
		String fullFilePath = getOutputDirectory();

		String fileSuffix = ".component.html";
		if ("TS".equalsIgnoreCase(fileType)) {
			fileSuffix = ".component.ts";
		} else if ("CSS".equalsIgnoreCase(fileType)) {
			fileSuffix = ".component." + compStyleExt;
		} 

		if (StringUtils.isNotEmpty(userDefinedPath)) {
			moduleName = userDefinedPath;
		} 
	
		componentFilePath = fullFilePath + "/" + moduleName + "/" + componentName + "/" + componentName + fileSuffix;
		return componentFilePath;
	}
	
	/**
	 * @return
	 */
	protected String getOutputDirectory() {
		String fullFilePath = outFilePath;
		if (StringUtils.endsWith(outFilePath, Constants.FORWARD_SLASH) == false) {
			fullFilePath = fullFilePath + "/";
		}
		//to replace application name in the relativePath for springboot generation
		if(relativePath.contains(Constants.APPNAME)){
			String path = relativePath.replaceAll(Constants.APPNAME, (String)properties.get(Constants.APPNAME));
			relativePath = path;
		}
		if (StringUtils.equalsIgnoreCase(relativePath, Constants.FORWARD_SLASH) == false) {
			fullFilePath = fullFilePath + relativePath;
			
			if (StringUtils.endsWith(outFilePath, Constants.FORWARD_SLASH) == false) {
				fullFilePath = fullFilePath + "/";
			}
		}
		
		File filePath = new File (fullFilePath);
		if (filePath.exists() == false) {
			filePath.mkdirs();
		}
		return fullFilePath;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getParametersMap(StringBuilder content, JsonObject obj, 
			String type, boolean appendRandomId) 
			throws CodeGenException {
		LOGGER.debug("Reading template for type = " + type);
		String rawTemplate = getRawTemplate(type);
		
		// Get the replaceable parameters defined in the template
		String[] stringArray = StringUtils.substringsBetween(rawTemplate, "${", "}");
		String[] listArray = StringUtils.substringsBetween(rawTemplate, "<#list ", " as");
		String[] paramsArray = ArrayUtils.addAll(stringArray, listArray);

		Set<Integer> generatedRandom = new HashSet<Integer>();
		// Set values for the parameters from the input UI layout / configuration
		Map<String, Object> contextVariables = new HashMap<String, Object>();
		if (paramsArray == null) {
			loadOtherAttributes(obj, contextVariables);
			
			return contextVariables;
		}
		
		for (String param : paramsArray) {
			String key = StringUtils.substringBefore(param, "_");
			JsonElement element = obj.get(key); 
			
			if (element != null) {
				if (element.isJsonArray()) {
					List<Map<String, Object>> arraysList = new ArrayList<>();
					
					JsonArray arrays = element.getAsJsonArray();
					for (JsonElement arrayElement : arrays) {
						arraysList.add(new Gson().fromJson(arrayElement, HashMap.class));
					}
					
					contextVariables.put(param, arraysList);
				} else {
					String value = element.getAsString();

					String operation = StringUtils.substringAfter(param, "_");
					if (StringUtils.equalsIgnoreCase(operation, "stripWhiteSpace")) {
						value = StringUtils.replace(value, " ", "");
					}

					if (appendRandomId) {
						value = value + JsonUtils.getRandomNumber(generatedRandom);
					}

					if (StringUtils.startsWith(value, "@")) {
						contextVariables.put(param, CodeGenProperties.getCacheValue(StringUtils.substring(value, 1)));
					} else {
						if(param.equalsIgnoreCase("label") || param.equalsIgnoreCase("label_i18n")){
							Constants.labelValues.put("app_"+value,value);
							String textValue  = "{{'app_"+value+"'|translate}}";

							LOGGER.info("label i18n="+textValue);
							if (param.equalsIgnoreCase("label_i18n")) {
								contextVariables.put(param, textValue);
							} else {
								contextVariables.put(param+"_i18n", textValue);
							}
						}
						else
						{
							contextVariables.put(param, value);
						}

					}
				}
			} else if (StringUtils.contains(param, ".") == false) {
				contextVariables.put(param, null);
			} else {
				contextVariables.put(key, "");
			}
			
		}
		
		loadOtherAttributes(obj, contextVariables);
		
		return contextVariables;
	}

	/**
	 * @param templateName
	 * @return
	 * @throws CodeGenException
	 */
	protected String getRawTemplate(String templateName) throws CodeGenException {
		if ("ftl".equalsIgnoreCase(configSource)) {
			return parser.read(templateName, true);
		} else {
			return parser.readFromConfig(templateName);
		}
	}
	
	
	private void loadOtherAttributes(JsonObject obj, Map<String, Object> contextVariables) {
		StringBuilder otherAttr = new StringBuilder();
		
		obj.entrySet().forEach(e -> {
			String key = e.getKey();
			if (contextVariables.containsKey(key) == false && isKeyAttribute(key) == false) {
				otherAttr.append(" ");

				if ("mandatory".equals(key)) {
					otherAttr.append("required");
					contextVariables.put(key, e.getValue().getAsString());
				} else {
					otherAttr.append(key);
					otherAttr.append("=");
					
					if (e.getValue().isJsonObject() || e.getValue().isJsonPrimitive()) {
						String value = e.getValue().toString();
						if (value.matches("\"\\{\\d+\\}\"")) {
							otherAttr.append(e.getValue().getAsString());
						} else {
							otherAttr.append(e.getValue());
						}
						
						if (e.getValue().isJsonObject()) {
							Map<String, Object> mapObject = JsonUtils.toMap(e.getValue().getAsJsonObject());
							
							contextVariables.put(key, mapObject);
						} else {
							contextVariables.put(key, e.getValue().getAsString());
						}
					}
				}

				otherAttr.append(" ");
			}
		});
		
		LOGGER.info("otherAttributes=" + otherAttr.toString());
		contextVariables.put(Constants.OTHER_ATTRIBUTES, otherAttr.toString());
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the outFilename
	 */
	public String getOutFilename() {
		return outFilename;
	}

	/**
	 * @param outFilename the outFilename to set
	 */
	public void setOutFilename(String outFilename) {
		this.outFilename = outFilename;
	}

	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * @return the outFilePath
	 */
	public String getOutFilePath() {
		return outFilePath;
	}

	/**
	 * @param outFilePath the outFilePath to set
	 */
	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * @param relativePath the relativePath to set
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * @return the config
	 */
	public JsonDocument getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(JsonDocument config) {
		this.config = config;
	}

	/**
	 * @return
	 */
	protected String getSavedFileName() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.startsWith(relativePath, Constants.FORWARD_SLASH)) {
			builder.append(StringUtils.substring(relativePath, 1));
		} else {
			builder.append(relativePath);
		}
		
		if (StringUtils.endsWith(builder.toString(), Constants.FORWARD_SLASH) == false) {
			builder.append(Constants.FORWARD_SLASH);
		}
		
		builder.append(outFilename.replace(".js", ""));

		String publicPath = CodeGenProperties.getCacheValueAsString(Constants.PUBLIC_PATH);
		return StringUtils.remove(builder.toString(), publicPath + Constants.FORWARD_SLASH);
	}
	
	protected String getRelativeSavedFileName(String cacheKey) {
		StringBuilder url = new StringBuilder(); 
		// component/LeaveReducerForm
		String filePath = CodeGenProperties.getCacheCompValueAsString(cacheKey);
		String publicPath = CodeGenProperties.getCacheValueAsString(Constants.PUBLIC_PATH);
		
		// Relative path = "/src"
		if (StringUtils.endsWith(relativePath, publicPath)) {
			url.append("./");
			url.append(filePath);
		} else {
			// Relative path = "/src/xxx"
			url.append("../");
			url.append(filePath);
		}
		
		return url.toString();
	}

	protected boolean checkFileExists(String fileName) {
		return getOutputFile(fileName).exists();
	}
	
	protected File getOutputFile(String fileName) {
		String outFileName = getOuputFileName(fileName);
		
		return new File(outFileName);
	}

	/**
	 * @param keyName 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected StringBuilder readUpdateExistingFile(String fileName, String keyName, Map<String, String> placeHolderMap) throws CodeGenException {
		
		StringBuilder content =  new StringBuilder();
	
		Reader reader = null;
		try {
			reader = new FileReader(getOutputFile(fileName));
			List<String> lines = IOUtils.readLines(reader);
	
			boolean alreadyUpdated = false;
			if (lines != null) {
				for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
					String line = iterator.next();
	
					if (StringUtils.contains(line, keyName)) {
						LOGGER.debug("Key Update already exists. key=" + keyName);
						alreadyUpdated = true;
					}
					
					if (alreadyUpdated == false) {
						placeHolderMap.forEach((ph, value) -> {
							if (StringUtils.contains(line, ph)) {
								content.append(value);
							}
						});
					}
					
					content.append(line + "\n"); 
					
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while reading File:" + fileName, e);
			throw new CodeGenException(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	
		return content;
	}
	
	protected String mergingFileContent(String fileName, String keyName, String beanContent, String importsContent) throws CodeGenException {
		
//		Reader reader = null;
		try {
//			reader = new FileReader(getOutputFile(fileName));
//			List<String> lines = IOUtils.readLines(reader);
//			lines.remove("}");
//			lines.add(content);
//			System.out.println(lines.toString());
//			System.out.println(lines);
//			int count = 0;
//			boolean alreadyUpdated = false;
//			if (lines != null) {
//				for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
//					count++;
//					String line = iterator.next();
//	
//					if (StringUtils.contains(line, keyName)) {
//						LOGGER.debug("Key Update already exists. key=" + keyName);
//						alreadyUpdated = true;
//					}
//					
//					if (alreadyUpdated) {
//						
//					}
//					else{
//						
//					}
//					
//					if(line.contains("#todo")){
////						if(!alreadyUpdated){
////							LineNumberReader lnr = new LineNumberReader(reader);
////							lnr.setLineNumber(count-1);
////							OutputStream os = new FileOutputStream(getOutputFile(fileName));
////							IOUtils.writeLines(lines, content, os);
////							IOUtils.closeQuietly(os);
//						System.out.println(line);
//						System.out.println(content);
//							line = line.replace("#todo", content);
//							System.out.println(line);
//							lines.add(line);
////						}
//					}
//				}
//				
//			}
//			System.out.println(lines);
//			return lines.toString();
			
			BufferedReader reader = new BufferedReader(new FileReader(getOutputFile(fileName)));
			String oldContent= "";
			String line = reader.readLine();
			while (line != null) 
			{
				if(line.contains("//todo"))
					line.trim();
			        oldContent = oldContent + line + System.lineSeparator();
			        line = reader.readLine();
			}
			String newContent = oldContent.replaceAll("//imports", importsContent);
			newContent = newContent.replaceAll("//todo", beanContent);
			System.out.println(newContent);
			return newContent;
		} catch (Exception e) {
			LOGGER.error("Exception while reading File:" + fileName, e);
			throw new CodeGenException(e);
		} finally {
//			IOUtils.closeQuietly(reader);
		}
	}

	protected String getTemplateName(String type) {
		JsonObject templates = config.getJsonObject("config").get("templates").getAsJsonObject();
		
		if (templates.get(type) == null) {
			LOGGER.info("No templates defined for type=" + type);
		}
		
		return templates.get(type).getAsString();
	}
	
	protected void addImports(String type, FormDetail formDetail) {
		String templateName = getTemplateName(type);
		templateName = templateName.replace(".ftl", "_import.ftl");
		
		String jsonContent = null;
		try {
			jsonContent = getRawTemplate(templateName);
		} catch (CodeGenException e) {
			LOGGER.info("No import templates defined for type=" + type);
		}
		
		if (jsonContent == null) {
			return;
		}
		
		JsonDocument jsonDocument = JsonUtils.getJsonObject(jsonContent);
		JsonArray importArray = jsonDocument.getJsonArray("import");
		
		String packageName = null;
		String importClasses = null;
		String fileName = null;
		for (JsonElement jsonElement : importArray) {
			JsonObject obj = jsonElement.getAsJsonObject();
			
			if (obj.has("packageName")) {
				packageName = obj.get("packageName").getAsString();
			} else {
				packageName = null;
			}
			
			if (obj.has("importClasses")) {
				importClasses = obj.get("importClasses").getAsString();
			} else {
				importClasses = null;
			}
			
			if (obj.has("fileName")) {
				fileName = obj.get("fileName").getAsString();
			} else {
				fileName = null;
			}
			
			formDetail.addImport(packageName, importClasses, fileName);
		}
	}

	protected String getCamelCase(String sectionHtmlId) {
		if (sectionHtmlId != null && sectionHtmlId.contains("-")) {
			
			String[] array = sectionHtmlId.split("-");
			String camCaseStr = array[0];
			for (int i = 1; i < array.length; i++) {
				camCaseStr += StringUtils.upperCase(StringUtils.substring(array[i], 0, 1));
				camCaseStr += StringUtils.lowerCase(StringUtils.substring(array[i], 1));
			}
			
			return camCaseStr;
		}
		
		return sectionHtmlId;
	}

	protected void addFormElement(List<FormElement> formElementList, JsonObject obj) {
		JsonArray formExcludeList = 
				config.getJsonObject("config").get("formExcludeList").getAsJsonArray();
		String type = obj.get("type").getAsString();
		Boolean isExcludeList = false;
		for (JsonElement element : formExcludeList) {
			if (StringUtils.equalsIgnoreCase(type, element.getAsString())) {
				isExcludeList = true;
				break;
			}
		}
		
		if (isExcludeList) {
			return;
		}
		
		FormElement formElement = null;
		if (obj.get("htmlID") != null) {
			formElement = new FormElement();
			formElement.setHtmlId(obj.get("htmlID").getAsString());
			formElement.setHtmlIdCamCase(getCamelCase(formElement.getHtmlId()));
			formElement.setType(type);
			
			if (obj.has("mandatory")) {
				formElement.setMandatory(obj.get("mandatory").getAsBoolean());
			} else if (obj.has("required")) {
				formElement.setMandatory(obj.get("required").getAsBoolean());
			}
			
			if (obj.has("minLength")) {
				formElement.setMinLength(obj.get("minLength").getAsInt());
			}
			
			if (obj.has("maxLength")) {
				formElement.setMaxLength(obj.get("maxLength").getAsInt());
			}
			
			if (obj.has("email")) {
				formElement.setEmail(obj.get("email").getAsBoolean());
			}
			
			if (obj.has("pattern")) {
				if (StringUtils.isNotBlank(obj.get("pattern").getAsString())) {
					formElement.setPattern(obj.get("pattern").getAsString());
				}
			}
			
			JsonArray validationOrderArray = null;
			
			if (config != null 
					&& config.getJsonObject("config") != null 
					&& config.getJsonObject("config").get("formValidationOrder") != null) {
				validationOrderArray = config.getJsonObject("config").get("formValidationOrder").getAsJsonArray();
			} 
			
			if (validationOrderArray != null) {
				formElement.generateTestData(validationOrderArray);
			}
			
			List<ComponentOptions> optionsList = getComponentOptions(obj);
			if (optionsList != null) {
				formElement.setOptionsList(optionsList);
			}
			
			formElementList.add(formElement);
		}
	}
	
	protected List<ComponentOptions> getComponentOptions(JsonObject obj) {
		List<ComponentOptions> optionsList = null;
		JsonElement element = obj.get("options");
		
		if (element != null && !"[]".equals(element.toString())) {
			optionsList = new ArrayList<>();
			
			JsonArray compOptions = element.getAsJsonArray();
			
			for (JsonElement childElement : compOptions) {
				System.out.println(childElement);
				JsonObject jsonObj = childElement.getAsJsonObject();
				
				Set<Entry<String, JsonElement>> optionValuesSet = jsonObj.entrySet();
				ComponentOptions option = new ComponentOptions();
				for (Entry<String, JsonElement> entry : optionValuesSet) {
					if("display".equals(entry.getKey())) {
						option.setValue(entry.getValue().getAsString());
					}
					if("value".equals(entry.getKey())) {
						option.setKey(entry.getValue().getAsString());
					}
				}
				optionsList.add(option);
			}
		} 
		
		return optionsList;
	}

	protected void addDatatableElement(List<FormElement> dataTableList, JsonObject obj) {
		JsonArray dtOptions = obj.get("dtcols").getAsJsonArray();
		
		String htmlId = obj.get("htmlID").getAsString();
		
		FormElement formElement = null;
		for (JsonElement dtOptEle : dtOptions) {
			JsonArray dtOptArr = dtOptEle.getAsJsonArray();
			for (JsonElement dtOpt : dtOptArr) {
				formElement = new FormElement();
				formElement.setHtmlId(htmlId);
				formElement.setPropValue(dtOpt.getAsJsonObject().get("value").getAsString());
				formElement.setDisplayColumnValue(dtOpt.getAsJsonObject().get("display").getAsString());
				
				if (dtOpt.getAsJsonObject().has("editable")) {
					formElement.setEditable(dtOpt.getAsJsonObject().get("editable").getAsBoolean());
				} else {
					formElement.setEditable(false);
				}
				
				if (dtOpt.getAsJsonObject().has("editableAlways")) {
					formElement.setEditableAlways(dtOpt.getAsJsonObject().get("editableAlways").getAsBoolean());
				} else {
					formElement.setEditableAlways(false);
				}
				
				formElement.setDataType(dtOpt.getAsJsonObject().get("dataType").getAsString());

				dataTableList.add(formElement);
			}
		}
	}

	/**
	 * @return the reducerTemplate
	 */
	public String getReducerTemplate() {
		return reducerTemplate;
	}

	/**
	 * @param reducerTemplate the reducerTemplate to set
	 */
	public void setReducerTemplate(String reducerTemplate) {
		this.reducerTemplate = reducerTemplate;
	}

	/**
	 * @return the actionTemplate
	 */
	public String getActionTemplate() {
		return actionTemplate;
	}

	/**
	 * @param actionTemplate the actionTemplate to set
	 */
	public void setActionTemplate(String actionTemplate) {
		this.actionTemplate = actionTemplate;
	}

	/**
	 * @return the layoutRepository
	 */
	public LayoutRepository getLayoutRepository() {
		return layoutRepository;
	}

	/**
	 * @param layoutRepository the layoutRepository to set
	 */
	public void setLayoutRepository(LayoutRepository layoutRepository) {
		this.layoutRepository = layoutRepository;
	}

	/**
	 * @return the codeGenConfig
	 */
	public JsonDocument getCodeGenConfig() {
		return codeGenConfig;
	}

	/**
	 * @param codeGenConfig the codeGenConfig to set
	 */
	public void setCodeGenConfig(JsonDocument codeGenConfig) {
		this.codeGenConfig = codeGenConfig;
		this.parser.setConfig(codeGenConfig);
	}
}
