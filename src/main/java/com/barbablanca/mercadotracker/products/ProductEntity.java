package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.prices.PriceEntity;
import com.barbablanca.mercadotracker.users.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private String imgUrl;
    private String url;

    @OneToOne( cascade = CascadeType.ALL)
    private PriceEntity curPrice;
    @OneToOne( cascade = CascadeType.ALL)
    private PriceEntity prevPrice;

    @JsonIgnore
    @ManyToMany( mappedBy = "products", fetch = FetchType.EAGER )
    private Set<UserEntity> subscribers = new HashSet<>();;

    ProductEntity () {}

    ProductEntity (String id, String name, String imgUrl, String url, PriceEntity price) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;

        this.url = url == null ? "https://www.mercadolibre.com.ar/" : url;

        this.curPrice = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public PriceEntity getCurPrice() {
        return curPrice;
    }

    public PriceEntity getPrevPrice() {
        return prevPrice;
    }

    public Set<UserEntity> getSubscribers() { return subscribers; }

    public void setId(String id) { this.id = id; }

    public void setPrevPrice(PriceEntity prevPrice) {
        this.prevPrice = prevPrice;
    }

    public void setCurPrice(PriceEntity curPrice) { this.curPrice = curPrice; }
}
