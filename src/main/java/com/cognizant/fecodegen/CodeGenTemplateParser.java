/**
 * 
 */
package com.cognizant.fecodegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author 238209
 *
 */
public class CodeGenTemplateParser {

	private static Logger LOGGER = Logger.getLogger(CodeGenTemplateParser.class);
	
	@Autowired
	protected Configuration fmConfig;
	
	protected JsonDocument config;

	@Value("${unik.templates.source}")
	protected String configSource;
	
	public String parse(String templateName, Map<String, Object> contextVariables) 
			throws CodeGenException {
		String response = null;

		try {
			Template t = null;
			if (templateName.endsWith(".ftl")) {
				t = fmConfig.getTemplate(templateName);
			} else {
				String tempContent = readFromConfig(templateName);
				t = new Template("template", new StringReader(tempContent), fmConfig);
			}
			
			response = FreeMarkerTemplateUtils.processTemplateIntoString(t, contextVariables);
		} catch (Exception e) {
			LOGGER.error("Error while parsing the template: " + templateName, e);
			throw new CodeGenException("Template Parser Exception: " + templateName, e);
		}

		response = response.replaceAll("(?m)^$([\\r\\n]+?)(^$[\\r\\n]+?^)+", "$1");
		
		return response;
	}

	public String readFromConfig(String templateName) {
		String templateContent = null;
		if (config != null && config.getJsonObject("componentConfig") != null) {
			JsonArray templatesArray = 
					config.getJsonObject(Constants.TAG_COMPONENT_CONFIG).getAsJsonObject()
				 		  .get(Constants.TAG_TEMPLATES).getAsJsonArray();
			
			String baseTemplateName = null;
			JsonObject jsonObj = null;
			for (JsonElement element : templatesArray) {
				jsonObj = element.getAsJsonObject();
				baseTemplateName = jsonObj.get(Constants.TAG_TEMPLATE_BASE_NAME).getAsString(); 
				if (StringUtils.equalsIgnoreCase(baseTemplateName, templateName)) {
					templateContent = jsonObj.get(Constants.TAG_TEMPLATE_CODE).getAsString();
					break;
				}
			}
		}
		
		return templateContent;
	}

	public String read(String templatePath, boolean isTemplate) throws CodeGenException {
		String path = templatePath;
		if (isTemplate) {
			path = "templates/" + templatePath;
		}
		
		LOGGER.info("Template Path = " + path);
		String configString = readFile(path);

		return configString;
	}

	/**
	 * @param templatePath
	 * @param path
	 * @return
	 * @throws CodeGenException
	 */
	public String readFile(String path) throws CodeGenException {
		InputStream is = 
				getClass().getClassLoader()
							.getResourceAsStream(path);
		
		String configString = null;
		if (is == null) {
			return configString;
		}
		
		try {
			byte[] configByteArray = new byte[is.available()];
			IOUtils.read(is, configByteArray);
			
			configString = new String(configByteArray);
		} catch (IOException e) {
			LOGGER.error("Error while reading the configuration: " + path, e);
			throw new CodeGenException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return configString;
	}

	/**
	 * @param templatePath
	 * @param path
	 * @return
	 * @throws CodeGenException
	 */
	public String readFileExternal(String path) throws CodeGenException {
		InputStream is = null;
		
		String configString = null;
		try {
			is = new FileInputStream(new File(path));
			byte[] configByteArray = new byte[is.available()];
			IOUtils.readFully(is, configByteArray);
			
			configString = new String(configByteArray);
		} catch (IOException e) {
			LOGGER.error("Error while reading the configuration: " + path, e);
			throw new CodeGenException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return configString;
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
}
