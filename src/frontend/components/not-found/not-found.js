import React from 'react';
import { Link } from 'react-router-dom';

import './styles.scss';

export default function () {
    return (
        <div id='not-found'>
            <h2> La pagina que buscas no se encuentra </h2>
            <Link to='/'>
                <button className='primary' > Inicio </button>
            </Link>
        </div>
    );
}