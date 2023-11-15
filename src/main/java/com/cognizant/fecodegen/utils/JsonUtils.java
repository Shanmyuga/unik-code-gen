/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class JsonUtils {

    // my javascript beautifier of choice
    private static final String BEAUTIFY_JS_RESOURCE = "beautify.js";
    
 // my javascript beautifier of choice
    private static final String BEAUTIFY_HTML_RESOURCE = "beautify-html.js";

    // name of beautifier function
    private static final String BEAUTIFYJS_METHOD_NAME = "js_beautify";

    // name of beautifier function
    private static final String BEAUTIFYHTML_METHOD_NAME = "style_html";
    
	public static JsonDocument getJsonObject(String jsonString) {
		JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
		
		return new JsonDocument(jobj);
		/*JsonArray ja = jobj.get("layout").getAsJsonArray();
		
		ja.forEach(el -> {
			System.out.println("type: " + el.getAsJsonObject().get("type").getAsString());
			
			JsonArray jo = el.getAsJsonObject().get("columns").getAsJsonArray();
			System.out.println(jo);
			jo.forEach(cl -> {
				System.out.println(cl);
				System.out.println(cl.isJsonArray());
				cl.getAsJsonArray().forEach(col -> {
					col.getAsJsonObject().entrySet().stream().forEach(qm -> {
						String key = qm.getKey();
						System.out.println("key: " + key);
					});
				});
			});
		});*/
	}
	
	public static String getRelativePath(String basePath, String targetPath) {
		basePath = standardizePath(basePath);
		targetPath = standardizePath(targetPath);
		
		StringBuilder path = new StringBuilder();
		
		getRelativePath(basePath, targetPath, path);
		
		return standardizePath(path.toString());
	}
	
	public static void getRelativePath(String basePath, String targetPath, StringBuilder relativePath) {
		//String basePath = "D:/238209/Technical/react-samples/react-elig-utilities/react-elig-utilities-ui/test/component/";
		//String targetPath ="D:/238209/Technical/react-samples/react-elig-utilities/react-elig-utilities-ui/src/container/LoginContainer";
		
		if (targetPath.contains(basePath)) {
			String newTargetPath = targetPath.replace(basePath, "");
			newTargetPath = standardizePath(newTargetPath);
			relativePath.append(newTargetPath);
		} else {
			String newBasePath = StringUtils.substringBeforeLast(basePath, Constants.FORWARD_SLASH);
			relativePath.append("../");
			
			getRelativePath(newBasePath, targetPath, relativePath);
		}
		
		String commonPath = getCommonPath (basePath, targetPath);
		System.out.println(commonPath);
		
		/*String currentRelPath = StringUtils.substringAfter(basePath, Constants.BASE_PATH);
		while (currentRelPath.contains("//")) {
			currentRelPath = currentRelPath.replace("//", "/");
		}
		if (currentRelPath.startsWith("/")) {
			currentRelPath = StringUtils.substring(currentRelPath, 1);
		}
		
		String modelRelPath = StringUtils.substringAfter(targetPath, Constants.BASE_PATH);
		while (modelRelPath.contains("//")) {
			modelRelPath = modelRelPath.replace("//", "/");
		}
		
		int innerDirCount = 0;
		String[] currPathArray = currentRelPath.split("/");
		for (int index = currPathArray.length - 2; index >=0; index--) {
			if (StringUtils.isNotBlank(currPathArray[index])) {
				if (modelRelPath.contains(currPathArray[index]) == false) {
					innerDirCount++;
				}
			}
		}*/
		
		//return null; //StringUtils.repeat("../", innerDirCount);
	}
	
	private static String getCommonPath(String basePath, String targetPath) {
		String[] basePathArray = basePath.split("/");
		String[] targetPathArray = targetPath.split("/");
		
		boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
		
		StringBuilder commonPath = null;
		if (basePathArray != null && targetPathArray != null 
				&& basePathArray.length > 0 && targetPathArray.length > 0) {
			commonPath = new StringBuilder();
			
			int folderIndex = 0;
			for (String folder : basePathArray) {
				String targetFolder = null;
				if (targetPathArray.length >= (folderIndex + 1)) {
					targetFolder = targetPathArray[folderIndex];
				} else {
					break;
				}
				
				if (StringUtils.equalsIgnoreCase(folder, targetFolder)) {
					commonPath.append(folder);
					commonPath.append("/");
				}
					
				folderIndex++;
			}
		}
		
		if (isWindows == false && commonPath != null) {
			commonPath.insert(0, "/");
		}
 		
		return commonPath == null? null: commonPath.toString();
	}

	/**
	 * @param basePath
	 * @param targetPath
	 * @return
	 */
	public static String getRelativePath1(String basePath, String targetPath) {
		String currentRelPath = StringUtils.substringAfter(basePath, Constants.BASE_PATH);
		while (currentRelPath.contains("//")) {
			currentRelPath = currentRelPath.replace("//", "/");
		}
		if (currentRelPath.startsWith("/")) {
			currentRelPath = StringUtils.substring(currentRelPath, 1);
		}
		
		String modelRelPath = StringUtils.substringAfter(targetPath, Constants.BASE_PATH);
		while (modelRelPath.contains("//")) {
			modelRelPath = modelRelPath.replace("//", "/");
		}
		
		int innerDirCount = 0;
		String[] currPathArray = currentRelPath.split("/");
		for (int index = currPathArray.length - 2; index >=0; index--) {
			if (StringUtils.isNotBlank(currPathArray[index])) {
				if (modelRelPath.contains(currPathArray[index]) == false) {
					innerDirCount++;
				}
			}
		}
		
		return StringUtils.repeat("../", innerDirCount);
	}
	
	public static String beautifyJs(String jsString) {
		return beautifyJs(jsString, false);
	}
	
	public static String beautifyJs(String jsString, boolean isStandalone) {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		try {
			//this is needed to make self invoking function modules work
			// otherwise you won't be able to invoke your function
			engine.eval("var global = this;");
			if (isStandalone) {
				String path = "D:/238209/Technical/GitWorkspace/ops-app-service/src/main/resources/js/beautify.js";
				engine.eval(new InputStreamReader(new FileInputStream(new File(path))));
			} else {
				engine.eval(new InputStreamReader(JsonUtils.class.getResourceAsStream(BEAUTIFY_JS_RESOURCE)));
			}
			JsonObject obj = new JsonObject();
			obj.addProperty("indent_size", "4");
			obj.addProperty("indent_char", " ");
			obj.addProperty("max_preserve_newlines", "5");
			obj.addProperty("preserve_newlines", true);
			obj.addProperty("keep_array_indentation", true);
			obj.addProperty("break_chained_methods", false);
			obj.addProperty("indent_scripts", "normal");
			obj.addProperty("brace_style", "none,preserve-inline");
			obj.addProperty("space_before_conditional", true);
			obj.addProperty("unescape_strings", false);
			obj.addProperty("jslint_happy", true);
			obj.addProperty("end_with_newline", false);
			obj.addProperty("wrap_line_length", "0");
			obj.addProperty("indent_inner_html", false);
			obj.addProperty("comma_first", false);
			obj.addProperty("e4x", true);
			
			return (String) ((Invocable) engine).invokeFunction(BEAUTIFYJS_METHOD_NAME, jsString, obj);
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String beautifyJsRhino(String jsString, boolean isStandalone) {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		try {
			 Context cx = Context.enter();
			 Scriptable scope = cx.initStandardObjects();
			    
			//this is needed to make self invoking function modules work
			// otherwise you won't be able to invoke your function
			engine.eval("var global = this;");
			if (isStandalone) {
				String path = "D:\\238209\\Technical\\GitWorkspace\\ui-code-generator\\src\\main\\resources\\js\\beautify_rhino.js";

				Reader reader = new InputStreamReader(new FileInputStream(new File(path)));
				cx.evaluateReader(scope, reader, "__beautify.js", 1, null);
				reader.close();
			} else {
				engine.eval(new InputStreamReader(JsonUtils.class.getResourceAsStream(BEAUTIFY_JS_RESOURCE)));
			}
			String options = "{\n" + 
					"  \"indent_size\": \"4\",\n" + 
					"  \"indent_char\": \" \",\n" + 
					"  \"max_preserve_newlines\": \"5\",\n" + 
					"  \"preserve_newlines\": true,\n" + 
					"  \"keep_array_indentation\": true,\n" + 
					"  \"break_chained_methods\": false,\n" + 
					"  \"indent_scripts\": \"normal\",\n" + 
					"  \"brace_style\": \"none,preserve-inline\",\n" + 
					"  \"space_before_conditional\": true,\n" + 
					"  \"unescape_strings\": false,\n" + 
					"  \"jslint_happy\": true,\n" + 
					"  \"end_with_newline\": false,\n" + 
					"  \"wrap_line_length\": \"0\",\n" + 
					"  \"indent_inner_html\": false,\n" + 
					"  \"comma_first\": false,\n" + 
					"  \"e4x\": true\n" + 
					"}";
			JsonObject obj = new JsonObject();
			obj.addProperty("indent_size", "4");
			obj.addProperty("indent_char", " ");
			obj.addProperty("max_preserve_newlines", "5");
			obj.addProperty("preserve_newlines", true);
			obj.addProperty("keep_array_indentation", true);
			obj.addProperty("break_chained_methods", false);
			obj.addProperty("indent_scripts", "normal");
			obj.addProperty("brace_style", "none,preserve-inline");
			obj.addProperty("space_before_conditional", true);
			obj.addProperty("unescape_strings", false);
			obj.addProperty("jslint_happy", true);
			obj.addProperty("end_with_newline", false);
			obj.addProperty("wrap_line_length", "0");
			obj.addProperty("indent_inner_html", false);
			obj.addProperty("comma_first", false);
			obj.addProperty("e4x", true);
			
			scope.put("jsCode", scope, jsString);
			return (String) cx.evaluateString(scope, "js_beautify(jsCode, " + options +")",
			        "inline", 1, null);
			//return (String) ((Invocable) engine).invokeFunction(BEAUTIFYJS_METHOD_NAME, jsString, obj);
			
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String beautifyHtml(String htmlString, boolean isStandalone) {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		try {
			//this is needed to make self invoking function modules work
			// otherwise you won't be able to invoke your function
			engine.eval("var global = this;");
			if (isStandalone) {
				String path = "D:\\238209\\Technical\\GitWorkspace\\ui-code-generator\\src\\main\\resources\\js\\beautify-html.js";
				engine.eval(new InputStreamReader(new FileInputStream(new File(path))));
			} else {
				engine.eval(new InputStreamReader(JsonUtils.class.getResourceAsStream(BEAUTIFY_HTML_RESOURCE)));
			}
			
			String options = "{\n" + 
					"  \"indent_size\": \"4\",\n" + 
					"  \"indent_char\": \" \",\n" + 
					"  \"max_preserve_newlines\": \"5\",\n" + 
					"  \"preserve_newlines\": true,\n" + 
					"  \"keep_array_indentation\": true,\n" + 
					"  \"break_chained_methods\": false,\n" + 
					"  \"indent_scripts\": \"normal\",\n" + 
					"  \"brace_style\": \"collapse,preserve-inline\",\n" + 
					"  \"space_before_conditional\": true,\n" + 
					"  \"unescape_strings\": false,\n" + 
					"  \"jslint_happy\": true,\n" + 
					"  \"end_with_newline\": false,\n" + 
					"  \"wrap_line_length\": \"0\",\n" + 
					"  \"indent_inner_html\": false,\n" + 
					"  \"comma_first\": false,\n" + 
					"  \"e4x\": true\n" + 
					"}";
			return (String) ((Invocable) engine).invokeFunction(BEAUTIFYHTML_METHOD_NAME, htmlString);
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Integer getRandomNumber(Set<Integer> generatedRandom) {
		Random random = new Random();
		
		int randomNum = 0;
		do {
			randomNum = random.nextInt(100);
		} while (generatedRandom.add(randomNum) != true);
		
		return randomNum;
	}
	
	public static JsonDocument getMockJsonDocument() {
		String jsonString = "{\"layout\":[{\"type\":\"section\",\"id\":\"4\",\"section\":\"section\",\"label\":\"Leave Request Form\",\"htmlID\":\"htmlID\",\"columns\":[[{\"type\":\"textbox\",\"id\":4,\"label\":\"Leave Reason\",\"htmlID\":\"vacationReason\",\"mandatory\":true,\"length\":\"20\"},{\"type\":\"datepicker\",\"id\":7,\"options\":[],\"label\":\"Leave Start Date\",\"htmlID\":\"vacationStartDate\"},{\"type\":\"datepicker\",\"id\":8,\"options\":[],\"label\":\"Leave End Date\",\"htmlID\":\"vacationEndDate\"},{\"type\":\"dropdown\",\"id\":6,\"options\":[{\"display\":\"Sick Leave\",\"value\":\"sickLeave\",\"htmlids\":\"vacationType_1\"},{\"display\":\"Personal Leave\",\"value\":\"personalLeave\",\"htmlids\":\"vacationType_2\"}],\"label\":\"Vacation Type\",\"htmlID\":\"vacationType\",\"tempdisplay\":\"\",\"tempoption\":\"\"}]]}]}";
		
		return getJsonObject(jsonString);
	}
	
	public static void main(String[] args) {
		//getMockJsonDocument();
		
//		String jsFilePath = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen-1\\trizetto-layout-demo\\src\\app\\trizetto-line\\claimsinqleftnav1-left-nav\\claimsinqleftnav1-left-nav.component.ts";
//		beautifyJsFile(jsFilePath);
		
		//String htmlFilePath = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen-1\\trizetto-layout-demo\\src\\app\\trizettolinedet\\claimsinqdetleftnav1-left-nav\\claimsinqdetleftnav1-left-nav.component.html";
		//beautifyHtmlFile(htmlFilePath);
		
		//String test="\"{4}\"";
		//System.out.println(test.matches("\"\\{\\d+\\}\""));
		
		String curr = "D:/238209/Technical/react-samples/react-elig-utilities/react-elig-utilities-ui/test/component/";
		String target="D:/238209/Technical/react-samples/react-elig-utilities/react-elig-utilities-ui/src/container/LoginContainer";
		
		System.out.println("Path=" + getRelativePath(curr, target));
	} 
	
	private static void beautifyJsFile(String jsFilePath) {
		InputStream io = null;
		try {
			io = new FileInputStream(jsFilePath);
			
			byte[] buffer = new byte[io.available()];
			IOUtils.readFully(io, buffer);
			
			String jsContent = new String (buffer);
			String formattedString = beautifyJsRhino(jsContent, true);
			System.out.println(formattedString);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(io);
		}
		
	}
	
	private static void beautifyHtmlFile(String htmlFilePath) {
		InputStream io = null;
		try {
			io = new FileInputStream(htmlFilePath);
			
			byte[] buffer = new byte[io.available()];
			IOUtils.readFully(io, buffer);
			
			String htmlContent = new String (buffer);
			String formattedString = beautifyHtml(htmlContent, true);
			System.out.println(formattedString);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(io);
		}
		
	}

	public static void test() {
		String jsonString = "{\"layout\":[{\"type\":\"section\",\"id\":\"4\",\"section\":\"section\",\"label\":\"Leave Request Form\",\"htmlID\":\"htmlID\",\"columns\":[[{\"type\":\"textbox\",\"id\":4,\"label\":\"Leave Reason\",\"htmlID\":\"vacationReason\",\"mandatory\":true,\"length\":\"20\"},{\"type\":\"datepicker\",\"id\":7,\"options\":[],\"label\":\"Leave Start Date\",\"htmlID\":\"vacationStartDate\"},{\"type\":\"datepicker\",\"id\":8,\"options\":[],\"label\":\"Leave End Date\",\"htmlID\":\"vacationEndDate\"},{\"type\":\"dropdown\",\"id\":6,\"options\":[{\"display\":\"Sick Leave\",\"value\":\"sickLeave\",\"htmlids\":\"vacationType_1\"},{\"display\":\"Personal Leave\",\"value\":\"personalLeave\",\"htmlids\":\"vacationType_2\"}],\"label\":\"Vacation Type\",\"htmlID\":\"vacationType\",\"tempdisplay\":\"\",\"tempoption\":\"\"}]]}]}";
		JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
		JsonArray ja = jobj.get("layout").getAsJsonArray();
		ja.forEach(el -> {
			System.out.println("product: " + el.getAsJsonObject().get("product").getAsString());
			JsonObject jo = el.getAsJsonObject().get("question_mark").getAsJsonObject();            
			jo.entrySet().stream().forEach(qm -> {
				String key = qm.getKey();
				JsonElement je = qm.getValue();
				System.out.println("key: " + key);
				JsonObject o = je.getAsJsonObject();
				o.entrySet().stream().forEach(prop -> {
					System.out.println("\tname: " + prop.getKey() + " (value: " + prop.getValue().getAsString() + ")");
				});
			});
			System.out.println("");
		});
	} 
	
	public static String toCamelCase(String input) {
		String temp = StringUtils.replace(input, " ", "");
		
		StringBuilder content = new StringBuilder();
		content.append(StringUtils.lowerCase(StringUtils.substring(temp, 0, 1)));
		content.append(StringUtils.substring(temp, 1));
		
		return content.toString();
	}
	
	public static String parseToCamelCase(String input) {
		String[] temp = StringUtils.split(input, "-");
		
		StringBuilder content = new StringBuilder();
		for (String string : temp) {
			content.append(StringUtils.capitalize(string));
		}
		
		return StringUtils.uncapitalize(content.toString());
	}
	
	public static String toComponentName(String input) {
		input = input.replaceAll(" ", "-");
		input = input.toLowerCase();
		
		return input;
	}
	
	public static String getFullFileName(String filePath) {
		URL url = 
				JsonUtils.class.getClassLoader()
							.getResource(filePath);
		
		String configString = null;
		if (url != null) {
			configString = url.toString();
			configString = StringUtils.replace(configString, "file:/", "");
		}

		return configString;
	}

	public static String toMethodNameStandard(String input){
		String[] words=input.split(" ");
		StringBuilder content = new StringBuilder();
		for(String word :words){
			content.append(StringUtils.substring(word, 0, 1).toUpperCase());
			content.append( StringUtils.substring(word, 1));
		}
		return content.toString();
	}

	public static <T> T deepCopy(T object, Class<T> type) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(gson.toJson(object, type), type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, Object> toMap(JsonObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<Entry<String, JsonElement>> keysItr = object.entrySet().iterator();
	    while(keysItr.hasNext()) {
	    	Entry<String, JsonElement> iterValue = keysItr.next();
	    	String key = iterValue.getKey();
	        Object value = iterValue.getValue().getAsString();

	        if(value instanceof JSONArray) {
	            value = toList((JsonArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JsonObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}
	
	public static List<Object> toList(JsonArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JsonArray) {
	            value = toList((JsonArray) value);
	        }

	        else if(value instanceof JsonObject) {
	            value = toMap((JsonObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}

	public static String standardizePath(String currentPath) {
		while (currentPath.contains("\\")) {
			currentPath = currentPath.replace("\\", "/");
		}
		
		while (currentPath.contains("//")) {
			currentPath = currentPath.replaceAll("//", "/");
		}
		
		if (currentPath.startsWith("/")) {
			currentPath = currentPath.substring(1);
		}
		
		if (currentPath.endsWith("/")) {
			currentPath = currentPath.substring(0, currentPath.length() - 1); 
		}
		
		return currentPath;
	}
}
