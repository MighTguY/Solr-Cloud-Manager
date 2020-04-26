import React, { Component } from 'react'
import { Navbar } from "react-bootstrap";

import NavBarLinks from "./NavBarLinks.js";

export default class NavigationBar extends Component {
    render() {
        return (
            <Navbar fluid>
               
                    <Navbar.Brand>
                        <a href="#pablo">Hello World</a>
                    </Navbar.Brand>
                    <Navbar.Toggle />
            
                <Navbar.Collapse>
                    <NavBarLinks />
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
