package com.cognizant.fecodegen.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.fecodegen.bo.CodeGenRequest;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.components.CodeGenerator;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import freemarker.template.Configuration;
import freemarker.template.Template;


@RestController
public class UIModellerRequestController extends CodeGenerator {
	
	protected JsonDocument configJson;
	@Autowired
	protected Configuration config;
	
	@RequestMapping(method=RequestMethod.POST, value="/uimodellerrequest")
	public void generateRequest(@RequestBody  List<CodeGenRequest> requests){
		requests.stream().forEach(request -> {
			String uiLayout = request.getUiLayout();
			String appName = request.getAppName();
			String uiName = request.getUiName();
			String codeGenpath = request.getCodeGenPath();
			configJson = JsonUtils.getJsonObject(uiLayout);
			JsonArray layout = configJson.getJsonArray("layout");
			List<JsonObject> uiComponentList = new ArrayList<JsonObject>();
			layout.forEach(je -> {
				JsonObject config = je.getAsJsonObject();
				String label = config.get("label").getAsString();
				label = label.replaceAll("\\s","");
				JsonArray columns = config.get("columns").getAsJsonArray();
				JsonObject uiDetails = new JsonObject();
				uiDetails.addProperty("DataType", "String");
				uiDetails.addProperty("VariableName", "id");
				uiDetails.addProperty("methodName", "Id");
				uiComponentList.add(uiDetails);
				columns.forEach(col -> {
					JsonObject obj = col.getAsJsonObject();
					String uiElementDataType = "String";
					String uiElementType = obj.get("type").getAsString();
					if(uiElementType.equals("textbox")){
						uiElementDataType = "String";
					}else if(uiElementType.equals("checkbox")){
						uiElementDataType = "Boolean";
					}
//					else if(uiElementType.equals("datepicker")){
//						uiElementDataType = "Date";
//					}
					String uiElementLabel = obj.get("label").getAsString();
					JsonObject uiObject = new JsonObject();
					uiObject.addProperty("DataType", uiElementDataType);
					uiObject.addProperty("VariableName", variableNameFormatter(uiElementLabel));
					uiObject.addProperty("methodName", methodNameFormatter(uiElementLabel));
					uiComponentList.add(uiObject);
				});
				try {
					String apiName = uiName.replaceAll("\\s","");
					Map<String, Object> sourceMap = new HashMap<String, Object>();
					sourceMap.put("className", apiName);
					sourceMap.put("member", uiComponentList);
					sourceMap.put("requestUrl", apiName.toLowerCase());
					sourceMap.put("package", "com.cognizant."+appName);
					sourceMap.put("apiName", appName);
					String projectPath =codeGenpath+"/"+appName;
					String packagePath = "/src/main/java/com/cognizant/"+appName+"/";
					String filePath = projectPath+packagePath;
					String appPropertiesPath = codeGenpath+"/"+appName+"/src/main/resources";
					String templatePath = "sercicefm/";
					generateSpringBoot(apiName, sourceMap, projectPath, packagePath, filePath, appPropertiesPath, templatePath);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
	}
	
	 private void generateSpringBoot(String apiName, Map<String, Object> sourceMap, String projectPath, String packagePath, String filePath, String appPropertiesPath, String templatePath){
		 try {
			Map<String, String> templateMap = getTemplateList(apiName, projectPath, packagePath, filePath, appPropertiesPath, templatePath);
			for (Map.Entry<String, String> entry : templateMap.entrySet()) {
				String file = entry.getKey();
				String fileDetails = entry.getValue();
				StringTokenizer stkFileDetails = new StringTokenizer(fileDetails, "~");
				while (stkFileDetails.hasMoreElements()) {
					String fileDestination = stkFileDetails.nextToken();
					String fileName = stkFileDetails.nextToken();
					String sourceCode = codeGeneration(sourceMap, templatePath+file+".ftl");
					writeSource(sourceCode,fileName,fileDestination);
				}
			}
		 }
		 catch (Exception e) {
			 e.printStackTrace();
		}
	 }
	 
	 public Map<String, String> getTemplateList(String apiName, String projectPath, String packagePath, String filePath, String appPropertiesPath, String templatePath){
		 Map<String, String> templateMap = new HashMap<String, String>();
		 templateMap.put("pom", projectPath+"~"+"pom.xml");
		 templateMap.put("Main", filePath+"~"+"Main");
		 templateMap.put("pojo", filePath+"dto"+"~"+apiName);
		 templateMap.put("controller",filePath+"controllers"+"~"+apiName+"Controller");
		 templateMap.put("service", filePath+"services"+"~"+apiName+"Service");
		 templateMap.put("serviceimpl", filePath+"service.impl"+"~"+apiName+"ServiceImpl");
		 templateMap.put("repository", filePath+"repositories"+"~"+apiName+"Repositories");
		 templateMap.put("response", filePath+"responses"+"~"+apiName+"Response");
		 templateMap.put("entity", filePath+"entities"+"~"+apiName+"Entity");
		 templateMap.put("application", appPropertiesPath+"~"+"application.properties");
		 return templateMap;
	 }
	 
	 private String codeGeneration(Map<String, Object> data, String templateName) throws Exception {
		 String response = null;
		 try {
				Template t = config.getTemplate(templateName);
				response = FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
			} catch (Exception e) {
				throw new Exception("Template Parser Exception: " + templateName, e);
			}
		 return response;
	 }
	 
	 public boolean writeSource(String sourceCode, String outFilename, String packageName) throws Exception {
		 File filePath = new File (packageName);
		 if (filePath.exists() == false) {
				filePath.mkdirs();
			}
		 String destinationFilePath;
		 if(outFilename.contains(".")){
			 destinationFilePath = packageName +"/"+ outFilename;
		 }
		 else{
			 destinationFilePath = packageName +"/"+ outFilename+".java";
		 }
		 boolean response = false;
		 try {
				OutputStream os = new FileOutputStream(new File(destinationFilePath));
				IOUtils.write(sourceCode, os);
				IOUtils.closeQuietly(os);
				response = true;
			} catch (FileNotFoundException e) {
				throw new Exception(e);
			} catch (IOException e) {
				throw new Exception(e);
			}
			return response;
		}
	 
	 public String variableNameFormatter(String label){
		String formattedVariable = "";
		String[] words=label.split(" ");
		int count = 0;
		for(String word :words){
			count++;
			 if(count == 1){
				 formattedVariable =word.toLowerCase();
			 }
			 else{
				 formattedVariable = formattedVariable + word.substring(0, 1).toUpperCase() + word.substring(1);
			 }
		}
		return formattedVariable;
	 }
	 
	 public String methodNameFormatter(String label){
		 String formattedMethodName = "";
		 String[] words=label.split(" ");
			for(String word :words){
				formattedMethodName = formattedMethodName + word.substring(0, 1).toUpperCase() + word.substring(1);
			}
		return formattedMethodName;
	 }
}
