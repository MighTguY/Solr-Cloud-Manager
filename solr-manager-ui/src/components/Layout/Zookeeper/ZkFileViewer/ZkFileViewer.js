import React, {PureComponent } from 'react'
import { Treebeard, decorators } from 'react-treebeard/dist'
import data from './data';
import Header from './Header';
import * as filters from './filter';
import { includes } from 'lodash';
import NodeViewer from './NodeViewer';
import defaultTheme from './defaultTheme';
import defaultAnimations from './animation';


export default class ZkFileViewer extends PureComponent {


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

        node.active = false;
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

        node.selected = false;

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
        defaultTheme.tree.base = {
            ...defaultTheme.tree.base,
            color: "black",
            backgroundColor: "white"
        };


        return (
            <div className="container zk-component fluid col-lg-12">
                <h5 className="text-success">Zookeeper Node Directory </h5>

                {/* Search Bar */}
                <div className="row">
                    <div className="col-12">
                        <div className="input-group">
                            <input className="form-control border-secondary py-2" type="search" placeholder="Search For File in Directory" onKeyUp={this.onFilterMouseUp.bind(this)} />
                        </div>
                    </div>
                </div>


                <div className="row">

                    <div className="col-sm-3 py-2">
                        <div className="card h-100 ">
                            <div className="card-body">
                                <h3 className="card-title">File Structure</h3>

                                <Treebeard
                                    style={defaultTheme}
                                    data={data}
                                    onToggle={this.onToggle}
                                    onSelect={this.onSelect}
                                    decorators={{ ...decorators, Header }}
                                    animations={defaultAnimations}
                                />


                            </div>
                        </div>
                    </div>

                    <div className="col-sm-9 py-2">
                        <div className="card h-100 border-primary overflow-auto">
                            <div className="card-body">
                                <h3 className="card-title">File: {console.log(cursor)} </h3>
                                <NodeViewer node={cursor} />
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}
