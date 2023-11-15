/**
 * 
 */
package com.cognizant.fecodegen.service;

import com.cognizant.fecodegen.bo.CodeGenRequest;
import com.cognizant.fecodegen.components.CodeGenerator;
import com.cognizant.fecodegen.components.GeneratorPool;
import com.cognizant.fecodegen.components.SpringBootCodeGenerator;
import com.cognizant.fecodegen.components.SpringBootGeneratorPool;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class CodeGenFactory {

	public static CodeGenerator getGenerator(CodeGenRequest request) throws CodeGenException {
		CodeGenerator codeGen = null;
		
		switch (request.getUiTechnology()) {
		case "React":
			codeGen = GeneratorPool.INSTANCE.getReactJsGenerator();
			codeGen.setRequest(request);
			break;
	
		case "Angular" :
			codeGen = GeneratorPool.INSTANCE.getReactJsGenerator();
			codeGen.setRequest(request);
			break;
			
		case "AngularJs" :
			codeGen = GeneratorPool.INSTANCE.getAngularJsGenerator();
			codeGen.setRequest(request);
			break;	
		
		default:
			break;
		}
		return codeGen;
	}
	
	//return springBootCodeGenerator
	public static SpringBootCodeGenerator getSpringBootCodeGenerator(CodeGenRequest request) throws CodeGenException {
		SpringBootCodeGenerator springBootCodeGenerator = SpringBootGeneratorPool.INSTANCE.getSpringBootGenerator();
		springBootCodeGenerator.setRequest(request);
		return springBootCodeGenerator;
	}
	
}
