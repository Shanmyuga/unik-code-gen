package ${package}.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ${package}.service.impl.${className}ServiceImpl;
import ${package}.response.${className}Response;
import ${package}.dto.${className};
import ${package}.entity.${className}Entity;

@RestController
public class ${className}Controller {

	@Autowired
	private ${className}ServiceImpl ${classReferenceName}ServiceImpl;
	
	<#if genLoginScreen == "true">
	@RequestMapping("/${requestUrl}/{id}")
	public Boolean loginAuthetication(@PathVariable String id){
		return ${classReferenceName}ServiceImpl.loginAuthetication(id);
	}
	</#if>
	
	@RequestMapping("/${requestUrl}")
	public ResponseEntity<List<${className}Entity>> getList(){
		return ${classReferenceName}ServiceImpl.getList();
	}
	
	@RequestMapping("/${requestUrl}/{id}")
	public ${className}Entity getById(@PathVariable String id){
		return ${classReferenceName}ServiceImpl.getById(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/${requestUrl}")
	public ResponseEntity<${className}Response> addValue(@RequestBody ${className} request){
		return ${classReferenceName}ServiceImpl.addValue(request);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/${requestUrl}/{id}")
	public ResponseEntity<${className}Response> updateValue(@PathVariable String id, @RequestBody ${className} request){
		return ${classReferenceName}ServiceImpl.updateValue(id, request);
	}
	
	@RequestMapping(method=RequestMethod.DELETE , value="/${requestUrl}/{id}")
	public ResponseEntity<${className}Response> deleteValue(@PathVariable String id){
		return ${classReferenceName}ServiceImpl.deleteValue(id);
	}
}