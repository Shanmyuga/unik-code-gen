import React from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

import ${componentName} from '${componentFileName}';
import { actions } from '${actionFileName}';

<#if page != "Login">
import Header from '../component/header'
import LeftNavBar from '../component/leftNavBar';
</#if>

class ${containerName} extends React.Component {

  constructor(props) {
    super(props);
 
 	this.state =  {
 	};
 
    this.handleSubmit = this.handleSubmit.bind(this);
    this.updateState = this.updateState.bind(this);
  }

  componentDidMount() {

  }

  updateState(args) {
  		this.setState(args);
  }

  handleSubmit(event) {
      this.props.actions.${actionMethodName}();
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
      }
  }
</#if>

  render() {
    return (
		<#if page != "Login">	
    	  <div>
	      	<LeftNavBar />
	     
	        <section className="wrapper scrollable">
		      	<Header />
			  
	          	<div className="main-grid">
		</#if>              
		                  <${componentName} updateState={this.updateState} />
		<#if page != "Login">		                  
	          </div>
	          
			  <div className="footer">
				  <p>© 2016 Colored . All Rights Reserved . Design by <a href="http://w3layouts.com/">W3layouts</a></p>
			  </div>
	      	</section>
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
  const { data } = state.${reducerName};
  return { data };
}

export default connect(mapStateToProps, mapDispatchToProps)(${containerName});
