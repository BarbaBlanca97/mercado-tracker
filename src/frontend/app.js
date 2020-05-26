import '@babel/polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import {
    BrowserRouter as Router,
    Switch,
    Route
} from 'react-router-dom';

import WellcomeScreen from './components/wellcome';
import DashboardScreen from './components/dashboard';
import ConfigScreen from './components/account-config';
import ProtectedRoute from './components/protected-route';
import MailVerification from './components/mail-verification';
import AccountDeleted from './components/account-config/account-deleted';
import SolicitePasswordReset from './components/account-config/solicite-pasword-reset';
import MakePasswordReset from './components/account-config/make-password-reset';

import './styles.scss';

const ApplicationContext = React.createContext();

class App extends React.Component {

    handleUnauthorized = () => {
        this.handleLogOut();
    }

    state = {
        awaitingServer: true,
        logedIn: false,
        user: null,
        products: [],
        onUnAuthorized: this.handleUnauthorized
    }

    componentDidMount() {
        let accesToken = localStorage.getItem('authorization');

        fetch('/api/authenticate-token', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': accesToken
            }
        })
            .then(async (response) => {
                const responseData = await response.json();

                if (response.ok)
                    this.setState({ logedIn: true, awaitingServer: false, user: responseData, products: responseData.products });
                else
                    this.setState({ awaitingServer: false });
            })
            .catch(_ => {
                this.setState({ awaitingServer: false });
            });
    }

    handleLogin = (user) => {
        this.setState({ logedIn: true, user, products: user.products });
    }

    handleLogOut = () => {
        localStorage.removeItem('authorization');
        this.setState({ logedIn: false, user: null, products: [] });
    }

    handleProductsChange = (products) => {
        this.setState({ products });
    }

    render() {
        const { logedIn, awaitingServer, user, products } = this.state;
        const handleLogin = this.handleLogin;
        const handleLogOut = this.handleLogOut;


        if (awaitingServer)
            return <div> Aguarde un momento </div>;

        return (
            <ApplicationContext.Provider value={{ onUnAuthorized: this.state.onUnAuthorized }}>
                <Router>
                    <Switch>
                        <ProtectedRoute
                            logedIn={logedIn}
                            exact
                            path='/dashboard'
                            render={() => (
                                <DashboardScreen
                                    user={user}
                                    products={products}
                                    onProductsChange={this.handleProductsChange}
                                    onLogOut={handleLogOut}
                                />)}
                        />

                        <ProtectedRoute
                            logedIn={logedIn}
                            exact
                            path='/config'
                            render={() => (
                                <ConfigScreen
                                    user={user}
                                />)}
                        />

                        <Route
                            exact
                            path='/verify/:token'
                            component={MailVerification}
                        />

                        <Route
                            exact
                            path='/deleted'
                            component={AccountDeleted}
                        ></Route>

                        <Route
                            exact
                            path='/reset/solicite'
                            component={SolicitePasswordReset}
                        ></Route>

                        <Route
                            exact
                            path='/reset/make'
                            component={MakePasswordReset}
                        ></Route>

                        <Route
                            path='/'
                            render={() => (
                                <WellcomeScreen
                                    logedIn={logedIn}
                                    onLogin={handleLogin}
                                />)}
                        />
                    </Switch>
                </Router>
            </ApplicationContext.Provider>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('app-root'));

export { ApplicationContext };