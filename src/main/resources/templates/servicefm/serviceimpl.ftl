package ${package}.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ${package}.response.${className}Response;
import ${package}.entity.${className}Entity;
import ${package}.service.${className}Service;
import ${package}.dto.${className};
import ${package}.dao.impl.${className}DAOImpl;

@Service
public class ${className}ServiceImpl implements ${className}Service{
	
	@Autowired
	private ${className}DAOImpl ${classReferenceName}DAOImpl;
	
	@Autowired
	${className}Response ${classReferenceName}Response;
	
	<#if genLoginScreen == "true">
	public Boolean loginAuthetication(String id){
		return ${classReferenceName}DAOImpl.loginAuthetication(id);
	}
	</#if>
	
	public ResponseEntity<List<${className}Entity>> getList(){
		List<${className}Entity> responseList = new ArrayList<>();
		responseList = ${classReferenceName}DAOImpl.getResponseList();
		if (responseList.isEmpty()) {
		    return new ResponseEntity<List<${className}Entity>>(HttpStatus.NO_CONTENT);
        }
	    return new ResponseEntity<List<${className}Entity>>(responseList, HttpStatus.OK);
	}
	
	public ${className}Entity getById(String id){
		return ${classReferenceName}DAOImpl.getById(id);
	}
	
	public ResponseEntity<${className}Response> addValue(${className} request){
		${className}Entity entity=new ${className}Entity();
		<#list member as variable>   
		entity.set${variable.get("methodName").getAsString()}(request.get${variable.get("methodName").getAsString()}());
		</#list>
		${classReferenceName}Response = ${classReferenceName}DAOImpl.addValue(entity);
	    return new ResponseEntity<${className}Response>(${classReferenceName}Response, HttpStatus.OK);
	}
	
	public ResponseEntity<${className}Response> updateValue(String id, ${className} request) {
		${className}Entity entity=new ${className}Entity();
		<#list member as variable>   
		entity.set${variable.get("methodName").getAsString()}(request.get${variable.get("methodName").getAsString()}());
		</#list>
		${classReferenceName}Response = ${classReferenceName}DAOImpl.updateValue(entity);
	    return new ResponseEntity<${className}Response>(${classReferenceName}Response, HttpStatus.OK);
	}
	
	public ResponseEntity<${className}Response> deleteValue(String id) {
		${classReferenceName}Response = ${classReferenceName}DAOImpl.deleteValue(id);
	    return new ResponseEntity<${className}Response>(${classReferenceName}Response, HttpStatus.OK);
	}
	
}