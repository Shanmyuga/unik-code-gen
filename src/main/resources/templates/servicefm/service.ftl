package ${package}.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import ${package}.response.${className}Response;
import ${package}.entity.${className}Entity;
import ${package}.dto.${className};

public interface ${className}Service {
	
	<#if genLoginScreen == "true">
	public Boolean loginAuthetication(String id);
	</#if>
	
	public ResponseEntity<List<${className}Entity>> getList();
	
	public ${className}Entity getById(String id);
	
	public ResponseEntity<${className}Response> addValue(${className} request);
	
	public ResponseEntity<${className}Response> updateValue(String id, ${className} request);
	
	public ResponseEntity<${className}Response> deleteValue(String id);
		
}