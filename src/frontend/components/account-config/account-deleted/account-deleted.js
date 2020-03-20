import React from 'react';
import { Link } from 'react-router-dom';

import './styles.scss';

export default function() {
    return (
        <div id='account-deleted'>
            Su cuenta ha sido borrada exitosamente
            <Link to='/'>
                <button> Ir a la pantalla de bienvenida </button>
            </Link>
        </div>
    );
}