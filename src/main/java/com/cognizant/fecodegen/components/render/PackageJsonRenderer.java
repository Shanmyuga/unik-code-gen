/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.Map;

/**
 * @author 238209
 *
 */
public class PackageJsonRenderer extends BaseRenderer {

	public static String PREFIX = "codegen.packagejson"; 
	
	public PackageJsonRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

}
