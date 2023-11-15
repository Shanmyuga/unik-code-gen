import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class Footer extends Component {
    render() {
        return (
			<footer id="myFooter">
			    <div class="w3-container w3-theme-l2 w3-padding-32">
			      <h4>Footer</h4>
			    </div>
			
			    <div class="w3-container w3-theme-l1">
			      <p>Powered by <a href="https://www.w3schools.com/w3css/default.asp" target="_blank">Cognizant</a></p>
			    </div>
		  	</footer>
        );
    }
}

export default Footer;
