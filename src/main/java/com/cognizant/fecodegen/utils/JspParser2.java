package com.cognizant.fecodegen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.bo.JsParsedComponent;
import com.cognizant.fecodegen.bo.jsonoutput.Action;
import com.cognizant.fecodegen.bo.jsonoutput.ActionOutput;
import com.cognizant.fecodegen.bo.jsonoutput.FormElement;
import com.cognizant.fecodegen.bo.jsonoutput.Grid;
import com.cognizant.fecodegen.bo.jsonoutput.Layout;
import com.cognizant.fecodegen.bo.jsonoutput.Method;
import com.cognizant.fecodegen.bo.jsonoutput.Navigation;
import com.cognizant.fecodegen.bo.jsonoutput.ParsedJsp;
import com.cognizant.fecodegen.bo.jsonoutput.ParsedKibanaJsp;
import com.cognizant.fecodegen.bo.jsonoutput.Row;
import com.cognizant.fecodegen.bo.jsonoutput.Section;
import com.cognizant.fecodegen.bo.jsonoutput.Validation;
import com.cognizant.fecodegen.bo.jsonoutput.Validations;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

public class JspParser2 extends Parser {

	private ParsedJsp parsedJsp;

	public JspParser2(String fileName) throws CodeGenException {
		super(fileName);
		parsedJsp = new ParsedJsp();
		parsedJsp.setPath(fileName);
		String[] name = fileName.split(Pattern.quote(File.separator));
		parsedJsp.setFileName(name[name.length - 1]);
	}

	@Override
	protected void postProcess() {

	}

	@Override
	protected void parseContent() {
		try {
			Properties prop = new Properties();
			InputStream input = getClass().getClassLoader().getResourceAsStream("config/parse.properties");
			// load a properties file
			prop.load(input);

			InputStream in;
			File folder = new File(prop.getProperty("properties.file.directory"));
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().endsWith(".properties")) {
					in = new FileInputStream(fileEntry);
					prop.load(in);
				}
			}

			File file = new File(prop.getProperty("file.output.path"));

			String content = FileUtils.readFileToString(file);

			Gson gson = new Gson();
			ActionOutput actionOutput = gson.fromJson(content, ActionOutput.class);

			String htmlLine = null;
			Matcher htmlLineStartMatcher = RegexPatterns.HTML_TAG_LINE_PATTERN.matcher(getFileContent());

