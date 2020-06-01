import React, { Component } from 'react'
import data from '../../Zookeeper/ZkFileViewer/data'
import FileViewer from '../../Zookeeper/ZkFileViewer/FileViewer'

export default class CollectionConfig extends Component {
    render() {
        return (
            <>
                <FileViewer data={data} title="Collection Zk Node Directory" />
            </>
        )
    }
}
