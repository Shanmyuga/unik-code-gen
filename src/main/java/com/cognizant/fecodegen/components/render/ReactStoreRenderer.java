/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class ReactStoreRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactStoreRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactStoreRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {

		LOGGER.info("Creating Store... ");
		
		Map<String, Object> contextVariables = new HashMap<>();
		StringBuilder content = new StringBuilder();
		content.append(parser.parse(templateName, contextVariables));
		
		write(content.toString());
		
		return false;
	}
}
