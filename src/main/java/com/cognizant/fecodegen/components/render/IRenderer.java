/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.Map;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public interface IRenderer {

	public boolean render(JsonDocument jsonDoc) throws CodeGenException;
	
	public void setOutFilePath(String outFilePath);
	
	public void initializeParams(JsonDocument jsonDoc);

	public void setConfig(JsonDocument configJson);
	
	public void setProperties(Map<String, Object> properties);
	
	public void setCodeGenConfig(JsonDocument codeGenConfig);
}
