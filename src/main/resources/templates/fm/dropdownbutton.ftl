<FormGroup controlId="${label_stripWhiteSpace}">
	<ControlLabel>${label}</ControlLabel> {' '}
	<div>
		<DropdownButton ${otherAttributes} bsStyle={"default"} title={"${label}"}
								  key={1}
								  id={"split-button-basic"}>
			<#list componentOptions as option>
				<MenuItem eventKey={${option?index}}>${option.value}</MenuItem>
			</#list>
		</DropdownButton>
	</div>
</FormGroup>