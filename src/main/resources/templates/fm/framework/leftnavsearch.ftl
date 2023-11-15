import React, { Component } from 'react';
import { Navbar, NavbarBrand, NavbarToggle, NavbarCollapse, Nav, NavItem } from 'react-bootstrap';
import { FormGroup, FormControl, Button } from 'react-bootstrap';

class LeftNavSearch extends Component {
    render() {
        return (
                <ul className="nav" id="side-menu">
                    <li className="sidebar-search">
                        <div className="input-group custom-search-form">
                            <FormControl type="text" placeholder="Search..."/>
                            <span className="input-group-btn">
                                <Button><i className="fa fa-search"></i></Button>
                            </span>
                        </div>
                    </li>
                </ul>
        );
    }
}

export default LeftNavSearch;