			Set<String> ignoredElements = new HashSet<>();
			List<ParsedKibanaJsp> parsedKibanaJspList = new ArrayList<>();
			while (currDocPosition < fileSize) {

				if (htmlLineStartMatcher.find(currDocPosition)) {
					htmlLine = htmlLineStartMatcher.group();

					String htmlTag = null;
					// System.out.println("start " + currDocPosition + " " + fileSize + " " +
					// htmlLine);
					Matcher m = RegexPatterns.HTML_TAG_PATTERN.matcher(htmlLine);
					if (m.find()) {

						htmlTag = StringUtils.substring(m.group(), 1);
						if (Arrays.asList(JspConstants.FORM_TAGS).contains(htmlTag.toLowerCase())) {
							FormElement element = new FormElement(htmlTag);
							ParsedKibanaJsp parsedKibanaJsp = new ParsedKibanaJsp();
							StringTokenizer tokenizer = new StringTokenizer(htmlLine, " ");
							// Skip the first Token
							if (tokenizer.hasMoreTokens()) {
								tokenizer.nextToken();
							}

							while (tokenizer.hasMoreTokens()) {
								String attribute = tokenizer.nextToken();

								String attributeKey = StringUtils.substringBefore(attribute, Constants.SEPARATOR_EQUAL);
								String[] endSyntax = { ">", "/>", "<" };
								if (!Arrays.asList(endSyntax).contains(attributeKey)) {
									String attributeValue = StringUtils.substringAfter(attribute,
											Constants.SEPARATOR_EQUAL);
									if (attributeValue.chars().filter(ch -> ch == '"').count() == 1
											&& tokenizer.hasMoreTokens()) {
										attributeValue = attributeValue + " " + tokenizer.nextToken();
									}
									attributeValue = StringUtils.remove(attributeValue,
											Constants.FORWARD_SLASH + Constants.SEPARATOR_GT);
									attributeValue = StringUtils.remove(attributeValue, Constants.SEPARATOR_GT);
									attributeValue = StringUtils.remove(attributeValue,
											Constants.SEPARATOR_DOUBLE_QUOTES);

									if (Arrays.asList(JspConstants.EVENT_ATTRIBUTES)
											.contains(attributeKey.toLowerCase())) {
										element.getEvents().put(attributeKey, attributeValue);
									} else if (Arrays.asList(JspConstants.VALIDATION_ATTRIBUTES)
											.contains(attributeKey.toLowerCase())) {
										element.getValidations().put(attributeKey, attributeValue);
									} else {
										element.getAttributes().put(attributeKey, attributeValue);

										if ("key".equals(attributeKey)) {
											element.getAttributes().put("label", prop.getProperty(attributeValue));
										}
									}
								}
							}
							/*
							 * Validation validation; parsedJsp.getValidations().add(validation);
							 */

							// for extracting c:url actions
							// setActionMethodValidations(actionOutput, element);

							parsedJsp.getForms().add(element);
							
							parsedKibanaJsp.setPath(parsedJsp.getPath());
							parsedKibanaJsp.setFileName(parsedJsp.getFileName());
							parsedKibanaJsp.setElement(element.getElement());
							
							Enumeration<?> en = prop.propertyNames();
							while (en.hasMoreElements()) {
								String key = (String) en.nextElement();
								String value = prop.getProperty(key);
								
								key = key.replace(".", ":");
								
								if(element.getElement().equalsIgnoreCase(key)){
										parsedKibanaJsp.setElementType(value);
								}
								
							}
							
							parsedKibanaJsp.setLabel(element.getAttributes().get("label"));
							Map<String, String> map;
							List<Map<String, String>> mapList = new ArrayList<>();
							List<Map<String, String>> eventList = new ArrayList<>();
							List<Map<String, String>> valList = new ArrayList<>();
							
							String keyAttributes = "";
							String otherAttributes = "";
							for (Map.Entry<String, String> entry : element.getAttributes().entrySet()) {
								map = new HashMap<>();
								map.put("key", entry.getKey());
								map.put("value", entry.getValue());
								
								String keyAttProp = (String) prop.get("keyAttributes");
								String[] keyValues = keyAttProp.split(",");
								for(String key : keyValues) {
									if(entry.getKey().equalsIgnoreCase(key)){
										keyAttributes = keyAttributes + (entry.getKey() + "='" + entry.getValue() + "',");
										break;
									}
									else
									{
										otherAttributes = otherAttributes + (entry.getKey() + "='" + entry.getValue() + "',");
										break;
									}
								}
								
								mapList.add(map);
							}
							
							otherAttributes  = removeLastChar(otherAttributes);
							parsedKibanaJsp.setOtherattributes(otherAttributes);
							keyAttributes = removeLastChar(keyAttributes);
							parsedKibanaJsp.setKeyattributes(keyAttributes);
							parsedKibanaJsp.setAttributes(mapList);
							
							String events = "";
							for (Map.Entry<String, String> entry : element.getEvents().entrySet()) {
								map = new HashMap<>();
								map.put("type", entry.getKey());
								map.put("value", entry.getValue());
								
								events = events + (entry.getKey() + "='" + entry.getValue() + "',");
								eventList.add(map);
							}
							events = removeLastChar(events);
							parsedKibanaJsp.setEvents(events);
							
							String clientSide = "";
							for (Map.Entry<String, String> entry : element.getValidations().entrySet()) {
								map = new HashMap<>();
								map.put("type", entry.getKey());
								map.put("value", entry.getValue());
								
								clientSide = clientSide + (entry.getKey() + "='" + entry.getValue() + "',");
								valList.add(map);
							}
							
							clientSide = removeLastChar(clientSide);
							parsedKibanaJsp.getValidations().setClientside(clientSide);
							
							parsedKibanaJspList.add(parsedKibanaJsp);

							if (Arrays.asList(JspConstants.NAVIGATION_TAGS).contains(htmlTag.toLowerCase())) {
								Navigation n = new Navigation();
								String jsMethodname = null;

								n.setElement(htmlTag);
								if (element.getEvents().containsKey("onclick")) {
									n.setJsMethod(element.getEvents().get("onclick"));
									jsMethodname = element.getEvents().get("onclick");
								} else if (element.getEvents().containsKey("onsubmit")) {
									n.setJsMethod(element.getEvents().get("onsubmit"));
									jsMethodname = element.getEvents().get("onsubmit");
								}

								if (jsMethodname != null) {

									try {
										Gson gsonObj = new Gson();
										String parsedJson = HelperUtil
												.readFile(prop.getProperty("file.output.js.json.path"));
										JsParsedComponent[] data = gsonObj.fromJson(parsedJson,
												JsParsedComponent[].class);

										for (JsParsedComponent jsComp : data) {

											if (jsMethodname.equalsIgnoreCase(jsComp.getFunctionName() + "();")) {
												n.setJsParsedComponent(jsComp);
											}

											if ("action".equals(jsComp.getVariable())) {
												for (Action action : actionOutput.getActions()) {
													if (jsComp.getVariableValue().contains(action.getNamespace())) {
														n.setNextPage(action.getResults());
													}
												}
											}

										}

									} catch (IOException e) {
										e.printStackTrace();
									}

								}

								element.getAttributes().forEach((k, v) -> {
									try {
										Navigation.class.getDeclaredMethod(
												"set" + k.substring(0, 1).toUpperCase() + k.substring(1), String.class)
												.invoke(n, v);
									} catch (Exception e) {
										// e.printStackTrace();
									}
								});
								parsedJsp.getNavigations().add(n);
							}

						} else {
							ignoredElements.add(htmlTag);
						}
						currDocPosition = htmlLineStartMatcher.end();
					}

				}
				if (htmlLineStartMatcher.hitEnd()) {
					break;
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);

			// for extracting navigation actions
			setNavigationValidations(actionOutput, prop);
			
			
			// for Setting Server side validations
			String validations = "";
			List<Map<String, String>> serverValList = new ArrayList<>();
			List<Validation> validationList = parsedJsp.getValidations();
			for (Validation val : validationList) {

				for (ParsedKibanaJsp parsedKib : parsedKibanaJspList) {
					List<Map<String, String>> attrList = parsedKib
							.getAttributes();
					for (Map<String, String> attmap : attrList) {
						if (val.getId().equalsIgnoreCase(attmap.get("value"))) {

							List<Validations> serverValidationValuesList = val
									.getServerSide();

							for (Validations serverValidationValue : serverValidationValuesList) {
								Map<String, String> map = new HashMap<>();
								map.put("elementName",
										serverValidationValue.getFieldName());
								map.put("type", serverValidationValue.getType());
								map.put("errorMsgKey",
										serverValidationValue.getKey());
								map.put("label", serverValidationValue.getLabel());
								
								if(serverValidationValue.getType() != null)
									validations =  validations + (serverValidationValue.getFieldName() + "='" + serverValidationValue.getType() + "',");
								else if(serverValidationValue.getFieldName() != null)
									validations =  validations + ( "'" +serverValidationValue.getFieldName() + "' = Required");	
							
								serverValList.add(map);
								//parsedKib.getValidations().setServerside(serverValList);
							}
							validations = removeLastChar(validations);
							parsedKib.getValidations().setServerside(validations);
						}
					}

				}
			}

			
			System.out.println(mapper.writeValueAsString(parsedJsp));
			System.out.println("====================================================\n");
			System.out.println(mapper.writeValueAsString(parsedKibanaJspList));
			
			// Forward engineering layout json
			Layout layout = new Layout();
			List<Section> sectionList = new ArrayList<>();
			Section sec = new Section();
			List<Map<String, String>> columnList = new ArrayList<>();
			Map<String, String> columnMap;
			Grid grid = new Grid();
			
			List<Row> rowList = new ArrayList<>();
			for (FormElement form : parsedJsp.getForms()) {
				if (Arrays.asList(JspConstants.SECTION_TAGS).contains(form.getElement())) {
					sec.setHtmlID(form.getAttributes().get("id"));
					sec.setType("section");
					sec.setSection("section");
				}
				if (Arrays.asList(JspConstants.COLUMN_TAG).contains(form.getElement())) {
					columnMap = new HashMap<>();
					columnMap.put("htmlID", form.getAttributes().get("id"));
					columnMap.put("type", prop.getProperty(form.getElement().replaceAll(":", ".")));
					columnMap.put("key", form.getAttributes().get("key"));

					if ("s:text".equals(form.getElement())) {
						columnMap.put("label", form.getAttributes().get("name"));
					} else {
						columnMap.put("label", prop.getProperty(form.getAttributes().get("key")));
					}

					if (null != form.getValidations()) {
						columnMap.putAll(form.getValidations());
					}
					columnList.add(columnMap);
				}
			}
			
			List<List<Map<String, String>>> list = Lists.partition(columnList, 2);
			
			Row row;
			for (List<Map<String, String>> entry : list) {
				row = new Row();
				row.setColumns(entry);
				rowList.add(row);
			}
			
			grid.setRow(rowList);
			sec.setGrid(grid);
			sectionList.add(sec);
			layout.setLayout(sectionList);

			System.out.println("====================================================\n");
			System.out.println(mapper.writeValueAsString(layout));

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(parsedJsp.toString());
		// System.out.println("Completed!!");

	}

