<FormGroup controlId="${label_stripWhiteSpace}" className="form-inline">
	<ControlLabel><Translate value="app.${htmlID}" /></ControlLabel> {' '}
	<div className="datepicker-inline">
		<DatePicker className="form-control" ${otherAttributes} name="${htmlID}"
            selected={this.state.${htmlID}}
        />
	</div>
</FormGroup>