import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Nav } from 'react-bootstrap';

class LeftNavBar extends Component {
    constructor(props) {
      super(props);
    }

	w3MenuClose() {
		w3_close();
	}
	
    render() {
        return (
        <div>
	        <nav className="w3-sidebar w3-bar-block w3-collapse w3-large w3-theme-l5 w3-animate-left" id="mySidebar">
		        <a href="javascript:void(0)" onClick={this.w3MenuClose} className="w3-right w3-xlarge w3-padding-large w3-hover-black w3-hide-large" title="Close Menu">
	    			<i className="fa fa-remove"></i>
	  			</a>
				
				<h4 className="w3-bar-item"><b>Menu</b></h4>
	        
	        <#list routerComponents as router>
	        <#if router.containerRoute != "">
	        	<a className="w3-bar-item w3-button w3-hover-black" href="#"><i className="fa fa-dashboard fa-fw"></i>${router.sectionName}</a>
			</#if>
            </#list>
			</nav>

			<div className="w3-overlay w3-hide-large w3-cursor-pointer" onclick="w3_close()" title="close side menu" id="myOverlay"></div>
		</div>
		
        );
    }
}

export default LeftNavBar;