	private void setNavigationValidations(ActionOutput actionOutput, Properties prop) {
		for (Navigation nav : parsedJsp.getNavigations()) {
			List<Validations> valList = null;
			if (nav.getAction() != null && nav.getAction().contains("!")) {
				String[] strArr = nav.getAction().split("!");
				String actionName = strArr[0];
				String methodName = strArr[1];

				for (Action action : actionOutput.getActions()) {
					String namespace = action.getNamespace().replace("/", "");
					namespace = namespace.replace("-", "");
					if (namespace.toLowerCase().equals(actionName.toLowerCase())) {
						for (Method mthd : action.getMethods()) {
							if (mthd.getName().equals(methodName)) {
								valList = mthd.getValidations();
							}
						}
					}
				}
			}
			if (null != valList) {

				for (Validations val : valList) {
					if(val.getKey() != null)
						val.setLabel(prop.getProperty(val.getKey()));
				}

				Validation val = new Validation();
				val.setElement(nav.getElement());
				val.setId(nav.getId());
				val.setName(nav.getName());
				val.setServerSide(valList);
				parsedJsp.getValidations().add(val);
			}
		}
	}

	@Override
	protected void filter() {

	}
	
	public String removeLastChar(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
	        str = str.substring(0, str.length() - 1);
	    }
	    return str;
	}
	
}
