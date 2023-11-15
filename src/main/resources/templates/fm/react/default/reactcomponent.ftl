import React from 'react';
<#assign schemaName = component.componentName?uncap_first>
import { <#if component.validationVariables??>${schemaName}Schema, </#if>validator } from '../utils/validationUtils';
import { renderField } from './renderField';

<#if formDetail.importMap??>
<#list formDetail.importMap?keys as packageName>
<#if packageName == "files">
<#list formDetail.importMap[packageName].fileNameList as fileName>
import '${fileName}';
</#list>
<#else><#if packageName == "react-datepicker">
import <#list formDetail.importMap[packageName].importClassList as impClass>${impClass}<#if impClass?has_next>, </#if></#list> from '${packageName}';
<#else>
import { <#list formDetail.importMap[packageName].importClassList as impClass>${impClass}<#if impClass?has_next>, </#if></#list> } from '${packageName}';
</#if>
</#if>
</#list>
</#if>

class ${label_stripWhiteSpace} extends React.Component {
	constructor(props) {
        super(props)
        this.state = {
        <#if stateVariables??>
			<#list stateVariables as stateVar>
	            ${stateVar.key}: ${stateVar.value}<#if stateVar?has_next>,</#if>
    		</#list>
    	</#if>
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
      		${childComponent}
      	);
   	}
}

<#if component.validationVariables??>
const validate = validator(${schemaName}Schema);
</#if>
export default reduxForm({
	form: '${formName}'<#if component.validationVariables??>,</#if>
	<#if component.validationVariables??>validate</#if>
})(${label_stripWhiteSpace});