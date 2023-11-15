/**
 * 
 */
package com.cognizant.fecodegen.components;

import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public interface ICodeGenerator {
	
	public boolean generate() throws CodeGenException;
}
