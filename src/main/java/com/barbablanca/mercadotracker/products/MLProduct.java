package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.prices.PriceEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MLProduct {
    @JsonProperty("id")
    String id;
    @JsonProperty("title")
    String name;
    @JsonProperty("thumbnail")
    String imgUrl;
    @JsonProperty("price")
    Float price;
    @JsonProperty("currency_id")
    String currency;
    @JsonIgnore
    Date createdAt = new Date();
    @JsonProperty("permalink")
    String url;

    MLProduct() {}

    MLProduct(String id, String name, String imgUrl, Float price, String currency, String url) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
        this.currency = currency;
        this.url = url;
    }

    public ProductEntity asProductEntity() {
        return new ProductEntity(id, name, imgUrl, url, new PriceEntity(price, currency, createdAt));
    }

    public Float getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getId() {
        return id;
    }
}
