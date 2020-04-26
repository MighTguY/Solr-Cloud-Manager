import React from 'react'
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider, { Search } from 'react-bootstrap-table2-toolkit';
import paginationFactory from 'react-bootstrap-table2-paginator'
import filterFactory, { textFilter } from 'react-bootstrap-table2-filter';
import { productsGenerator } from '../../../tmp_utils/commons';

function ClusterTable(data) {

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
  const { SearchBar } = Search;

  const customTotal = (from, to, size) => (
  <span className="react-bootstrap-table-pagination-total">
    Showing { from } to { to } of { size } Results
  </span>
);

const expandRow = {
  renderer: row => (
    <div>
      <p>{ `This Expand row is belong to rowKey ${row.id}` }</p>
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

const options = {
  paginationSize: 4,
  pageStartIndex: 0,
  // alwaysShowAllBtns: true, // Always show next and previous button
  // withFirstAndLast: false, // Hide the going to First and Last page button
  // hideSizePerPage: true, // Hide the sizePerPage dropdown always
  // hidePageListOnlyOnePage: true, // Hide the pagination list when only one page
  firstPageText: 'First',
  prePageText: 'Back',
  nextPageText: <span>Next</span>,
  lastPageText: 'Last',
  nextPageTitle: 'First page',
  prePageTitle: 'Pre page',
  firstPageTitle: 'Next page',
  lastPageTitle: 'Last page',
  showTotal: true,
  paginationTotalRenderer: customTotal,
  disablePageTitle: true,
  sizePerPageList: [{
    text: '5', value: 5
  }, {
    text: '10', value: 10
  }, {
    text: '20', value: 20
  }, {
    text: '30', value: 30
  }, {
    text: '40', value: 40
  }, {
    text: 'All', value: products.length
  }] // A numeric array is also available. the purpose of above example is custom the text
};
  // const Caption = () => <h3 style={{ borderRadius: '0.25em', textAlign: 'center', color: 'purple', border: '1px solid purple', padding: '0.5em' }}>Component as Header</h3>;

  return (
    <div className="col-lg-12">
      {/* <BootstrapTable bootstrap4 keyField="id" data={products} caption="Plain text header" columns={columns} /> */}
      <ToolkitProvider
      keyField="id"
      data={ products }
      columns={ columns }
      search={ {  } }
      
    >
      {
        props => (
          <div className="col-lg-12">
            <h3>Solr Cluster Details</h3>
            <SearchBar  { ...props.searchProps } placeholder="Enter Node Name or Index Name to Filter" className="col-lg-12"/>
            <hr />
            <BootstrapTable {...props.baseProps} 
            pagination={ paginationFactory(options) }  
            expandRow={ expandRow } 
            filter={ filterFactory() } 
            />
          </div>
        )
      }
    </ToolkitProvider>
    </div>
  );
}

export default ClusterTable