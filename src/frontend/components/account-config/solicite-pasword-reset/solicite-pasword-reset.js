import React from 'react';
import httpRequest from '../../../httpRequest';

import './styles.scss';
import { Link } from 'react-router-dom';

export default class SolicitePasswordReset extends React.Component {

    state = {
        email: ''
    }

    handleInputChange = (event) => {
        this.setState({ email: event.target.value });
    }

    handleSendRequest = () => {
        httpRequest('/api/reset/solicite', 'POST', this.state)
        .catch(_ => {});
    }

    render () {
        const { email } = this.state;

        return (
            <div id='solicite-password-reset'>
                <h4>Ingrese su correo electrónico, si existe se le enviará un correo con un código para efectuar un cambio de contraseña</h4>
                <ul>
                    <li>Por discreción, si la operación falla (el mail no se envia o no esta registrado en la base de datos)<strong> no se le informará</strong> en esta pantalla, deberá reintentar la operacion más tarde </li>
                    <li>Si reintentó la operacion multiples veces, solo el código del primer correo recibido será valido</li>
                </ul>
                <input 
                    placeholder='Correo electrónico'
                    type='email'
                    value={ email }
                    onChange={ this.handleInputChange }
                />

                <div> 
                    <button className='primary' onClick={ this.handleSendRequest }> Enviar </button>
                    <Link to='/reset/make'>
                        <button> Ya recibí el correo </button>
                    </Link>
                </div>
            </div>
        );
    }
}