import React from 'react';
import { FaTimes } from 'react-icons/fa';

import './styles.scss';

const Notification = function({ onDismiss, type, children, ...otherProps }) {

    return (<div { ...otherProps } className={`notification &{ type === 'ERROR' ? error : '' }`} >
        { children }
        <div onClick={ onDismiss } className='notification-close' ><FaTimes /> </div>
    </div>);
};

export default Notification;