import React, { Component } from 'react'
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider, { Search } from 'react-bootstrap-table2-toolkit';
import paginationFactory from 'react-bootstrap-table2-paginator'
import filterFactory from 'react-bootstrap-table2-filter';



export default class ClusterTable extends Component {

    render() {

        const { SearchBar } = Search;

        const customTotal = (from, to, size) => (
            <span className="react-bootstrap-table-pagination-total text-primary">
                &nbsp;&nbsp;<b>Showing { from} to { to} of { size} Results</b>
            </span>
        );

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
                text: 'All', value: this.props.data.length
            }] // A numeric array is also available. the purpose of above example is custom the text
        };
        // const Caption = () => <h3 style={{ borderRadius: '0.25em', textAlign: 'center', color: 'purple', border: '1px solid purple', padding: '0.5em' }}>Component as Header</h3>;

        return (
            <div className="col-lg-12">
                {/* <BootstrapTable bootstrap4 keyField="id" data={products} caption="Plain text header" columns={columns} /> */}
                <ToolkitProvider
                    keyField="id"
                    data={this.props.data}
                    columns={this.props.columns}
                    search={{}}

                >
                    {
                        props => (
                            <div className="col-lg-12">
                                <h3>{this.props.title}</h3>

                                <SearchBar  {...props.searchProps} placeholder={this.props.searchholder} className="col-lg-12" />
                                <hr />
                                <BootstrapTable {...props.baseProps}
                                    pagination={paginationFactory(options)}
                                    expandRow={this.props.expandRow}
                                    filter={filterFactory()}
                                    wrapperClasses="table-responsive"
                                    hiddenRows={this.props.hiddenRows}
                                />
                            </div>
                        )
                    }
                </ToolkitProvider>
            </div>
        );
    }
}

