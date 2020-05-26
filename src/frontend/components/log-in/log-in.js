import React from 'react';
import { Redirect, Link } from 'react-router-dom';
import withHttpRequest from '../../HOCs/withHttpRequest';

import ErrorDisplay from '../error-display';

import './styles.scss'

class LogIn extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            credentials: {
                username: '',
                password: ''
            },
            logedIn: false,
            hasError: false
        }
    }

    handleInputChange = (event) => {
        let field = '';
        let value = event.target.value;

        switch (event.target.id) {
            case 'login-input-user': field = 'username'; break;
            case 'login-input-password': field = 'password'; break;
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

        this.props.httpRequest('/api/login', 'POST', this.state.credentials)
            .then(response => {
                if (response.id) {
                    this.setState({ logedIn: true, hasError: false });
                    this.props.onLogin(response);
                }
            })
            .catch(error => {
                this.setState({ hasError: true, error });
            });
    }

    render() {
        const { username, password } = this.state.credentials;
        const { hasError, error } = this.state;

        if (this.props.logedIn)
            return <Redirect to='/dashboard' />
        else
            return (
                <div id='login-container'>
                    {this.props.match.location.search.includes('expired') &&
                        <ErrorDisplay>
                            Ha pasado demasiado tiempo inactivo, vuelva a iniciar sesion
                    </ErrorDisplay>
                    }

                    <h2> Iniciar sesión </h2>

                    <form id='login-form' onSubmit={this.handleFormSubmit}>
                        <label htmlFor='login-input-user'> Usuario o correo electrónico </label>
                        <input
                            id='login-input-user'
                            type='text'
                            value={username}
                            onChange={this.handleInputChange}
                        />

                        <label htmlFor='login-input-password'> Contraseña </label>
                        <input
                            id='login-input-password'
                            type='password'
                            value={password}
                            onChange={this.handleInputChange}
                        />

                        {hasError && <ErrorDisplay>{error.message}</ErrorDisplay>}

                        <button type='submit' className="primary"> Entrar </button>
                    </form>

                    <Link to='/reset/solicite'><button id='login-forgot-password'> Olvide mi contraseña </button></Link>
                </div>
            );
    }
}

export default withHttpRequest(LogIn);