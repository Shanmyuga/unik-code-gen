/**
 * 
 */
package com.cognizant.fecodegen.components;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.CodeGenRequest;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.dao.ConfigRepository;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.model.ConfigEntity;
import com.cognizant.fecodegen.model.FormTemplate;
import com.cognizant.fecodegen.model.ResourceTemplate;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class CodeGenerator implements ICodeGenerator {

	protected static Logger LOGGER = Logger.getLogger(CodeGenerator.class);
	
	protected CodeGenRequest request;
	
	@Autowired
	protected CodeGenTemplateParser parser;
	
	protected JsonDocument configJson;

	@Value("${unik.templates.source}")
	protected String configSource;
	
	@Autowired
	protected ConfigRepository configRepository;
	
	private void configure() throws CodeGenException {
		if (request != null) {
			String configName = request.getConfigName();
			readConfig(configName);
		}
	}

	/**
	 * @param configName
	 * @return
	 * @throws CodeGenException
	 */
	protected String readConfig(String configName) throws CodeGenException {
		String configString = null;
		ConfigEntity entity = null; 
				
		JsonArray formTemplatesArray = null;
		if ("ftl".equalsIgnoreCase(configSource)) {
			configString = parser.read("config/"+configName+".json", false);
		} else {
			ConfigEntity defaultEntity = null;
			entity = configRepository.findOne(new BigInteger(configName));
			
			List<FormTemplate> formTemplates = null; 
			
			if (entity.getDefaultConfigId() != null && StringUtils.isNotBlank(entity.getDefaultConfigId().toString())) {
				defaultEntity = configRepository.findOne(entity.getDefaultConfigId());
				configString = defaultEntity.getDefaultConfigJson();
				
				formTemplates = defaultEntity.getFormTemplates();
				overrideWithCustomizedTemplates(formTemplates, entity.getFormTemplates());
			} else {
				configString = entity.getDefaultConfigJson();
				formTemplates = entity.getFormTemplates();
			}
			
			String jsonArray = new Gson().toJson(formTemplates);
			formTemplatesArray = new Gson().fromJson(jsonArray, JsonArray.class);
			
			List<ResourceTemplate> resourceTemplates = entity.getResourceTemplates();
			String resourceFiles = new Gson().toJson(resourceTemplates);
			configString = configString.replace("\"${resource_files}\"", resourceFiles);
		}
		
		configString = configString.replace("${layout_json}", request.getUiLayout());
		
		configJson = JsonUtils.getJsonObject(configString);
		configJson.getJson().get("componentConfig").getAsJsonObject().add("templates", formTemplatesArray);
		
		if (entity != null) {
			JsonObject compConfig = configJson.getJson().get("componentConfig").getAsJsonObject();
			if (entity.getComponentConfig() != null) {
				if (StringUtils.isNotBlank(entity.getComponentConfig().getComponentPrefix())) {
					compConfig.addProperty("componentPrefix", entity.getComponentConfig().getComponentPrefix());
				}
				
				if (StringUtils.isNotBlank(entity.getComponentConfig().getStyleExt())) {
					compConfig.addProperty("styleExt", entity.getComponentConfig().getStyleExt());
				}
			}
		}
		
		parser.setConfig(configJson);
		
		return configString;
	}
	
	private void overrideWithCustomizedTemplates(List<FormTemplate> defaultTemplates, List<FormTemplate> customTemplates) {
		if (customTemplates != null && customTemplates.isEmpty() == false) {
			for (FormTemplate customTemplate : customTemplates) {
				for (FormTemplate formTemplate : defaultTemplates) {
					if (StringUtils.equalsIgnoreCase(customTemplate.getBaseTemplateName(), formTemplate.getBaseTemplateName())) {
						formTemplate.setTemplateCode(customTemplate.getTemplateCode());
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean generate() throws CodeGenException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the request
	 */
	public CodeGenRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 * @throws CodeGenException 
	 */
	public void setRequest(CodeGenRequest request) throws CodeGenException {
		this.request = request;
		
		configure();
	}

}
