package com.cognizant.fecodegen.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.Module;
import com.cognizant.fecodegen.bo.jsonoutput.RoutingModules;
import com.cognizant.fecodegen.components.CodeGenerator;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HelperUtil {
	private static Logger LOGGER = Logger.getLogger(CodeGenerator.class);
	
	@Autowired
	protected CodeGenTemplateParser parser;

	public void writeToFile(String content, String filePath) {

		BufferedWriter bw = null;
		FileWriter fw = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = getClass().getClassLoader().getResourceAsStream("config/parse.properties");

			// load a properties file
			prop.load(input);

			File file = new File(prop.getProperty(filePath));

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
				IOUtils.closeQuietly(input);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	/**
	 * This method is used to parse the routing file and store it in the file path
	 * 
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param routingModulesMap
	 * @param isModule
	 * @param moduleName
	 * @throws CodeGenException
	 */
	public void parseRoutingFile(String filePath, String fileName, String projectPath, RoutingModules routingModulesMap,
			Boolean isModule, String modCompPath, String userDefinedPath, String formFieldComponent)
			throws CodeGenException {
		try {

			String componentName = getComponentName(modCompPath);
			String moduleName = getModuleName(modCompPath);

			ModuleParser modParser = new ModuleParser(filePath + File.separator + fileName);
			modParser.parse();

			Module module = modParser.getModObject();

			Map<String, List<String>> routerMap = null;
			if (null != routingModulesMap) {
				routerMap = routingModulesMap.getComponentList();
			} else {
				routingModulesMap = new RoutingModules();
				routerMap = new HashMap<>();
			}

			List<String> componentList = routerMap.get(moduleName);
			if (componentList == null) {
				componentList = new ArrayList<>();
				routerMap.put(moduleName, componentList);
			}

			if (!componentList.contains(componentName) && !formFieldComponent.equals(componentName)) {
				componentList.add(componentName);
			}

			routingModulesMap.setComponentList(routerMap);

			String routingModule = StringUtils.EMPTY;
			Map<String, Object> contextVariables = new HashMap<>();

			if (isModule && null != module) {
				String compFileName = getComponentFileName(componentName);

				module.addRoutePath("'" + componentName + "'", StringUtils.capitalize(compFileName) + "Component");

				String importObj = getImportObject(modCompPath, userDefinedPath, componentName);

				module.addTypeScriptImport(importObj, StringUtils.capitalize(compFileName) + "Component");
			}

			contextVariables.put("ts", module);
			routingModule = parser.parse("fm/angular/routing_module_ts.ftl", contextVariables);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String routing = gson.toJson(routingModulesMap);

			// write converted json data to a file
			FileWriter writer = new FileWriter(projectPath + File.separator + Constants.ROUTING_CONF_JSON);
			writer.write(routing);
			writer.close();

			FileWriter writer2 = new FileWriter(
					projectPath + Constants.BASE_PATH + File.separator + Constants.APP_ROUTING_MODULE_TS);
			writer2.write(routingModule + "\n");
			writer2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param modCompPath
	 * @param userDefinedPath
	 * @param componentName
	 * @return
	 */
	private String getImportObject(String modCompPath, String userDefinedPath, String componentName) {
		String importStr;
		if (StringUtils.isEmpty(userDefinedPath)) {
			importStr = "./" + modCompPath + "/" + componentName + ".component";
		} else {
			importStr = "./" + userDefinedPath + "/" + componentName + "/" + componentName + ".component";
		}
		return importStr;
	}

	/**
	 * @param componentName
	 * @return
	 */
	private String getComponentFileName(String componentName) {
		String compFileName = StringUtils.EMPTY;
		if (componentName.contains("-")) {
			String[] strArr = componentName.split("-");

			StringBuilder compFileNameStr = new StringBuilder();
			for (String str : strArr) {
				compFileNameStr.append(StringUtils.capitalize(str));
			}
			compFileName = compFileNameStr.toString();
		} else {
			compFileName = componentName;
		}
		return compFileName;
	}

	/**
	 * @param modCompPath
	 * @return
	 */
	private String getComponentName(String modCompPath) {
		String componentName;
		if (modCompPath.contains("/")) {
			componentName = modCompPath.split("/")[modCompPath.split("/").length - 1];
		} else if (modCompPath.equalsIgnoreCase(Constants.APP)) {
			componentName = Constants.APP;
		} else {
			componentName = modCompPath;
		}
		return componentName;
	}

	/**
	 * @param modCompPath
	 * @return
	 */
	private String getModuleName(String modCompPath) {
		String moduleName;
		if (modCompPath.contains("/")) {
			moduleName = modCompPath.split("/")[0];
		} else if (modCompPath.equalsIgnoreCase(Constants.APP)) {
			moduleName = Constants.APP;
		} else {
			moduleName = modCompPath;
		}
		return moduleName;
	}

	/**
	 * @param filePath
	 * @param fileName
	 * @param componentPath
	 * @param userDefinedPath
	 * @param moduleName
	 * @param isApp
	 * @throws CodeGenException
	 */
	public void parseModuleFile(String componentPath, String userDefinedPath,
			String moduleName, Component component, boolean isApp) throws CodeGenException {
		try {
			Module module = component.getCompModule();
			if (isApp) {
				module = component.getAppModule();
			}
			
			String componentName = componentPath.split("/")[componentPath.split("/").length - 1];

			String compFileName = getComponentFileName(componentName);
			String modFileName = getComponentFileName(moduleName);

			String folderPath = getFolderPath(userDefinedPath, moduleName);

			module.addTypeScriptImport("@angular/platform-browser/animations", "BrowserAnimationsModule");
			module.addTypeScriptImport("@angular/material", "MatExpansionModule");
			module.addTypeScriptImport("@swimlane/ngx-datatable", "NgxDatatableModule");
			module.addTypeScriptImport("@angular/forms", "FormsModule");
			module.addTypeScriptImport("@angular/forms", "ReactiveFormsModule");
			module.addTypeScriptImport("@angular/material", "MatSidenavModule");
			module.addTypeScriptImport("@angular/material", "MatTabsModule");
			module.addTypeScriptImport("ag-grid-angular", "AgGridModule");
			
			module.addNgModuleImport("BrowserAnimationsModule");
			module.addNgModuleImport("MatExpansionModule");
			module.addNgModuleImport("NgxDatatableModule");
			module.addNgModuleImport("FormsModule");
			module.addNgModuleImport("ReactiveFormsModule");
			module.addNgModuleImport("MatSidenavModule");
			module.addNgModuleImport("MatTabsModule");
			module.addNgModuleImport("AgGridModule.withComponents([])");
			
			String componentFileName = StringUtils.capitalize(compFileName) + "Component";
			String moduleFileName = StringUtils.capitalize(modFileName) + "Module";

			if(isApp) {
				module.addTypeScriptImport("@ngx-translate/core", "TranslateModule");
				module.addTypeScriptImport("@ngx-translate/core", "TranslateLoader");
				module.addTypeScriptImport("@ngx-translate/core", "MissingTranslationHandler");

				module.addTypeScriptImport("@angular/common/http", "HttpClient");
				module.addTypeScriptImport("@angular/common/http", "HttpClientModule");
				module.addTypeScriptImport("@ngx-translate/http-loader", "TranslateHttpLoader");
				module.addTypeScriptImport("./utility/missing-translation", "NbMissingTranslationHandler");

				module.addNgModuleImport("HttpClientModule");
				
				String text = parser.read("fm/angular_translatemodule.ftl", true).toString();
				module.addNgModuleImport(text);
				component.setAppModule(module);
				
				module.addTypeScriptImport("./" + moduleName + "/" + moduleName + ".module", moduleFileName);
				module.addNgModuleImport(moduleFileName);
				module.setIsApp("true");
				
			} else {
				componentPath = componentName;
				component.setCompModule(module);
				
				if (StringUtils.isEmpty(userDefinedPath)) {
					module.addTypeScriptImport(folderPath + componentPath + "/" + componentName + ".component",
							componentFileName);
				} else {
					module.addTypeScriptImport(
							folderPath + userDefinedPath + "/" + componentName + "/" + componentName + ".component",
							componentFileName);
				}
				module.addNgModuleDeclaration(componentFileName);
				if (component.isReusableComponent()) {
					module.addNgModuleExport(componentFileName);
				}
				
				module.addTypeScriptImport("@ngx-translate/core", "TranslateModule");
				module.addNgModuleImport("TranslateModule");
			}

			Map<String, Object> contextVariables = new HashMap<>();
			contextVariables.put("ts", module);
			String text = parser.parse("module_ts_template", contextVariables);

			FileWriter writer = new FileWriter(isApp?component.getAppModulePath(): component.getCompModulePath());
			writer.write(text + "\n");
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param userDefinedPath
	 * @param moduleName
	 * @return
	 */
	private String getFolderPath(String userDefinedPath, String moduleName) {
		String folderPath = "./";

		if (StringUtils.isNotEmpty(userDefinedPath)) {
			if (userDefinedPath.contains(moduleName)) {
				folderPath = "./";
			} else {
				folderPath = "../";
			}
		}
		return folderPath;
	}
	
	public static void codeGenerate(String cmd) throws CodeGenException {
		
		boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
		
		String command = "cmd.exe";
		String commandArg = "/c";
		if (isWindows == false) {
			command = "sh";
			commandArg = "-c";
		}
		
		try {
			ProcessBuilder builder = new ProcessBuilder(command, commandArg, cmd );
			builder.redirectErrorStream(true);
			    Process p = builder.start();
			    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			    String line;
			    while (true) {
			        line = r.readLine();
			        if (line == null) { break; }
			        System.out.println(line);
			    }
			    p.destroy();
		} catch (IOException e) {
			LOGGER.error("Code Gen Exception: e=" + e.getMessage(), e);
			throw new CodeGenException(e);
		}
	}
}