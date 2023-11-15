<Field name="${htmlID}" type="text" component={renderField} label={ <Translate value="app.${htmlID}" /> } 
		<#if label??>placeholder="Enter ${label}"</#if>
		<#if readonly?? && readonly=="true">
		readOnly
		</#if> 
		/>