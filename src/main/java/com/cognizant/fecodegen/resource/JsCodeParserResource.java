package com.cognizant.fecodegen.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.fecodegen.bo.JsParsedComponent;
import com.cognizant.fecodegen.utils.HelperUtil;
import com.cognizant.fecodegen.visitor.JSNodeVisitor;
import com.google.gson.Gson;

@RestController
public class JsCodeParserResource {
	
	@GetMapping("/process/parsejs")
	public List<JsParsedComponent> parseJavaScripts() throws IOException {
		
		InputStream input = null;
		Properties prop = new Properties();
		
		input = getClass().getClassLoader().getResourceAsStream("config/parse.properties");

		// load a properties file
		prop.load(input);

		final File folder = new File(prop.getProperty("action.class.directory"));
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().endsWith(".jsp")) {
				parseJsContentFromJSP(fileEntry);
			}
		}
		
		
		List<JsParsedComponent> parsedComponents = null;
		
		String jsFile = prop.getProperty("file.output.js.path");
		Reader reader = new FileReader(jsFile);
		
		try {
			CompilerEnvirons env = new CompilerEnvirons();
			env.setRecordingLocalJsDocComments(true);
			env.setAllowSharpComments(true);
			env.setRecordingComments(true);
			AstRoot node = new Parser(env).parse(reader, jsFile, 1);
			JSNodeVisitor jsNodeVisitor = new JSNodeVisitor();
			node.visitAll(jsNodeVisitor);
			parsedComponents = jsNodeVisitor.getJsParsedValues();
		} finally {
			reader.close();
		}
		
		Gson gson = new Gson();	
		String jsonFormt = gson.toJson(parsedComponents);
		
		FileUtils.writeStringToFile(new File(prop.getProperty("file.output.js.json.path")), jsonFormt);
		
		return parsedComponents;
		
	}
	
	public static void parseJsContentFromJSP(File JspFilePath) throws IOException {
		
		
		Document doc = Jsoup.parse(JspFilePath, "UTF-8");
		Elements scripts = doc.getElementsByTag("script");
		for (Element script : scripts) {
		   for (DataNode node : script.dataNodes()) {
			   
			   HelperUtil helpUtilObj = new HelperUtil();
			   helpUtilObj.writeToFile(node.getWholeData(), "file.output.js.path");
			   
	           //FileUtils.writeStringToFile(new File("D://SampleExercise//JavaScript//itemSearch.js"),node.getWholeData());
	        }
		}
	}
	

}
