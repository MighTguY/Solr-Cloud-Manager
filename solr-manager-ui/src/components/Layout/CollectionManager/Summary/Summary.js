import React, { Component } from 'react'
import { Card, CardDeck } from 'react-bootstrap'
import Segments from '../Segments/Segments';

export default class Summary extends Component {
    render() {
        const type = "warning";
        const text = "black";
        const timeDiff = "4 days";
        return (
            <>
            <CardDeck>
                    <Card bg={type} text={text} border="dark">
                        <Card.Header>
                            <h3 className="text-mute">Configurations</h3>
                        </Card.Header>
                        <Card.Body>

                            <Card.Text className="bold">
                                <i class="fa fa-code icon pull-right display-4"></i>

                                <ul>
                                    <li><b>Config Name</b>: uk_grocery_recipes_A_config</li>
                                    <li><b>Max shards per node</b>:</li>
                                    <li><b>Replication factor:</b></li>
                                    <li><b>Router name:</b></li>
                                </ul>

                            </Card.Text>
                        </Card.Body>
                        <Card.Footer>
                            Last modified {timeDiff} ago
                            </Card.Footer>
                    </Card>
                    <Card bg={type} text={text} border="dark">
                        <Card.Header>
                            <h3 className="text-mute">Statistics</h3>
                        </Card.Header>
                        <Card.Body>

                            <Card.Text>
                                <i class="fa fa-dashboard icon pull-right display-4"></i>
                                <ul>
                                    <li><b>Index Size:</b>123</li>
                                    <li><b>Num Docs:</b>123</li>
                                    <li><b>Max Doc:</b>123</li>
                                    <li><b>Deleted Docs:</b>123</li>
                                    <li><b>Version:</b>123</li>
                                    <li><b>Segments Count:</b>123</li>
                                </ul>
                            </Card.Text>
                        </Card.Body>
                        <Card.Footer>
                            Last modified {timeDiff} ago
                            </Card.Footer>
                    </Card>
                    <Card bg={type} text={text} border="dark">
                        <Card.Header>
                            <h3 className="text-mute">Replication (Master)</h3>
                        </Card.Header>
                        <Card.Body>
                            <Card.Title></Card.Title>
                            <Card.Text>
                                <i class="fa fa-crosshairs icon pull-right display-4"></i>
                                <ul>
                                    <li>
                                        <b>Version</b>
                                    </li>
                                    <li>
                                        <b>Gen</b>
                                    </li>
                                    <li>
                                        <b>Size</b>
                                    </li>
                                </ul>
                            </Card.Text>
                        </Card.Body>
                        <Card.Footer>
                            Last modified {timeDiff} ago
                            </Card.Footer>
                    </Card>
                </CardDeck>

                <Card>
                    <Card.Header bg="primary">
                        <h3 className="text-info">Segments</h3>
                    </Card.Header>
                    <Card.Body>
                        <Segments />
                    </Card.Body>
                </Card>
            </>
        )
    }
}
