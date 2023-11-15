import React from 'react';

${imports}

class ${label_stripWhiteSpace} extends React.Component {
	constructor(props) {
        super(props)
        this.state = {
			<#list stateVariables as stateVar>
	            ${stateVar.key}: ${stateVar.value}<#if stateVar?has_next>,</#if>
    		</#list>
        };
		
        this.handleChange = this.handleChange.bind(this);
    }
    
    getValidationState(propId) {
    	let length = -1;
    	if (this.state[propId] != null) {
    		length = this.state[propId].length;

			<#if mandatory?? && mandatory=="true">
	    	if (length <= 0) { 
	    		return 'error';
	    	}
	    	</#if>
	    	
	    	<#if minLength??>
	    	if (length < ${minLength}) { 
	    		return 'error';
	    	}
	    	</#if>
	    	
	    	<#if maxLength??>
	    	if (length > ${maxLength}) { 
	    		return 'error';
	    	}
	    	</#if>
	    	
	    	return 'success';
    	} else {
    		return null;
    	}
  	}

	handleChange(event) {
	    this.setState({
	        [event.target.id]: event.target.value
	    });
	    
	    this.props.updateState(this.state);
	}
    
	render() {
    	return (
      		${childComponent}
      	);
   	}
}

export default ${label_stripWhiteSpace};