<FormGroup controlId="${label_stripWhiteSpace}">
      <ControlLabel>${label}</ControlLabel> {' '}
      <FormControl <#if readOnly??>${readOnly}</#if> componentClass="textarea" placeholder="${label}" />
</FormGroup>