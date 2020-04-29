import React from 'react'
import { Tooltip } from 'react-bootstrap'

export default function renderTooltip(props) {
    console.log("renderer", props);
    return (
        <Tooltip id="button-tooltip" {...props}>
            {props.hdata}
        </Tooltip>
    );
}
