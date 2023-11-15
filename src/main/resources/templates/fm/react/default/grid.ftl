<Grid bsClass="form-group">
	<#list children as row>
		<Row>
		<#list row as col>
			<Col xs={12} md={${col.md}}>
				${col.elemContent}
			</Col>	
		</#list>
		</Row>
	</#list>
</Grid>