import React from 'react';
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import SolrUI from './components/Layout/SolrUI';

function App() {

  return (
   <BrowserRouter>
    <Switch>
      <Route path="/solr" render={props => <SolrUI {...props} />} />
      <Redirect from="/" to="/solr/dashboard" />
    </Switch>
  </BrowserRouter>
  );
}

export default App;
