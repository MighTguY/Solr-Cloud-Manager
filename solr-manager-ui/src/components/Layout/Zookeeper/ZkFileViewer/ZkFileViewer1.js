import React, { Fragment, PureComponent } from 'react';
import ReactDOM from 'react-dom';
import { includes } from 'lodash';

import { Treebeard, decorators } from 'react-treebeard';
import data from './data';
import * as filters from './filter';
import Header from './Header';
import NodeViewer from './NodeViewer';

export default class DemoTree extends PureComponent {
    constructor(props) {
        super(props);
        this.state = { data };
        this.onToggle = this.onToggle.bind(this);
        this.onSelect = this.onSelect.bind(this);
    }

    onToggle(node, toggled) {
        const { cursor, data } = this.state;

        if (cursor) {
            this.setState(() => ({ cursor, active: false }));
        }

        node.active = true;
        if (node.children) {
            node.toggled = toggled;
        }

        this.setState(() => ({ cursor: node, data: Object.assign({}, data) }));
    }

    onSelect(node) {
        const { cursor, data } = this.state;

        if (cursor) {
            this.setState(() => ({ cursor, active: false }));
            if (!includes(cursor.children, node)) {
                cursor.toggled = false;
                cursor.selected = false;
            }
        }

        node.selected = true;

        this.setState(() => ({ cursor: node, data: Object.assign({}, data) }));
    }

    onFilterMouseUp({ target: { value } }) {
        const filter = value.trim();
        if (!filter) {
            return this.setState(() => ({ data }));
        }
        let filtered = filters.filterTree(data, filter);
        filtered = filters.expandFilteredNodes(filtered, filter);
        this.setState(() => ({ data: filtered }));
    }

    render() {
        const { data, cursor } = this.state;
        return (
            <div className="container">

                {/* <div className="row">
                    <div className="col-lg-12">
                        <div className="input-group">
                            <input className="form-control border-secondary " type="search" placeholder="Search the tree..." onKeyUp={this.onFilterMouseUp.bind(this)} />
                            <div className="input-group-append">
                                <button className="btn btn-outline-secondary" type="button">
                                    <i className="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                 */}
                <div className="row">
                    <div className="col-sm-6 py-2">
                        <div class="card card-body h-100">
                            <div class="card-body">
                                <div className="card-title"> File Structure </div>
                                <div className="panel-text">
                                    <Treebeard
                                        data={data}
                                        onToggle={this.onToggle}
                                      
                                        decorators={{ ...decorators, Header }}
                                        customStyles={{
                                            header: {
                                                title: {
                                                    color: 'red'
                                                }
                                            }
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-sm-6 py-2">
                        <div className="panel-heading"> File Content </div>
                        <div className="panel-body">
                            <NodeViewer node={cursor} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

