import React, { Component } from 'react'
import { Container, OverlayTrigger, Card, Button, Popover } from 'react-bootstrap'
import Segments from './Segments/Segments';

import { NavLink } from 'react-router-dom';
import Summary from './Summary/Summary';
import CollectionConfig from './ConfigFiles/CollectionConfig';

export default class Collection extends Component {
    state = {
        current: "summary"
    }

    changeView(val) {
        this.setState((state, props) => ({
            current: val
        }))
    }

    renderView(view) {
        switch (view) {
            case 'summary':
                return (
                    <Summary />
                );
            case 'config':
                return (
                    <CollectionConfig />
                );
            default:
                return (
                    <Summary />
                );
        }
    }


    render() {
        const type = "warning";
        const text = "black";
        const timeDiff = "4 days";
        const popover = (
            <Popover id="popover-basic">
                <Popover.Title as="h4">Summary</Popover.Title>
                <Popover.Content>
                    <ul>
                        <li><strong>Alias</strong>:  CollectionLive</li>
                        <li><strong>Shards</strong>: 3</li>
                        <li><strong>Replica</strong>: 1</li>
                        <li><strong>Size</strong>: 2 GB</li>
                    </ul>
                </Popover.Content>
            </Popover>
        );
        return (
            <Container fluid className="zk-component">
                <h5 className="text-success">Collection1 Manager {'  '}
                    <OverlayTrigger trigger="click" placement="right" overlay={popover}>
                        <i className="fa fa-info-circle text-primary" />
                    </OverlayTrigger>
                </h5>
                <NavLink
                    to={"/dashboard"}
                    className="nav-link"
                    activeClassName="active"
                    key="config">
                </NavLink>
                <Button variant="info" onClick={() => this.changeView("summary")}>Summary</Button>{' '}
                <Button variant="info" onClick={() => this.changeView("config")}>Config Files</Button>{' '}
                <Button variant="info" onClick={() => this.changeView("query")}>Query</Button>{' '}
                <Button variant="info" onClick={() => this.changeView("analysis")}>Analysis</Button>{' '}
                <Button variant="info" onClick={() => this.changeView("plugins")}>Plugins</Button>{' '}

                {this.renderView(this.state.current)}
            </Container>

        )
    }
}
