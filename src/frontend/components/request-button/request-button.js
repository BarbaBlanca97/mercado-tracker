import React from 'react';
import { FaSyncAlt } from 'react-icons/fa';

import './styles.css';

function RequestButton({ waiting, children, ...otherProps }) {

    if (waiting)
        otherProps.className += ' request-button__waiting';

    return (
        <button
            { ...otherProps }
        >
            { waiting && <FaSyncAlt className='request-button__waiting--icon' /> }
            { children }
        </button>
    );
}

export default RequestButton;