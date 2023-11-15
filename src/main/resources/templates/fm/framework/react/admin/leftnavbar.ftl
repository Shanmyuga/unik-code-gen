import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Nav } from 'react-bootstrap';

class LeftNavBar extends Component {
    constructor(props) {
      super(props);
    }

    render() {
        return (
        		<nav className="main-menu">
		<ul>
			<li>
				<a href="index.html">
					<i className="fa fa-home nav_icon"></i>
					<span className="nav-text">
					Dashboard
					</span>
				</a>
			</li>

			<li className="has-subnav">
				<a href="javascript:;">
				<i className="fa fa-cogs" aria-hidden="true"></i>
				<span className="nav-text">
					Modules
				</span>
				<i className="icon-angle-right"></i><i className="icon-angle-down"></i>
				</a>
				<ul>

        	  <#list routerComponents as router>
        	  <#if router.containerRoute != "">
					<li>
					<Link className="subnav-text" to="/${router.containerRoute}">
                        ${router.sectionName}
                    </Link>
					</li>
              </#if>
              </#list>

				</ul>
			</li>
		</ul>
		<ul className="logout">
			<li>
			<a href="login.html">
			<i className="icon-off nav-icon"></i>
			<span className="nav-text">
			Logout
			</span>
			</a>
			</li>
		</ul>
		</nav>		
        );
    }
}

export default LeftNavBar;
