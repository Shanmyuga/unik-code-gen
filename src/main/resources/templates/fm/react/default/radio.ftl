<Field name="${htmlID}" type="radio" component={renderField} label={ <Translate value="app.${htmlID}" /> }
		<#if readonly?? && readonly=="true">
		readOnly
		</#if>>
  	<#list componentOptions as option>
    <label className="radio-inline"><input name="${htmlID}" type="radio" /><Translate value="app.${option.htmlID}" /></label>
	</#list>
</Field>    