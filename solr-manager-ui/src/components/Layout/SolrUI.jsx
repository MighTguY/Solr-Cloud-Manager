import React, { Component } from 'react'
import Footer from './Footer/Footer'
import NavigationBar from './Navbar/NavigationBar'
import routes from '../../routes'
import { Route, Switch } from 'react-router-dom'
import ErrorComponent from './Error/ErrorComponent'

export default class SolrUI extends Component {

    getRoutes = routes => {
        return routes.map((prop, key) => {
            if (prop.layout === "/solr") {
                return (
                    <Route
                        path={prop.layout + prop.path}
                        render={props => (
                            <prop.component
                                {...props}
                            />
                        )}
                        key={key}
                    />
                );
            } else {
                return (<Route
                    path={prop.layout + "error"}
                    render={props => (
                        <ErrorComponent
                            {...props}
                        />
                    )}
                    key={key}
                />);
            }
        });
    };

    

    render() {
        console.log(this.getRoutes(routes));
        return (
            <div>
                <NavigationBar />
              
                <Switch>{this.getRoutes(routes)}<Route component={ErrorComponent} /></Switch>
                {/* <ClusterTable /> */}
                <Footer />
            </div>
        )
    }
}