import React, { Component } from 'react'
import { Row, Col, OverlayTrigger, Tooltip } from 'react-bootstrap';
import renderTooltip from '../../Helper/Tooltip/TooltipRenderer';

class StatsCard extends Component {
    render() {
        console.log(this.props.hintText);
        return (
            <div className="card card-stats">
                <div className="content">
                    <Row>
                        <Col xs={5}>
                            <div className="icon-big text-center icon-warning">
                                {this.props.bigIcon}
                            </div>
                        </Col>
                        <Col xs={7}>
                            <div className="numbers">
                                <p><b>{this.props.statsText}</b></p>
                                {this.props.statsValue}
                            </div>
                        </Col>

                    </Row>
                    <OverlayTrigger id="button-tooltip"
                        placement="top"
                        delay={{ show: 250, hide: 400 }}
                        overlay={<Tooltip >
                            {this.props.hintText}
                        </Tooltip>
                        }
                    >
                        <i className="fa fa-info-circle"></i>
                    </OverlayTrigger>

                </div>

            </div>
        );
    }
}

export default StatsCard
