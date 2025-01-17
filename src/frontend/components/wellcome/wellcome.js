import React from 'react';
import { Link, Switch, Route } from 'react-router-dom';

import './styles.scss'

import LogIn from '../log-in';
import SignIn from '../sign-in';

const WellcomeScreen = function ({ logedIn, onLogin }) {

    return (
        <div id="wellcome-container">
            <h1 id="wellcome-header"> Mercado Tracker </h1>

            <img id='wellcome-image' src='/start-image.png' alt='Imagen de presentacion' />
            
            <p> Elegí un producto de mercadolibre y te avisamos cuando cambie el precio <strong>¡Así de simple!</strong> </p>

            <div id="wellcome-actions">
                <Link to={`/signin`}><button> Registrarse </button></Link>
                <Link to={`/login`}><button> Iniciar sesión </button></Link>
            </div>

            <Switch>
                <Route
                    path='/login'
                    render={(match) => (<LogIn
                        onLogin={onLogin}
                        logedIn={logedIn}
                        match={match}
                    />)}
                />
                <Route path='/signin' component={SignIn} />
            </Switch>
        </div>
    );
}

export default WellcomeScreen;