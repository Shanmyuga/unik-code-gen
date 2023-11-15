/**
 * 
 */
package com.cognizant.fecodegen.exception;

/**
 * @author 238209
 *
 */
public class CodeGenException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5732877981744768130L;

	public CodeGenException(Throwable e) {
		super(e);
	}
	
	public CodeGenException(String message, Throwable e) {
		super(message, e);
	}
}
