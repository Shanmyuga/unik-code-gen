<FormGroup controlId="${label_stripWhiteSpace}">
	<ControlLabel>${label}</ControlLabel> {' '}
		<div>
		<#list componentOptions as option>
			<Radio name="${htmlID}" inline>${option.value}</Radio>
		</#list>
	</div>
</FormGroup>