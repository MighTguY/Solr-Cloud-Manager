import React, { Component } from "react";
import { Nav, NavDropdown } from "react-bootstrap";
import { NavLink } from "react-router-dom";

class AdminNavbarLinks extends Component {
    render() {
        return (
            <div>
                <Nav>
                    <NavDropdown title="Services" id="basic-nav-dropdown">
                        <NavDropdown.Item>
                            <NavLink
                                to="/admin/serviceone"
                                className="nav-link"
                                activeClassName="active"
                            >
                                API Service 1
                    </NavLink>
                        </NavDropdown.Item>
                        <NavDropdown.Item>
                            <NavLink
                                to="/admin/servicetwo"
                                className="nav-link"
                                activeClassName="active"
                            >
                                API Service 2
                    </NavLink>
                        </NavDropdown.Item>
                        <NavDropdown.Item>
                            <NavLink
                                to="/admin/servicethree"
                                className="nav-link"
                                activeClassName="active"
                            >
                                API Service 3
                    </NavLink>
                        </NavDropdown.Item>
                        <NavDropdown.Item>
                            <NavLink
                                to="/admin/servicefour"
                                className="nav-link"
                                activeClassName="active"
                            >
                                API Service 4
                    </NavLink>
                        </NavDropdown.Item>
                        <NavDropdown.Item>
                            <NavLink
                                to="/admin/servicefive"
                                className="nav-link"
                                activeClassName="active"
                            >
                                API Service 5
                    </NavLink>
                        </NavDropdown.Item>
                    </NavDropdown>
                </Nav>
                <Nav pullRight>
                //Other NavDropdowns
                </Nav>
            </div>
        );
    }
}

export default AdminNavbarLinks;