package com.cognizant.fecodegen.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.fecodegen.bo.jsonoutput.Action;
import com.cognizant.fecodegen.bo.jsonoutput.ActionOutput;
import com.cognizant.fecodegen.utils.HelperUtil;
import com.cognizant.fecodegen.visitor.ClassVisitor;
import com.cognizant.fecodegen.visitor.MethodVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is used to parse all the action classes and save the output as a
 * JSON file.
 * 
 * @author 244898
 *
 */
@RestController
public class ActionParserResource {

	/**
	 * This method is used to parse all the action classes and write it as a JSON
	 * File
	 * 
	 * @throws IOException
	 */
	@GetMapping("/action/parse")
	public void parseAction() throws IOException {

		InputStream in = null;
		CompilationUnit cu = null;
		Properties prop = new Properties();
		InputStream input = null;
		Map<String, Action> actionMap = new HashMap<>();
		HelperUtil helperUtil = new HelperUtil();
		try {

			// Getting the properties file from the classpath
			input = getClass().getClassLoader().getResourceAsStream("config/parse.properties");

			// load a properties file
			prop.load(input);

			// Reading the file path from the properties file
			final File folder = new File(prop.getProperty("action.class.directory"));

			// Iterating through the list of files to extract the data
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().endsWith(".java")) {
					in = new FileInputStream(fileEntry);
					cu = JavaParser.parse(in);

					// Extract the class level details
					ClassVisitor classVisitor = new ClassVisitor();
					classVisitor.visit(cu, actionMap);

					// Extract the method level details
					MethodVisitor methodVisitor = new MethodVisitor();
					methodVisitor.visit(cu, actionMap);
				}
			}

			List<Action> actionList = new ArrayList<>();

			for (Map.Entry<String, Action> entry : actionMap.entrySet()) {
				actionList.add(entry.getValue());
			}

			// Converting the object into String and writing it as JSON file
			ActionOutput actionOutput = new ActionOutput();
			actionOutput.setActions(actionList);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonInString = gson.toJson(actionOutput);

			helperUtil.writeToFile(jsonInString, "file.output.path");

		} catch (ParseException x) {
			x.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}

	}
}
