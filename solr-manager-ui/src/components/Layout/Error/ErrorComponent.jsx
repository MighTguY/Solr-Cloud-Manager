import React, { Component } from 'react'
import { Link } from 'react-router-dom'

export default class ErrorComponent extends Component {

    render() {
        console.log(this.props);
        return (
            <div>
                Error occurred please click  <Link to="/solr/Dashboard">Click to go to Dashboard</Link>
            </div>
        )
    }
}
