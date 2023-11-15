const translationsObject = {
	<#if languages?? && reactComponents??>
	  <#list languages?keys as lang>
	  ${lang}: {
	  	app: {
	  		<#list reactComponents?keys as compName>
	  		<#if reactComponents[compName].allVariables??>
				<#list reactComponents[compName].allVariables as variable>
					<#if variable.key?? && variable.label??>
	  				${variable.key}: '${variable.label}<#if lang != 'en'>_${lang}</#if>'<#if variable?has_next>,<#else><#if compName?has_next>,</#if></#if>
	  				</#if>
	  			</#list>
	  		</#if>
	  		</#list>
	  	}
	  }<#if lang?has_next>,</#if>
	  </#list>
	</#if>
};

export default translationsObject;