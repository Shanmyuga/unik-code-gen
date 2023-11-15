package ${package}.dao;

import java.util.List;

import ${package}.entity.${className}Entity;
import ${package}.response.${className}Response;

public interface ${className}DAO{

	<#if genLoginScreen == "true">
	public Boolean loginAuthetication(String id);
	</#if>
	
	public ${className}Entity getById(String id);
	
	public List<${className}Entity> getResponseList();
	
	public ${className}Response addValue(${className}Entity entity);
	
	public ${className}Response updateValue(${className}Entity entity);
	
	public ${className}Response deleteValue(String id);
}