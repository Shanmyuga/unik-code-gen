<PanelGroup accordion id="${htmlID}">
	<Panel eventKey="${id}" ${otherAttributes}>
		<Panel.Heading>
			<Panel.Title componentClass="h3">${label}</Panel.Title>
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