import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Nav, Navbar, NavItem, NavDropdown, MenuItem  } from 'react-bootstrap';

class LeftNavBar extends Component {
    constructor(props) {
      super(props);
    }

    render() {
        return (
     
      			

  <Nav>
  	  <#list routerComponents as router>
        	  <#if router.containerRoute != "">
             
                  <li  role="presentation">
                    <Link to="/${router.containerRoute}">
                        <i className="fa fa-dashboard fa-fw"></i> ${router.sectionName}
                    </Link>
                  </li>
             
              </#if>
              </#list>
   	  <NavDropdown eventKey={1} title="Deal">
      <MenuItem eventKey={1.1}>Create</MenuItem>
      <MenuItem eventKey={1.2}>Reactive</MenuItem>
      <MenuItem eventKey={1.3}>Search</MenuItem>
      </NavDropdown>
    <NavDropdown eventKey={2} title="Guarantees">
      <MenuItem eventKey={2.1}>Create</MenuItem>
      <MenuItem eventKey={2.2}>Reactive</MenuItem>
      <MenuItem eventKey={2.3}>Search</MenuItem>
     </NavDropdown>
     	  <NavDropdown eventKey={3} title="Guarantee Groups">
      <MenuItem eventKey={3.1}>Create</MenuItem>
      <MenuItem eventKey={3.2}>Search</MenuItem>
      </NavDropdown>
    <NavDropdown eventKey={4} title="List">
      <MenuItem eventKey={4.1}>Create</MenuItem>
      <MenuItem eventKey={4.2}>Search</MenuItem>
     </NavDropdown>
     	  <NavDropdown eventKey={5} title="Reports">
      <MenuItem eventKey={5.1}>Create Settlement Report</MenuItem>
      <MenuItem eventKey={5.2}>Search Settlement Report</MenuItem>
      <MenuItem eventKey={5.3}>Business Objects 4</MenuItem>
      </NavDropdown>
    <NavItem eventKey={8} href="/#">
      Sign Out
    </NavItem>
  </Nav>

            
        );
    }
}

export default LeftNavBar;
