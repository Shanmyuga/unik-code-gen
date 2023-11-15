/**
 * 
 */
package com.cognizant.fecodegen.resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.CodeGenRequest;
import com.cognizant.fecodegen.components.CodeGenerator;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.service.CodeGenFactory;
import com.cognizant.fecodegen.utils.ZipUtils;
import com.google.gson.Gson;

/**
 * Resource Class to generate the UI source code
 * 
 * @author 238209
 *
 */
@RestController
public class CodeGenResource {

	private static Logger LOGGER = Logger.getLogger(CodeGenResource.class);
	
	@Autowired
	protected CodeGenTemplateParser parser;
	
	@Value("${cloud.code.gen.path}")
	private String cloudCodeGenPath;

	@Value("${unik.templates.source}")
	protected String configSource;
	
	@PostMapping("/process/ui")
	public @ResponseBody String processUi(@RequestBody CodeGenRequest request) {
		String response = "Success!";
		
		formatValidateRequest(request);
		
		try {
			// Added to consume from rest client
			if (request.getUiTechnology() == null) {
				String content = FileUtils.readFileToString(new File("D:/" + request.getUiLayout()),
						StandardCharsets.UTF_8);

				Map<String, Object> contextVariables = new HashMap<>();
				contextVariables.put("genLoginScreen", request.getGenLoginScreen());
				contextVariables.put("uiName", request.getUiName());

				String escapedContent = StringEscapeUtils.escapeJava(content);
				contextVariables.put("json", escapedContent);

				String inputJson = parser.parse("fm/layoutjson.ftl", contextVariables);

				Gson gson = new Gson();
				request = gson.fromJson(inputJson, CodeGenRequest.class);
			}

			String inputUiLayout = request.getUiLayout();
			request.setCodeGenPath(request.getCodeGenPath() + "/" + request.getProjectName());
			
			if (StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), request.getGenLoginScreen())) {
				response = generateLoginScreen(request);
			}
			
			request.setUiLayout(inputUiLayout);
			request.setGenLoginScreen(Boolean.FALSE.toString());
			
			CodeGenerator generator = CodeGenFactory.getGenerator(request);
			generator.generate();
			
			//request.setCodeGenPath(codeGenPath + "/" + baseFolder + "-service");
			//LOGGER.info("Spring Boot code generated in: " + request.getCodeGenPath());
			//to generate spring boot application
			//SpringBootCodeGenerator springBootCodegenerator = CodeGenFactory.getSpringBootCodeGenerator(request);
			//springBootCodegenerator.generate();
			
			zipGeneratedCode(request.getCodeGenPath());
			
			LOGGER.info("Your React application is ready for use !!");
		} catch (CodeGenException e) {
			response = "Error !!";
			LOGGER.error("Error while processing the UI.", e);
		} catch (IOException e1) {
			response = "Error !!";
			LOGGER.error("Error while processing the UI.", e1);
		}
		
		return response;
	}
	
	/**
	 * This method is used to process the angular layout
	 * 
	 * @param request
	 * @return response string
	 * @throws CodeGenException
	 */
	@ResponseBody
	@PostMapping("/process/angular/ui")
	public String processAngularUi(@RequestBody CodeGenRequest request) throws CodeGenException {
		String response = "Success!";

		formatValidateRequest(request);
		
		CodeGenerator generator = CodeGenFactory.getGenerator(request);
		generator.generate();
		LOGGER.info("Your Angular application is ready for use !!");

		zipGeneratedCode(request.getCodeGenPath() + "/" + request.getProjectName());
		
		return response;
	}

	private void zipGeneratedCode(String folderPath) {
		ZipUtils.zipDirectoryByOs(folderPath);
	}
	
	private void formatValidateRequest(CodeGenRequest request) {
		if (StringUtils.isNotBlank(request.getComponentName())) {
			request.setComponentName(StringUtils.lowerCase(request.getComponentName()));
		}
		
		if (StringUtils.isNotBlank(request.getModuleName())) {
			request.setModuleName(StringUtils.lowerCase(request.getModuleName()));
		}
		
		if (StringUtils.isBlank(request.getCodeGenPath())) {
			request.setCodeGenPath(cloudCodeGenPath);
		}
	}

	private String generateLoginScreen(CodeGenRequest request) throws CodeGenException {
		
		String uiLayout = parser.read("config/loginForm.json", false);
		
		request.setUiLayout(uiLayout);
		
		String response = "Success!";
		try {
			CodeGenerator generator = CodeGenFactory.getGenerator(request);
			generator.generate();
		} catch (CodeGenException e) {
			response = "Error !!";
			LOGGER.error("Error while processing the Login UI.", e);
		}
		
		return response;
	}
	
}
