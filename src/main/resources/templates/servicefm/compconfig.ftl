package ${package}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ${package}.controller.${className}Controller;
import ${package}.dao.impl.${className}DAOImpl;
import ${package}.response.${className}Response;
import ${package}.service.impl.${className}ServiceImpl;
//imports

@Configuration
public class ComponentConfiguration{
	
	<#list compConfigBeanList as beanName> 
	@Bean
	public ${beanName.compConfigBeanName.getAsString()} ${beanName.compConfigMethodName.getAsString()}(){
		return new ${beanName.compConfigBeanName.getAsString()}();
	}
	</#list>
//todo
}