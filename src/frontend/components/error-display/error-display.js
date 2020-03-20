import React from 'react';

import './styles.scss';

export default function({ children }) {
    return (<div className="error-display">
        { children }
    </div>);
};