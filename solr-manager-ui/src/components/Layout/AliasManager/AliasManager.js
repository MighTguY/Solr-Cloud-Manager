import React, { Component } from 'react'
import { Accordion, Card, Button, Row, Container, Col } from 'react-bootstrap'
import StatsCard from '../../Helper/StatsCard/StatsCard'


export default class AliasManager extends Component {
    render() {
       
        return (
            <Container fluid className="col-lg-12 zk-component">
                <h5 className="text-success">Alias Manager </h5>
                <Row className="justify-content-md-center">
                    <Col lg={4} sm={6}>
                        <StatsCard
                            bigIcon={<i className="fa fa-tags fw text-sucess" />}
                            statsText="Aliases"
                            statsValue="1"
                            statsIcon={<i className="fa fa-tags fw" />}
                            hintText="Number of aliases"
                        />
                    </Col>
                    <Col lg={4} sm={6}>
                        <StatsCard
                            bigIcon={<i className="fa fa-book text-success" />}
                            statsText="Collections"
                            statsValue="1"
                            statsIcon={<i className="fa fa-object-group" />}
                            hintText="Number of Collections"
                        />
                    </Col>

                </Row>
                <Accordion defaultActiveKey="0">
                    <Card >
                        <Card.Header>
                            <Accordion.Toggle as={Button} variant="link" eventKey="0"><h5 className="text-primary">Current Aliases <i className="fa fa-gear fw" /></h5></Accordion.Toggle>
                        </Card.Header>
                        <Card.Body>
                            
                            <Accordion.Collapse eventKey="0">
                                <Row className="justify-content-md-center">
                                </Row>
                            </Accordion.Collapse>
                        </Card.Body>
                    </Card>
                </Accordion>
                <Accordion defaultActiveKey="0">
                    <Card >
                        <Card.Header>
                            <Accordion.Toggle as={Button} variant="link" eventKey="0"><h5 className="text-danger">Alias Manager <i className="fa fa-gear fw" /></h5></Accordion.Toggle>
                        </Card.Header>
                        <Card.Body>
                            <Accordion.Collapse eventKey="0">
                                <Row className="justify-content-md-center">
                                    Hello how are you I am fine bros
                            </Row>
                            </Accordion.Collapse>
                        </Card.Body>
                    </Card>
                </Accordion>
            </Container>
        )
    }
}
