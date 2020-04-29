import React from 'react';
import { Container, Row, Col, Card, Accordion, Button } from 'react-bootstrap';
import StatsCard from '../../Helper/StatsCard/StatsCard';
import ClusterTable from '../../Helper/ClusterTable/ClusterTable';
import 'react-bootstrap-table2-toolkit/dist/react-bootstrap-table2-toolkit.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import Solr from '../../../assets/svg/Solr.svg'
import { productsGenerator } from '../../tmp_utils/commons';

const Dashboard = (props) => {

    const products = productsGenerator(87);

    const columns = [{
        dataField: 'id',
        text: 'Node',
    }, {
        dataField: 'name',
        text: 'Collection Name',
        sort: true
    }, {
        dataField: 'price',
        text: 'Collection Size',
        sort: true,
        sortCaret: (order, column) => {
            if (!order) return (<span>&nbsp;&nbsp;<i className="fa fa-sort" /> </span>);
            else if (order === 'asc') return (<span>&nbsp;&nbsp;<i className="fa fa-sort-asc" /></span>);
            else if (order === 'desc') return (<span>&nbsp;&nbsp;<i className="fa fa-sort-desc" /></span>);
            return null;
        }
    }, {
        dataField: 'price',
        text: 'Index Shards',
    }, {
        dataField: 'price',
        text: 'Collection Health',
    }];

    const expandRow = {
        renderer: row => (
            <div>
                <p>{`This Expand row is belong to rowKey ${row.id}`}</p>
                <p>You can render anything here, also you can add additional data on every row object</p>
                <p>expandRow.renderer callback will pass the origin row object to you</p>
            </div>
        ),
        showExpandColumn: true,
        expandByColumnOnly: true,
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return (<b><i className="fa fa-minus" /> </b>);
            }
            return (<b><i className="fa fa-plus" /> </b>);
        },
        expandColumnRenderer: ({ expanded }) => {
            if (expanded) {
                return (
                    <b><i className="fa fa-search-minus" /> </b>
                );
            }
            return (
                <b><i className="fa fa-search-plus" /> </b>
            );
        }
    };




    return (
        <Container fluid>
            <Row className="justify-content-md-center">
            <Col lg={2} sm={6}>         
                        <StatsCard
                            bigIcon={<i className="fa fa-object-group text-warning" />}
                            statsText="Clusters"
                            statsValue="1"
                            statsIcon={<i className="fa fa-object-group" />}
                            hintText="Number of clusters"
                        />
                </Col>
                <Col lg={2} sm={6}>         
                        <StatsCard
                            bigIcon={<img alt="Solr" src={Solr} className="img-fluid" />}
                            statsText="Solr"
                            statsValue="Version 7.7"
                            statsIcon={<i className="fa fa-refresh" />}
                            hintText="Solr Version of leader"
                        />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-sitemap text-warning" />}
                        statsText="Nodes"
                        statsValue="1 Node"
                        statsIcon={<i className="fa fa-refresh" />}
                        hintText="Total no. of nodes"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-book text-success" />}
                        statsText="Indices"
                        statsValue="4"
                        statsIcon={<i className="fa fa-calendar-o" />}
                        hintText="Total Indices"
                    />
                </Col>
                <Col lg={2} sm={6}>
                    <StatsCard
                        bigIcon={<i className="fa fa-braille text-danger" />}
                        statsText="Shards"
                        statsValue="23"
                        statsIcon={<i className="fa fa-clock-o" />}
                        hintText="Total Shards"
                    />
                </Col>
            </Row>
            <Accordion defaultActiveKey="0">
                <Card >
                    <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0"><h4 className="text-success">Solr Cluster Details <i className="fa fa-gear fw" /></h4></Accordion.Toggle>
                    </Card.Header>
                    <Card.Body>
                        <Accordion.Collapse eventKey="0">
                            <Row className="justify-content-md-center">
                                <ClusterTable data={products} columns={columns} expandRow={expandRow} searchholder="Enter Node Name or Index Name to Filter" />
                            </Row>
                        </Accordion.Collapse>
                    </Card.Body>
                </Card>
            </Accordion>
        </Container>
    );
}

export default Dashboard