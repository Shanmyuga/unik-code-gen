<div class="form-group form-inline">
    <label class="col-form-label">{ <Translate  value="app.${htmlID}" /> }</label> &nbsp;
    <input name="${htmlID}" type="text" class="form-control" component={renderField} 
	
	<#if label??>placeholder="Enter ${label}"</#if>
	
	<#if readonly?? && readonly=="true">
		readOnly
	</#if> />
</div>

