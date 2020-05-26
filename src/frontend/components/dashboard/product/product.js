import React from 'react';
import RequestButton from '../../request-button';

import './styles.scss';

class Product extends React.Component {

    render () {
        const { name, imgUrl, url, price, currency, prevPrice, prevCurrency, onDelete, waiting } = this.props;
        let variation = 0;
        let currencyChange = false;

        if ( prevPrice && price !== prevPrice) {
            if (currency === prevCurrency)
                variation = price - prevPrice;
            else
                currencyChange = true;
        }

        return (
        <a className='dashboard-product' href={ url } target='_blank'>
            <div className='dashboard-product-header'>
                <div className='dashboard-product-header-img' style={{ backgroundImage: `url("${ imgUrl }")`}} />
                <h4>{ name }</h4>
            </div>

            <div className='dashboard-product-info'>
                <div className='dashboard-product-info-price left' >
                    <div className='dashboard-product-info-price-header' >
                        Precio actual
                    </div>
                    $ { price.toFixed(2) } { currency }
                </div>
                <div className='dashboard-product-info-price' >
                    <div className='dashboard-product-info-price-header' >
                        Variacion
                    </div>
                    { currencyChange ?
                        `${ prevCurrency } >> ${ currency }` :
                        `${ variation < 0 ? '-' : '+' } $${ Math.abs(variation).toFixed(2) } ${ currency }`
                    }
                </div>
            </div>

            <div className='dashboard-product-controls'>
                <RequestButton
                waiting={ waiting }
                onClick={ (e) => { e.stopPropagation(); e.preventDefault(); onDelete(); } }> 
                <span>Dejar de seguir</span>
                </RequestButton>
            </div>
        </a>
        );
    }
}

export default Product;