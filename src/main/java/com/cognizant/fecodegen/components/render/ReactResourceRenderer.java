/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.model.ResourceEntity;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class ReactResourceRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactResourceRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactResourceRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Resources... ");
		
		boolean isDirCopy = false;
		if (jsonDoc.getJson().has("dirCopy")) {
			isDirCopy = jsonDoc.getJson().get("dirCopy").getAsBoolean();
		}
		
		JsonArray resourceFiles = getResourceNames();
		for (JsonElement res : resourceFiles) {
			if (res.isJsonObject()) {
				JsonObject obj = res.getAsJsonObject();
				if (obj.get("fileId") != null) {
					ResourceEntity entity = resourceRepository.findOne(obj.get("fileId").getAsBigInteger());
					String fileContent = entity.getFileContent();
					String filePath = obj.get("filePath").getAsString();
					
					String fullFilePath = getFullFilePath(filePath);
					try {
						FileUtils.write(new File(fullFilePath), fileContent + "\n");
					} catch (IOException e) {
						LOGGER.error("Error while generating framework files: ", e);
					}
				}
			} else {
				copyFiles(isDirCopy, res);
			}
		}
		
		return false;
	}

	/**
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	protected String getFullFilePath(String filePath) {
		String fullFilePath = getOutFilePath();
		fullFilePath = fullFilePath.replaceAll("\\\\", "/");
		
		if (StringUtils.endsWith(fullFilePath, Constants.FORWARD_SLASH) == false
				&& StringUtils.startsWith(filePath, Constants.FORWARD_SLASH) == false) {
			fullFilePath = fullFilePath + Constants.FORWARD_SLASH;
		}
		
		fullFilePath += filePath;
		
		return fullFilePath;
	}

	/**
	 * @param isDirCopy
	 * @param res
	 */
	protected void copyFiles(boolean isDirCopy, JsonElement res) {
		String fileName = res.getAsString();
		LOGGER.info("		Creating " + fileName + " ...");
		
		File srcFile = new File (JsonUtils.getFullFileName(fileName));
		if (srcFile.exists() == false) {
			String resourcePath = System.getProperty("resource.path");
			if (StringUtils.isNotBlank(resourcePath)) {
				srcFile = new File(resourcePath + "/" + fileName);
			}
		}
		
		try {
			if (isDirCopy) {
				FileUtils.copyDirectory(srcFile, new File(getOutputDirectory()));
			} else {
				FileUtils.copyFileToDirectory(srcFile, new File(getOutputDirectory()));
			}
		} catch (IOException e) {
			LOGGER.error("Error while generating framework files: ", e);
		}
	}

	private JsonArray getResourceNames() {
		JsonArray resourceFiles = config.getJsonObject("config").get("files").getAsJsonArray();
		
		return resourceFiles;
	}
}
