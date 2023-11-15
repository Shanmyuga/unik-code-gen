import React from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

import ${componentName} from '${componentFileName}';
import { actions } from '${actionFileName}';

import Header from '../component/header'
import LeftNavBar from '../component/leftNavBar';
import Footer from '../component/footer';

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
              	<Header />
				<LeftNavBar />
					
				<div className="w3-main w3-margin-left-layout">
					<div class="w3-row w3-padding-64">
					
				    	<div className="w3-twothird w3-container">
				            <${componentName} updateState={this.updateState} />
			          	</div>
		          	</div>
		          	
		          	<Footer />
		      	</div>
	      	</div>
	    </#if>  

		<#if page == "Login">		                  
        	<${componentName} updateState={this.updateState} />
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
