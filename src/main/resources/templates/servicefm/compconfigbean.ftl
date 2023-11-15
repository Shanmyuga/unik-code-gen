<#list compConfigBeanList as beanName> 
	@Bean
	public ${beanName.compConfigBeanName.getAsString()} ${beanName.compConfigMethodName.getAsString()}(){
		return new ${beanName.compConfigBeanName.getAsString()}();
	}
</#list>
//todo
