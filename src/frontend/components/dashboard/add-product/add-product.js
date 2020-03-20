import React from 'react';
import { FaPlus } from 'react-icons/fa';

import './styles.scss';

class AddProduct extends React.Component {

    state = {
        productUrl: ''
    }

    handleInputChange = (event) => {
        this.setState({ productUrl: event.target.value });
    }

    render () {

        const { productUrl } = this.state;
        const { onSubmit } = this.props;

        return (
            <div id='dashboard-add_product' >
                <input 
                    id='dashboard-add_product-input' 
                    type='text' 
                    onChange={ this.handleInputChange } 
                    value={ productUrl }
                    placeholder='Introducir aquí la url (o id) del producto...'
                />

                <button 
                    id='dashboard-add_product-button'
                    className='primary'
                    onClick={ () => { onSubmit(productUrl) } } 
                > 
                    <FaPlus className="mr" />
                    Agregar producto
                </button>
            </div>
        );
    }
}

export default AddProduct;