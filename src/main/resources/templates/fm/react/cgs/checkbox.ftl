<#if styleType == "type1">
<Field name="${htmlID}" type="checkbox" component={renderField} label={ <Translate value="app.${htmlID}" /> } 
		<#if label??>placeholder="Enter ${label}"</#if>
		<#if readonly?? && readonly=="true">
		readOnly
		</#if>>
	
	<#list componentOptions as option>
    <label className="checkbox-inline"><input name="${htmlID}" type="checkbox" /><Translate value="app.${option.htmlID}" /></label>
	</#list>
</Field>		
</#if>

<#if styleType == "type2">
<Field name="${htmlID}" type="checkbox" component={renderField} label={ <Translate value="app.${htmlID}" /> } 
		<#if label??>placeholder="Enter ${label}"</#if>
		<#if readonly?? && readonly=="true">
		readOnly
		</#if>>
	
	<#list componentOptions as option>
    <label className="checkbox checkbox-columns"><input name="${htmlID}" type="checkbox" /><Translate value="app.${option.htmlID}" /></label>
	</#list>
</Field>		
</#if>