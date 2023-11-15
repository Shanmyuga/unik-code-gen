/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;

/**
 * @author 238209
 *
 */
public class ReactUnitTestRenderer extends BaseRenderer {
	
	private static Logger LOGGER = Logger.getLogger(ReactUnitTestRenderer.class);
	
	public static String PREFIX = "codegen.react";
	
	public ReactUnitTestRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	/* (non-Javadoc)
	 * @see com.cognizant.fecodegen.components.render.BaseRenderer#render(com.cognizant.fecodegen.bo.JsonDocument)
	 */
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Unit Tests... ");
		

		Component component = CodeGenProperties.getCurrentReactComponent();
		String existingRelativePath = relativePath;
		String srcPath = jsonDoc.getAsString("srcPath");
		setRelativePath(existingRelativePath, srcPath, Constants.COMPONENT);
		write(parser.parse(templateName, getContextVariables(Constants.COMPONENT, component)),
				component.getComponentName().toLowerCase() + ".spec.js");
		setRelativePath(existingRelativePath, srcPath, Constants.REDUCER);

		write(parser.parse(reducerTemplate, getContextVariables(Constants.REDUCER, component)),
				component.getReducerName().toLowerCase() + ".spec.js");

		setRelativePath(existingRelativePath, srcPath, Constants.ACTION);

		write(parser.parse(actionTemplate, getContextVariables(Constants.ACTION, component)),
				component.getActionName().toLowerCase() + ".spec.js");
		
		return false;
	}
	
	protected Map<String, Object> getContextVariables(String name, Object component) {

		Map<String, Object> contextVariables = new HashMap<>();

		contextVariables.put(name, component);

		return contextVariables;
	}

	protected void setRelativePath(String existingRelativePath, String srcPath, String name) {
		Component component = CodeGenProperties.getCurrentReactComponent();

		if (name.equals(Constants.COMPONENT)) {
			relativePath = existingRelativePath + "/" + component.getComponentFolder();
		}
		if (name.equals(Constants.REDUCER)) {
			relativePath = existingRelativePath + "/" + component.getReducerFolder();
		}
		if (name.equals(Constants.ACTION)) {
			relativePath = existingRelativePath + "/" + component.getActionFolder();
		}
		component.calculateRelativePath(getOutputDirectory(), outFilePath + srcPath);
	}
}
