import React, { PureComponent } from 'react'
import FileViewer from './FileViewer'
import { Treebeard, decorators } from 'react-treebeard/dist'
import data from './data';
import Header from './Header';
import * as filters from './filter';
import { includes } from 'lodash';
import NodeViewer from './NodeViewer';
import defaultTheme from './defaultTheme';
import defaultAnimations from './animation';
import { Container } from 'react-bootstrap';


export default class ZkFileViewer extends PureComponent {

    render() {
        return (
            <FileViewer data={data} title="Zookeeper Node Directory"/>
        )
    }
}
