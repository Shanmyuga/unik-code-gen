import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class Header extends Component {
	w3MenuOpen() {
		w3_open();
	}

    render() {
        return (
			<div className="w3-top">
				<div className="w3-bar w3-theme w3-top w3-left-align w3-large">
					<a className="w3-bar-item w3-button w3-right w3-hide-large w3-hover-white w3-large w3-theme-l1" href="javascript:void(0)" onClick={this.w3MenuOpen}><i class="fa fa-bars"></i></a>
					<a href="#" className="w3-bar-item w3-button w3-theme-l1">Generated React Application</a>
				</div>
			</div>
        );
    }
}

export default Header;
