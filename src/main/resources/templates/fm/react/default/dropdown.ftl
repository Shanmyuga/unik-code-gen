<Field name="${htmlID}" type="dropdown" component={renderField} label={ <Translate value="app.${htmlID}" /> } 
		<#if readonly?? && readonly=="true">
		readOnly
		</#if>>
  	<#list componentOptions as option>
    <option value="${option.key}">${option.value}</option>
	</#list>
</Field>    