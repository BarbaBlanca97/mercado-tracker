import React from 'react';
import { Link } from 'react-router-dom';
import DeleteAccount from './delete-account';
import ChangePassword from './change-password';

import './styles.scss';

class ConfigScreen extends React.Component {

    render () {
        return (
            <div id='account-config'>
                <h3>Datos de usuario</h3>
                <div id='account-config-user-data-container'>
                    <div className='account-config-user-data'>
                        <span className='account-config-user-data-label'>Nombre: </span>
                        <span>{ this.props.user.name }</span>
                    </div>

                    <div className='account-config-user-data'>
                        <span className='account-config-user-data-label'>Correo electrónico: </span>
                        <span>{ this.props.user.email }</span>
                    </div>

                    <div className='account-config-user-data'>
                        <span className='account-config-user-data-label'>Cuenta verificada: </span>
                        <span>{ this.props.user.verified ? 'Sí' : 'No, debe verificarla utilizando el correo que se le envió' }</span>
                    </div>
                </div>
                <h3>Cambiar contraseña</h3>
                <ChangePassword user={ this.props.user.id } ></ChangePassword>

                <hr />
                
                <h3>Borrar cuenta</h3>
                <DeleteAccount></DeleteAccount>

                <hr />

                <Link to='/dashboard' id='account-config-back'>
                    <button> Volver a la pantalla principal </button>
                </Link>
            </div>
        );
    }
}

export default ConfigScreen;