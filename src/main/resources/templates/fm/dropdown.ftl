<FormGroup controlId="${label_stripWhiteSpace}">
      <ControlLabel>${label}</ControlLabel>
      
      <FormControl ${otherAttributes} componentClass="select" placeholder="select">
      	<#list componentOptions as option>
        <option value="${option.key}">${option.value}</option>
		</#list>
      </FormControl>
    </FormGroup>