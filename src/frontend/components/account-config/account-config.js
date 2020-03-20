import React from 'react';
import { Link } from 'react-router-dom';
import DeleteAccount from './delete-account';
import ChangePassword from './change-password';

import './styles.scss';

class ConfigScreen extends React.Component {

    render () {
        return (
            <div id='account-config'>
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