import React, { Component } from 'react'
import { Container, Row, Card } from 'react-bootstrap'
import { productsGenerator } from '../../../tmp_utils/commons';
import ClusterTable from '../../../Helper/ClusterTable/ClusterTable';

export default class ZkStatus extends Component {
    render() {
        const products = productsGenerator(87);
        const hiddenRows = [1, 4];
        const columns = [{
            dataField: 'id',
            text: 'Node',
        }, {
            dataField: 'Zk Node 1',
            text: 'Collection Name',
            sort: true
        }, {
            dataField: 'price',
            text: 'Zk Node 2',
            sort: true,
            sortCaret: (order, column) => {
                if (!order) return (<span>&nbsp;&nbsp;<i className="fa fa-sort" /> </span>);
                else if (order === 'asc') return (<span>&nbsp;&nbsp;<i className="fa fa-sort-asc" /></span>);
                else if (order === 'desc') return (<span>&nbsp;&nbsp;<i className="fa fa-sort-desc" /></span>);
                return null;
            }
        }, {
            dataField: 'price',
            text: 'Zk Node 3',
        }];
        const expandRow = {}

        return (
            <Container fluid className="col-lg-12 zk-component">
                <h5 className="text-success">Zookeeper Node Details</h5>
                <Card className="col-lg-12" >
                    <Card.Body>
                        <Row className="justify-content-md-center">
                            <ClusterTable data={products} columns={columns} expandRow={expandRow} searchholder="Enter Node Name to Filter" hiddenRows={hiddenRows} />
                        </Row>
                    </Card.Body>
                </Card >
            </Container>
        )
    }
}
