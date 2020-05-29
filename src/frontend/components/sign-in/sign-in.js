import React from 'react';
import withHttpRequest from '../../HOCs/withHttpRequest';

import ErrorDisplay from '../error-display';
import RequestButton from '../request-button';

import './styles.scss'

class SingIn extends React.Component {

    user = {
        email: 'default'
    }

    constructor(props) {
        super(props);

        this.state = {
            credentials: {
                username: '',
                mail: '',
                passwordA: '',
                passwordB: ''
            },
            registeredSuccessufly: false,
            hasError: false,
            waitingResponse: false
        }
    }

    handleInputChange = (event) => {
        let field = '';
        let value = event.target.value;

        switch (event.target.id) {
            case 'singin-input-user': field = 'username'; break;
            case 'singin-input-mail': field = 'mail'; break;
            case 'singin-input-passwordA': field = 'passwordA'; break;
            case 'singin-input-passwordB': field = 'passwordB'; break;
            default: break;
        }

        this.setState(state => ({
            credentials: {
                ...state.credentials,
                [field]: value
            }
        }));
    }

    handleFormSubmit = (event) => {
        event.preventDefault();

        if (this.state.credentials.passwordA === this.state.credentials.passwordB) {
            this.setState({ waitingResponse: true });

            this.props.httpRequest('/api/users', 'POST',
                {
                    username: this.state.credentials.username,
                    email: this.state.credentials.mail,
                    password: this.state.credentials.passwordA
                })
                .then(response => {
                    this.user = response;
                    this.setState({ registeredSuccessufly: true, hasError: false });
                })
                .catch(this.props.errorHandler(error => {
                    this.setState({ hasError: true, error })
                }))
                .finally(_ => this.setState({ waitingResponse: false }));
        }
        else {
            this.setState({ hasError: true, error: { message: 'Las contraseñas no coinciden' } });
        }
    }

    render() {
        const { username, mail, passwordA, passwordB } = this.state.credentials;
        const { hasError, error, waitingResponse } = this.state;

        if (this.state.registeredSuccessufly)
            return (
                <div id='singin-success'>
                    <p>
                        <span className='text-bold'>Listo! Ya tenés tu cuenta</span>, para habilitar las notificaciones por correo hace click en el link que te enviamos a <span className='text-bold'>{this.user.email}</span>
                    </p>
                    <p>
                        Podes loguearte y empezar a usar la aplicacion haciendo click el botón <span className='text-bold'>Iniciar sesión</span> que hay acá arriba.
                    </p>
                    <p>
                        Si no encontrás los correos <span className='text-bold'>revisá tu carpeta de spam</span>
                    </p>
                </div>);
        else
            return (
                <div id='singin-container'>
                    <h2> Crear una cuenta </h2>

                    <form id='singin-form' onSubmit={this.handleFormSubmit}>
                        <label htmlFor='singin-input-user'> Ingrese un nombre de usuario </label>
                        <input
                            id='singin-input-user'
                            type='text'
                            value={username}
                            onChange={this.handleInputChange}
                        />

                        <label htmlFor='singin-input-mail'> Ingrese su dirección de correo electrónico </label>
                        <input
                            id='singin-input-mail'
                            type='email'
                            value={mail}
                            onChange={this.handleInputChange}
                        />

                        <label htmlFor='singin-input-passwordA'> Ingrese su contraseña </label>
                        <input
                            id='singin-input-passwordA'
                            type='password'
                            value={passwordA}
                            onChange={this.handleInputChange}
                        />

                        <label htmlFor='singin-input-passwordB'> Repita su contraseña </label>
                        <input
                            id='singin-input-passwordB'
                            type='password'
                            value={passwordB}
                            onChange={this.handleInputChange}
                        />

                        {hasError && <ErrorDisplay>{error.message} </ErrorDisplay>}

                        <RequestButton type='submit' waiting={ waitingResponse } className="primary">
                        <span>Crear cuenta</span>
                        </RequestButton>
                    </form>

                </div>
            );
    }
}

export default withHttpRequest(SingIn);