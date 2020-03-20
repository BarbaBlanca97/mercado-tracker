import React from 'react';
import ReactDOM from 'react-dom';

import Notification from './notification';

import './styles.scss';

class NotificationCenter extends React.Component {

    render() {
        const { notifications, onDismiss } = this.props;
        
        if (notifications.length === 0) return null;

        return (ReactDOM.createPortal(
            <div id='notification-center' >
                { notifications.map(notification => (
                    <Notification
                        key={ notification.id }
                        type={ notification.type }
                        onDismiss={ () => onDismiss(notification.id) }
                    >
                        { notification.message }
                    </Notification>
                )) }
            </div>
            , document.getElementById('popup-overlay')));
    }
}

export default NotificationCenter;