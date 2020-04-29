import React from 'react';
import PropTypes from 'prop-types';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { docco } from 'react-syntax-highlighter/dist/esm/styles/hljs'

const HELP_MSG = 'Select A Node To See Its Data Structure Here...';

const NodeViewer = ({node}) => {
    let json = JSON.stringify(node, null, 4);

    if (!json) {
        json = HELP_MSG;
    }
    
    return  <SyntaxHighlighter language="xml" style={docco}>
    {json}
  </SyntaxHighlighter>;
};

NodeViewer.propTypes = {
    node: PropTypes.object
};

export default NodeViewer;
