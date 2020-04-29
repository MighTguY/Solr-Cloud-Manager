import React, { Component } from 'react'
import { Navbar, NavDropdown, Nav, Form, Button } from 'react-bootstrap';
import { NavLink } from "react-router-dom";
import Aux from '../../../hoc/Aux';

export default class NavigationBar extends Component {

    generateNav = (routes, isDropDown) => {

        return routes.map((prop, key) => {
            if (prop.type === "inline") {
                if (!isDropDown) {
                    return (
                        <NavLink
                            to={prop.layout + prop.path}
                            className="nav-link"
                            activeClassName="active"
                            key={prop.id}>
                            <i className={prop.icon} /> {prop.name}
                        </NavLink>
                    );
                } else {
                    return (
                        <NavDropdown.Item
                        key={prop.id}>
                        <NavLink
                            to={prop.layout + prop.path}
                            className="nav-link"
                            activeClassName="active"
                            key={prop.id}>
                        <i className={prop.icon} /> {prop.name}
                        </NavLink>
                        </NavDropdown.Item>
                    )
                }
            } else if (prop.type === "dropdown") {
                let titleICON = (<><i className={prop.icon} /> {prop.name}</>)
                return (
                    <NavDropdown    title={titleICON} id="basic-nav-dropdown" key={prop.id}>
                        {this.generateNav(prop.routes, true)}
                    </NavDropdown>
                );
            } else
                return null;
        });
    };

    render() {
        const RefreshropDownIcon = (<><i className="fa fa-recycle fw" /> refresh</>)
        var status = "";
        var a = this.generateNav(this.props.routes, false);
        console.log('a: ', a);
        return (
            <Aux>
                <Navbar bg="light" expand="lg" variant="light" className={"navbar-" + (status)}>
                    <Navbar.Brand href="/">Solr Manager UI</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto">
                            {this.generateNav(this.props.routes, false)}
                        </Nav>
                        <Nav className="navbar-right">
                            <NavDropdown title={RefreshropDownIcon} id="basic-nav-dropdown">
                                <NavDropdown.Item href="#action/3.1">5</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.2">10</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.3">15</NavDropdown.Item>
                            </NavDropdown>
                            <Form inline>
                                <Button><i className="fa fa-power-off fw" /> </Button>
                            </Form>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            </Aux>
        );
    }
}
