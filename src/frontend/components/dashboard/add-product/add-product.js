import React from 'react';
import { FaPlus } from 'react-icons/fa';

import RequestButton from '../../request-button';

import './styles.scss';

class AddProduct extends React.Component {

    state = {
        productUrl: ''
    }

    handleInputChange = (event) => {
        this.setState({ productUrl: event.target.value });
    }

    render() {

        const { productUrl } = this.state;
        const { onSubmit, waiting } = this.props;

        return (
            <div id='dashboard-add_product' >
                <input
                    id='dashboard-add_product-input'
                    type='text'
                    onChange={this.handleInputChange}
                    value={productUrl}
                    placeholder='Introducir aquí la url (o id) del producto...'
                />

                <RequestButton
                    id='dashboard-add_product-button'
                    className='primary'
                    waiting={ waiting }
                    onClick={() => { onSubmit(productUrl) }}
                >
                    <FaPlus className="mr" />
                    <span>Agregar producto</span>
                </RequestButton>
            </div>
        );
    }
}

export default AddProduct;