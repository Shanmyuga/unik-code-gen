import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Navbar, NavbarBrand, NavbarToggle, NavbarCollapse } from 'react-bootstrap';

class Header extends Component {
    render() {
        return (
              <Navbar.Header>
                  <Navbar.Brand>
                      <Link to="/">Generated React Application</Link>
                  </Navbar.Brand>
                  <Navbar.Toggle />
              </Navbar.Header>
        );
    }
}

export default Header;
