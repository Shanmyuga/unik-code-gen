<#if formDetail.dataTableList??>
<FormGroup controlId="${label_stripWhiteSpace}">
  <ControlLabel><Translate value="app.${htmlID}" /></ControlLabel> {' '}	
  <BootstrapTable data={this.state.items}  striped hover condensed>
    <TableHeaderColumn dataField="id" isKey={ true } dataAlign="center">Id</TableHeaderColumn>
    <#list formDetail.dataTableList as colElem>
    <TableHeaderColumn dataField="${colElem.propValue}" >${colElem.displayColumnValue}</TableHeaderColumn>
    </#list>
  </BootstrapTable>
</FormGroup>
</#if>