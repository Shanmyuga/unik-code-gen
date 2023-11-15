<FormGroup controlId="${htmlID}" <#if mandatory?? && mandatory=="true">validationState={this.getValidationState("${htmlID}")}</#if>>
	<ControlLabel>${label}</ControlLabel> {' '}
	<div>
		<FormControl
			type="text"
			placeholder="Enter ${label}"
			onChange={this.handleChange}
			<#if mandatory?? && mandatory=="true">
			required
			</#if>
			<#if readonly?? && readonly=="true">
			readOnly
			</#if>
		  />
	</div>
</FormGroup>