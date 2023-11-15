<#if panelType?? && panelType == "childTab">
			<#list children as row>
				<#list row as col>
					${col.elemContent}
				</#list>
			</#list>
<#else>
<PanelGroup accordion id="${htmlID}">
	<Panel eventKey="${id}" bsStyle="primary">
		<Panel.Heading>
			<Panel.Title componentClass="h3"><Translate value="app.${htmlID}" /></Panel.Title>
		</Panel.Heading>
		<Panel.Body>
			<#list children as row>
				<#list row as col>
					${col.elemContent}
				</#list>
			</#list>
		</Panel.Body>
	</Panel>
</PanelGroup>
</#if>