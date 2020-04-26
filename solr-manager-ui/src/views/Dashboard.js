import React, { Component } from "react";
import { Row, Col, Container } from "react-bootstrap";
import { StatsCard } from "components/StatsCard/StatsCard.jsx";
class Dashboard extends Component {
    
    render() {
        return (
            <div className="content">
                <Container fluid>
                    <Row>
                        <Col lg={3} sm={6}>
                            <StatsCard
                                bigIcon={<i className="pe-7s-server text-warning" />}
                                statsText="Capacity"
                                statsValue="105GB"
                                statsIcon={<i className="fa fa-refresh" />}
                                statsIconText="Updated now"
                            />
                        </Col>
                        <Col lg={3} sm={6}>
                            <StatsCard
                                bigIcon={<i className="pe-7s-wallet text-success" />}
                                statsText="Revenue"
                                statsValue="$1,345"
                                statsIcon={<i className="fa fa-calendar-o" />}
                                statsIconText="Last day"
                            />
                        </Col>
                        <Col lg={3} sm={6}>
                            <StatsCard
                                bigIcon={<i className="pe-7s-graph1 text-danger" />}
                                statsText="Errors"
                                statsValue="23"
                                statsIcon={<i className="fa fa-clock-o" />}
                                statsIconText="In the last hour"
                            />
                        </Col>
                        <Col lg={3} sm={6}>
                            <StatsCard
                                bigIcon={<i className="fa fa-twitter text-info" />}
                                statsText="Followers"
                                statsValue="+45"
                                statsIcon={<i className="fa fa-refresh" />}
                                statsIconText="Updated now"
                            />
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default Dashboard;