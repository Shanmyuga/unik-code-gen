package ${package}.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ${package}.dao.${className}DAO;
import ${package}.entity.${className}Entity;
import ${package}.repository.${className}Repository;
import ${package}.response.${className}Response;

public class ${className}DAOImpl implements ${className}DAO{

	@Autowired
	private ${className}Repository ${classReferenceName}Repository;
	
	@Autowired
	${className}Response ${classReferenceName}Response;
	
	<#if genLoginScreen == "true">
	public Boolean loginAuthetication(String id){
		return true;
	}
	</#if>
	
	public ${className}Entity getById(String id){
		return ${classReferenceName}Repository.findOne(id);
	}
	
	public List<${className}Entity> getResponseList(){
		List<${className}Entity> responseList = new ArrayList<>();
		${classReferenceName}Repository.findAll().forEach(responseList::add);
		return responseList;
	}
	
	public ${className}Response addValue(${className}Entity entity){
		${classReferenceName}Repository.save(entity);
		${classReferenceName}Response.setStatus("OK");
		${classReferenceName}Response.setStatusCode("200");
		${classReferenceName}Response.setStatusMessage("Successfully Added");
		return ${classReferenceName}Response;
	}
	
	public ${className}Response updateValue(${className}Entity entity){
		${classReferenceName}Repository.save(entity);
		${classReferenceName}Response.setStatus("OK");
		${classReferenceName}Response.setStatusCode("200");
		${classReferenceName}Response.setStatusMessage("Successfully Updated");
		return ${classReferenceName}Response;
	}

	public ${className}Response deleteValue(String id){
		${classReferenceName}Repository.delete(id);
		${classReferenceName}Response.setStatus("OK");
		${classReferenceName}Response.setStatusCode("200");
		${classReferenceName}Response.setStatusMessage("deleted");
		return ${classReferenceName}Response;
	}
}