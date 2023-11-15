import React from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

import ${componentName} from '${componentFileName}';
import { actions } from '${actionFileName}';

class ${containerName} extends React.Component {

  constructor(props) {
    super(props);
 
 	this.state =  {
 	};
 
    this.handleSubmit = this.handleSubmit.bind(this);
    this.updateState = this.updateState.bind(this);
  }

  componentDidMount() {
  	<#if page == "Login">
		sessionStorage.setItem('authenticated', false);
	</#if>	
  }

  updateState(args) {
  		this.setState(args);
  }

  handleSubmit(event) {
      this.props.actions.${actionMethodName}();
      <#if page == "Login">
      		this.setState({"login": "true"});
      </#if> 
  }

<#if page == "Login">
  componentDidUpdate() {
      const { data } = this.props;

      // comment for Login functionality
      //if (data) {
      if (true) {
        const location = {
        	// ReplaceWithFirstPath
        };

        this.props.history.push(location);
        sessionStorage.setItem('authenticated', true);
      } else {
      	sessionStorage.setItem('authenticated', false);
      }
  }
</#if>

  render() {
    return (
		<#if page != "Login">	
    	  <div>
	          <div id="page-wrapper">
		          <div className="row">
		              <div className="col-lg-12">
		</#if>              
		                  <${componentName} onSubmit={this.handleSubmit} />
		<#if page != "Login">		                  
		              </div>
		          </div>
	          </div>
	      </div>
	    </#if>  
    );
  }
}

function mapDispatchToProps (dispatch) {
  return {
      actions: bindActionCreators( actions, dispatch)
  };
}

function mapStateToProps (state) {
  const { data } = state.${reducerName?uncap_first};
  return { data };
}

export default connect(mapStateToProps, mapDispatchToProps)(${containerName});
