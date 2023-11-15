<div class="form-group form-inline">
    <label class="col-form-label">{ <Translate value="app.${htmlID}" /> }</label>
    <select  class="form-control" name="${htmlID}" component={renderField} 
      <#if readonly?? && readonly=="true">
		readOnly
	  </#if>>
	  
	  <#list componentOptions as option>
      <option value="${option.key}">${option.value}</option>
	  </#list>
    </select>    
</div>