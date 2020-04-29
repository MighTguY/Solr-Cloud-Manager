import React, { Component } from 'react'
import MonacoEditor from "react-monaco-editor";

export default class CodeView extends Component {
    render() {
        return (
            <div>
                <MonacoEditor
                    width="800"
                    height="300"
                    language={this.props.language}
                    defaultValue={this.props.code}
                    theme="vs-dark"
                    // editorWillMount={this.editorWillMount}
                />
            </div>
        )
    }
}
