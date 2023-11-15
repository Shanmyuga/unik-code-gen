import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Nav } from 'react-bootstrap';

class LeftNavBar extends Component {
    constructor(props) {
      super(props);
    }

    render() {
        return (
        	<div>
        	  <#list routerComponents as router>
        	  <#if router.containerRoute != "">
              <Nav id="side-menu">
                  <li  role="presentation">
                    <Link to="/${router.containerRoute}">
                        <i className="fa fa-dashboard fa-fw"></i> ${router.sectionName}
                    </Link>
                  </li>
              </Nav>
              </#if>
              </#list>
            </div>  
        );
    }
}

export default LeftNavBar;
