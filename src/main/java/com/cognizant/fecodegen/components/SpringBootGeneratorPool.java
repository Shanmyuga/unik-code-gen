package com.cognizant.fecodegen.components;

import org.springframework.beans.factory.annotation.Autowired;

public class SpringBootGeneratorPool {

	public static SpringBootGeneratorPool INSTANCE;
	
	public static SpringBootGeneratorPool getInstance() {
		if (INSTANCE == null) {
			 INSTANCE = new SpringBootGeneratorPool();
		}
		
		return INSTANCE;
	}
	
	@Autowired
	private SpringBootGenerator springBootGenerator;
	
	/**
	 * @return the springBootGenerator
	 */
	public SpringBootGenerator getSpringBootGenerator() {
		return springBootGenerator;
	}

	/**
	 * @param springBootGenerator the springBootGenerator to set
	 */
	public void setSpringBootGenerator(SpringBootGenerator springBootGenerator) {
		this.springBootGenerator = springBootGenerator;
	}
}
