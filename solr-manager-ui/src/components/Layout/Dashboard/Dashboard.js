import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import StatsCard from '../StatsCard/StatsCard';
import ClusterTable from '../ClusterTable/ClusterTable';
import 'react-bootstrap-table2-toolkit/dist/react-bootstrap-table2-toolkit.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';

const Dashboard = (props) => {
    return (
        <Container fluid>
            <Row className="justify-content-md-center">
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-sitemap text-warning" />}
                        statsText="Nodes"
                        statsValue="1 Node"
                        statsIcon={<i className="fa fa-refresh" />}
                        statsIconText="Updated now"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-book text-success" />}
                        statsText="Indices"
                        statsValue="4"
                        statsIcon={<i className="fa fa-calendar-o" />}
                        statsIconText="Last day"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-braille text-danger" />}
                        statsText="Shards"
                        statsValue="23"
                        statsIcon={<i className="fa fa-clock-o" />}
                        statsIconText="In the last hour"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-files-o text-info" />}
                        statsText="Docs"
                        statsValue="+45"
                        statsIcon={<i className="fa fa-refresh" />}
                        statsIconText="Updated now"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-download text-info" />}
                        statsText="Size"
                        statsValue="400 MB"
                        statsIcon={<i className="fa fa-refresh" />}
                        statsIconText="Updated now"
                    />
                </Col>
            </Row>

                <Row className="justify-content-md-center"> 
                    <ClusterTable />  
                </Row>
        </Container>
    );
}

export default Dashboard