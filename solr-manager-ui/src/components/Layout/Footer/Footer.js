import React, { Component } from "react";
import { Container } from "react-bootstrap";


class Footer extends Component {
    render() {
        return (
            <footer className="py-4 bg-dark text-white-50">
                <Container fluid>
                   
                    <p className="copyright pull-right">
                        &copy; {new Date().getFullYear()}{" "}
                        
                            Lucky Sharma
           
              , Solr Managed UI
            </p>
                </Container>
            </footer>
        );
    }
}

export default Footer;