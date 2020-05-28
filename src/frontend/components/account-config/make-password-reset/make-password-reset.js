import React from 'react';
import { Link } from 'react-router-dom';

import withHttpRequest from '../../../HOCs/withHttpRequest';
import ErrorDisplay from '../../error-display';
import RequestButton from '../../request-button';

import './styles.scss';

class MakePasswordReset extends React.Component {

    state = {
        code: undefined,
        newPassword: '',
        passworConfirm: '',
        success: false,
        hasError: false,
        error: '',
        waitingResponse: false
    }

    handleInputChange = (event) => {
        let fieldtoUpdate = '';

        switch (event.target.id) {
            case 'make-password-reset-input-code': fieldtoUpdate = 'code'; break;
            case 'make-password-reset-input-password': fieldtoUpdate = 'newPassword'; break;
            case 'make-password-reset-input-password-confirm': fieldtoUpdate = 'passworConfirm'; break;
        }

        this.setState({ [fieldtoUpdate]: event.target.value });
    }

    handleFormSubmit = (event) => {
        event.preventDefault();

        if (this.state.passworConfirm === this.state.newPassword) {
            this.setState({ waitingResponse: true });

            this.props.httpRequest('/api/reset/make', 'POST', {
                code: this.state.code,
                newPassword: this.state.newPassword
            })
                .then(response => {
                    if (response)
                        this.setState({ hasError: false, success: true });
                })
                .catch(this.props.errorHandler(error => {
                    this.setState({ hasError: true, success: false, error: error.message });
                }))
                .finally(_ => this.setState({ waitingResponse: false }));
        }
        else {
            this.setState({ hasError: true, error: 'Las contraseñas no coinciden' });
        }
    }

    renderContent = () => {
        const { code, newPassword, passworConfirm, success, hasError, error, waitingResponse } = this.state;

        if (success)
            return <>
                <h3> El cambio se ha realizado con exito </h3>
                <Link to='/login'>
                    <button className='primary' > Log in </button>
                </Link>
            </>

        else
            return <>
                <h3> Ingrese el código recibido en su correo para efectuar el cambio de contraseña </h3>

                <form onSubmit={this.handleFormSubmit} id='make-password-reset-form'>
                    <label htmlFor='make-password-reset-input-code'> Codigo </label>
                    <input
                        id='make-password-reset-input-code'
                        type='number'
                        placeholder='Ingrese el codigo'
                        onChange={this.handleInputChange}
                        value={code}
                    />

                    <label htmlFor='make-password-reset-input-pasword'> Contraseña </label>
                    <input
                        id='make-password-reset-input-password'
                        type='password'
                        placeholder='Ingrese la nueva contraseña'
                        onChange={this.handleInputChange}
                        value={newPassword}
                    />

                    <input
                        id='make-password-reset-input-password-confirm'
                        type='password'
                        placeholder='Confirme la nueva contraseña'
                        onChange={this.handleInputChange}
                        value={passworConfirm}
                    />

                    {hasError &&
                        <ErrorDisplay>{error}</ErrorDisplay>}

                    <RequestButton type='submit' waiting={ waitingResponse }> 
                    <span>Enviar</span> 
                    </RequestButton>
                </form>
            </>;
    }

    render() {
        return (
            <div id='make-password-reset'>
                {this.renderContent()}
            </div>
        );
    }
}

export default withHttpRequest(MakePasswordReset);