import React from 'react';
import { Redirect } from 'react-router-dom';
import withHttpRequest from '../../../HOCs/withHttpRequest';

import './styles.scss';

class DeleteAccount extends React.Component {

    state = {
        awaitingConfirmation: false,
        serverResponded: false,
        accountDeleted: false,
        error: ''
    }

    handleDeleteAttempt = () => {
        this.setState({ awaitingConfirmation: true });
    }

    handleConfirm = () => {
        this.props.httpRequest('/api/users', 'DELETE')
        .then(response => {
            this.setState({ serverResponded: true, accountDeleted: response });
        })
        .catch(this.props.errorHandler(error => {
            this.setState({ serverResponded: true, accountDeleted: false, error: error.message })
        }));
    }

    render () {
        
        const { awaitingConfirmation, serverResponded, accountDeleted, error } = this.state;

        if (serverResponded) {
            if (accountDeleted)
                return <Redirect to='/deleted' />;
            else
                return (
                    <div> No se pudo borrar la cuenta: { error } </div>
                );
        }
            

        return (
            <div id='delete-account'>
                { !awaitingConfirmation ?
                <button className='warn' onClick={ this.handleDeleteAttempt } > Borrar cuenta </button>
                :
                <>
                    Esta acción no se puede deshacer, ¿Estás seguro que deseas continuar?
                    <button className='warn' onClick={ this.handleConfirm } > Confirmar </button>
                </>
                }
            </div>
        );
    }
}

export default withHttpRequest(DeleteAccount);