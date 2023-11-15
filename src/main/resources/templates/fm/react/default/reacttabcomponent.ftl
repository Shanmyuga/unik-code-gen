import React from 'react';
import { reduxForm, Field } from 'redux-form';
import { renderField } from './renderField';
import { Translate } from 'react-redux-i18n';
import {PanelGroup, Panel, Tabs, Tab} from 'react-bootstrap';

<#list tabComp.tabCompMap?keys as key>
    import ${key} from './${key}';
</#list>

class ${tabComp.componentName} extends React.Component {
	constructor(props) {
        super(props)
        this.state = {
            
        };
        
        this.handleChange = this.handleChange.bind(this);
    }
    
    handleChange(event) {
        this.setState({
            [event.target.id]: event.target.value
        });
        
        this.props.updateState(this.state);
    }
    
	render() {
		const { handleSubmit, pristine, reset, submitting } = this.props;
		
    	return (
      		<form id="${tabComp.htmlID}" onSubmit={handleSubmit}>
            <PanelGroup accordion id="${tabComp.htmlID}">
                <Panel eventKey="4" bsStyle="primary"  panelType="single" >
                    <Panel.Heading>
                        <Panel.Title componentClass="h3">${tabComp.sectionName}</Panel.Title>
                    </Panel.Heading>
                    <Panel.Body>
                    <#assign count = 1>
                        <Tabs defaultActiveKey={1} id="uncontrolled-tab-example">
	                        <#list tabComp.tabCompMap?keys as key>
							    <Tab eventKey={${count}} title="${tabComp.tabCompMap[key]}">
	                                <${key} />
	                            </Tab>
	                            <#assign count = count + 1>
							</#list>
                        </Tabs>
                    </Panel.Body>
                </Panel>
            </PanelGroup>
        </form>
      	);
   	}
}

export default reduxForm({
	form: '${tabComp.htmlID}'
})(${tabComp.componentName});