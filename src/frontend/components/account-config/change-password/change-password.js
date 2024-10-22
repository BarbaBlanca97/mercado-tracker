import React from 'react';
import ErrorDisplay from '../../error-display';
import withHttpRequest from '../../../HOCs/withHttpRequest';
import RequestButton from '../../request-button';

import './styles.scss';

class ChangePassword extends React.Component {
    state = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        hasError: false,
        errorMessage: '',
        success: false,
        waitingResponse: false
    }

    handleInputChange = (event) => {
        let fieldToUpdate;

        switch (event.target.id) {
            case 'config-input-old-password': fieldToUpdate = 'oldPassword'; break;
            case 'config-input-password': fieldToUpdate = 'newPassword'; break;
            case 'config-input-confirm-password': fieldToUpdate = 'confirmPassword'; break;
        }

        this.setState({ [fieldToUpdate]: event.target.value });
    }

    handleFormSubmit = (event) => {
        event.preventDefault();

        if (this.state.newPassword !== this.state.confirmPassword) {
            this.setState({ hasError: true, errorMessage: 'Las contraseñas no coinciden', success: false });
            return;
        }

        this.setState({ waitingResponse: true });
        this.props.httpRequest(`/api/users/${ this.props.user }`, 'PATCH', {
            oldPassword: this.state.oldPassword,
            newPassword: this.state.newPassword
        })
        .then(response => {
            if (response)
                this.setState({ 
                    success: true, 
                    hasError: false, 
                    oldPassword: '', 
                    newPassword: '', 
                    confirmPassword: '' 
                });
            else
                throw { message: 'Algo salió mal' }
        })
        .catch(this.props.errorHandler(response => {
            this.setState({ hasError: true, errorMessage: response.message, success: false })
        }))
        .finally(_ => this.setState({ waitingResponse: false }))
    }

    render () {
        const { oldPassword, newPassword, confirmPassword, hasError, errorMessage, success, waitingResponse } = this.state;

        return (
            <form 
                id='config-change_password-form'
                onSubmit={ this.handleFormSubmit }
            >
                <label htmlFor='config-input-old-password' > Antigua contraseña </label>
                <input
                    id='config-input-old-password' 
                    type='password' 
                    value={ oldPassword }
                    onChange={ this.handleInputChange }
                />
    
                <label htmlFor='config-input-password' > Nueva contraseña </label>
                <input 
                    id='config-input-password' 
                    type='password' 
                    value={ newPassword }
                    onChange={ this.handleInputChange }
                />
                
                <label htmlFor='config-input-confirm-password' > Confirme su nueva contraseña </label>
                <input 
                    id='config-input-confirm-password' 
                    type='password' 
                    value={ confirmPassword }
                    onChange={ this.handleInputChange }
                />

                { hasError &&
                <ErrorDisplay>{ errorMessage }</ErrorDisplay> }

                { success &&
                'Cambio realizado con exito' }

                <RequestButton 
                type='submit' 
                id='config-button-confirm'
                waiting={ waitingResponse }
                >
                <span>Confirmar</span>
                </RequestButton>
            </form>
        );
    }
}

export default withHttpRequest(ChangePassword);