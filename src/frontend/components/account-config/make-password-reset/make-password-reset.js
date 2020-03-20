import React from 'react';
import { Link } from 'react-router-dom';

import httpRequest from '../../../httpRequest';
import ErrorDisplay from '../../error-display';

import './styles.scss';

export default class MakePasswordReset extends React.Component {

    state = {
        code: undefined,
        newPassword: '',
        passworConfirm: '',
        success: false,
        hasError: false,
        error: ''
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
            httpRequest('/api/reset/make', 'POST', {
                code: this.state.code,
                newPassword: this.state.newPassword
            })
            .then(response => {
                if (response)
                    this.setState({ hasError: false, success: true });
            })
            .catch(error => {
                this.setState({ hasError: true, success: false, error: error.message });
            });
        }
        else {
            console.log('las contraseñas no coinciden');
        }
    }

    renderContent = () => {
        const { code, newPassword, passworConfirm, success, hasError, error } = this.state;

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

                <form onSubmit={ this.handleFormSubmit } id='make-password-reset-form'>
                    <label htmlFor='make-password-reset-input-code'> Codigo </label>
                    <input 
                        id='make-password-reset-input-code' 
                        type='number' 
                        placeholder='Ingrese el codigo'
                        onChange={ this.handleInputChange }
                        value={ code }
                    />

                    <label htmlFor='make-password-reset-input-pasword'> Contraseña </label>
                    <input 
                        id='make-password-reset-input-password' 
                        type='password' 
                        placeholder='Ingrese la nueva contraseña'
                        onChange={ this.handleInputChange }
                        value={ newPassword }
                    />

                    <input 
                        id='make-password-reset-input-password-confirm' 
                        type='password'  
                        placeholder='Confirme la nueva contraseña'
                        onChange={ this.handleInputChange }
                        value={ passworConfirm }
                    />

                    { hasError && 
                    <ErrorDisplay>{ error }</ErrorDisplay> }

                    <button type='submit'> Enviar </button>
                </form>
            </>;
    }

    render () {
        return (
            <div id='make-password-reset'>
                { this.renderContent() }
            </div>
        );
    }
}