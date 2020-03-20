import React from 'react';
import { Link } from 'react-router-dom';
import httpRequest from '../../httpRequest';

class MailVerification extends React.Component {

    state = {
        verified: false,
        awaitingServer: true
    }

    componentDidMount() {
        httpRequest('/api/verify', "POST", {
            token: this.props.match.params.token
        })
        .then(response => {
            this.setState({
                verified: response,
                awaitingServer: false
            });
        })
        .catch(_ => {});
    }

    render () {
        const { verified, awaitingServer } = this.state;

        if (awaitingServer)
            return (<div> Aguarde un instante </div>);
        else {
            if (verified)
                return (<div>
                    <h3> Su correo ha sido verificado exitosamente </h3>
                    <Link to='/login'>
                        <button> Ir al login </button>
                    </Link>
                </div>);
            else
                return (<div>
                    <h3> Su correo no pudo ser verificado </h3>
                </div>);

        }
    }
}

export default MailVerification;