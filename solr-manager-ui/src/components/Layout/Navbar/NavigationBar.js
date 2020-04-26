import React from 'react'
import { Navbar, NavDropdown, Nav, Form,  Button } from 'react-bootstrap';
import { faHome, faServer, faSitemap, faMagic, faClone, faLifeRing, faCloud, faTags, faArrowsAltH, faCamera, faRecycle, faPowerOff } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Aux from '../../../hoc/Aux';

const NavigationBar = (props) => {
    const ZookeeperDropDownIcon = (<><FontAwesomeIcon icon={faHome} fixedWidth /> Zookeeper</>)
    const CollectionDropDownIcon = (<><FontAwesomeIcon icon={faServer} fixedWidth /> Collections</>)
    const MoreDropDownIcon = (<><FontAwesomeIcon icon={faMagic} fixedWidth /> More</>)
    const RefreshropDownIcon = (<><FontAwesomeIcon icon={faRecycle} fixedWidth /> refresh</>)
    var status = "";
    return (
        <Aux>
            <Navbar bg="light" expand="lg" variant="light" className={"navbar-" + (status)}>
                <Navbar.Brand href="#home">Solr Manager UI</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link href="#overview"><FontAwesomeIcon icon={faHome} fixedWidth /> Dashboard</Nav.Link>
                        <Nav.Link href="#nodes"><FontAwesomeIcon icon={faSitemap} fixedWidth /> Cloud Manager</Nav.Link>

                        <NavDropdown title={ZookeeperDropDownIcon} id="basic-nav-dropdown">
                            <NavDropdown.Item   href="#action/3.1"><FontAwesomeIcon icon={faClone} fixedWidth /> Zookeeper Files</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2"><FontAwesomeIcon icon={faLifeRing} fixedWidth />Zookeeper Status</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title={CollectionDropDownIcon} id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1"><FontAwesomeIcon icon={faCloud} /> Collection A</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2"><FontAwesomeIcon icon={faCloud} /> Collection B</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title={MoreDropDownIcon} id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1"><FontAwesomeIcon icon={faTags} /> Aliases</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2"><FontAwesomeIcon icon={faArrowsAltH} /> CDCR</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3"><FontAwesomeIcon icon={faCamera} /> BackupRestore</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav className="navbar-right">
                        <NavDropdown title={RefreshropDownIcon} id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">5</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">10</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3">15</NavDropdown.Item>
                        </NavDropdown>
                        <Form inline>
                            <Button><FontAwesomeIcon icon={faPowerOff} fixedWidth /> </Button>
                        </Form>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </Aux>
    );
}

export default NavigationBar;