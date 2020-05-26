import React from 'react';
import { Redirect } from 'react-router-dom';
import withHttpRequest from '../../../HOCs/withHttpRequest';
import RequestButton from '../../request-button';

import './styles.scss';

class DeleteAccount extends React.Component {

    state = {
        awaitingConfirmation: false,
        serverResponded: false,
        accountDeleted: false,
        error: '',
        waitingResponse: false 
    }

    handleDeleteAttempt = () => {
        this.setState({ awaitingConfirmation: true });
    }

    handleConfirm = () => {
        this.setState({ waitingResponse: true });

        this.props.httpRequest('/api/users', 'DELETE')
        .then(response => {
            if (response)
                localStorage.removeItem('authorization');
                
            this.setState({ serverResponded: true, accountDeleted: response });
        })
        .catch(this.props.errorHandler(error => {
            this.setState({ serverResponded: true, accountDeleted: false, error: error.message })
        }))
        .finally(_ => this.setState({ waitingResponse: false }));
    }

    render () {
        
        const { awaitingConfirmation, serverResponded, accountDeleted, error, waitingResponse } = this.state;

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
                    <RequestButton className='warn' waiting={ waitingResponse } onClick={ this.handleConfirm } >
                        <span>Confirmar</span>
                    </RequestButton>
                </>
                }
            </div>
        );
    }
}

export default withHttpRequest(DeleteAccount);