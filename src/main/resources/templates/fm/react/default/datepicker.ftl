<FormGroup controlId="${label_stripWhiteSpace}">
	<ControlLabel><Translate value="app.${htmlID}" /></ControlLabel> {' '}
		<div>
		<DatePicker className="form-control" ${otherAttributes} name="${htmlID}"
            selected={this.state.${htmlID}}
        />
	</div>
</FormGroup>