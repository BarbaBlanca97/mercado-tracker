package com.barbablanca.mercadotracker.prices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PriceEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @JsonIgnore
    private Integer id;
    private Float amount;
    private String currency;
    private Date date;

    PriceEntity () {}

    public PriceEntity (Float amount, String currency, Date date) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }
}
