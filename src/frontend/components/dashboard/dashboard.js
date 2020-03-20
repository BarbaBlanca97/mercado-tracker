import React from 'react';
import { Link } from 'react-router-dom';
import httpRequest from '../../httpRequest';
import { FaUser } from 'react-icons/fa';
import { MdSettings, MdExitToApp } from 'react-icons/md';

import withHttpRequest from '../../HOCs/withHttpRequest';

import Product from './product';
import AddProduct from './add-product';
import NotificationCenter from './notification-center';

import './styles.scss';

class DashboardScreen extends React.Component {
    notificationCounter = 0;
    state = {
        notifications: []
    }

    constructor(props) {
        super(props);

        this.onProductsChange = this.props.onProductsChange;
    }

    handleProductDelete = (productId) => {
        httpRequest(`/api/users/${ this.props.user.id }/products/${ productId }`, 'DELETE')
        .then(response => {
            if (response)
                this.onProductsChange(this.props.products.filter(product => product.id !== productId));
        })
        .catch(_ => {});
    }

    handleAddProductSubmit = (productUrl) => {
        this.props.httpRequest(`/api/users/${ this.props.user.id }/products`, 'POST', { url: productUrl })
        .then(response => {
            let repeated = false;
            const newProductsArray = []

            for(let i = 0; i < this.props.products.length; i++) {
                if (this.props.products[i].id === response.id)
                    repeated = true;
                
                newProductsArray.push(this.props.products[i]);
            }  
            
            if (!repeated)
                newProductsArray.push(response);

            this.onProductsChange(newProductsArray);
        })
        .catch(this.props.errorHandler(response => {
            this.setState(state => ({ notifications: [ ...state.notifications, { id: this.notificationCounter++, message: response.message, type: 'ERROR' } ] }))
        }));
    }

    handleDismissNotification = (notificationId) => {
        this.setState(state => ({ notifications: state.notifications.filter(notification => notification.id !== notificationId) }));
    }

    render () {
        const { user, onLogOut, products } = this.props;
        const { notifications } = this.state;

        return (
            <div>
                <div id='dashboard-navbar'>
                    <h2 id='dashboard-navbar-header'> Mercado Tracker </h2>
    
                    <div id='dashboard-navbar-side'>
                        <span id='dashboard-navbar-user'> <FaUser className="mr" /> { user.name } </span>
                        <button onClick={ onLogOut } className='icon'><MdExitToApp></MdExitToApp></button>
                        <Link to='/config'><button className='icon'><MdSettings></MdSettings></button></Link>
                    </div>
                </div>

                <AddProduct
                    onSubmit={ this.handleAddProductSubmit }
                />
    
                <div id='dashboard-product-container'>
                    { products.map((product) => ( 
                        <Product 
                        key={ product.id } 
                        name={ product.name }
                        imgUrl={ product.imgUrl }
                        url={ product.url }
                        price={ product.curPrice.amount }
                        currency={ product.curPrice.currency }
                        prevPrice={ product.prevPrice && product.prevPrice.amount }
                        prevCurrency={ product.prevPrice && product.prevPrice.currency }
                        onDelete={ () => { this.handleProductDelete(product.id) } }
                        /> )) }
                </div>

                <NotificationCenter 
                    notifications={ notifications }
                    onDismiss={ this.handleDismissNotification }
                />
            </div>
        );
    }
}

export default withHttpRequest(DashboardScreen);