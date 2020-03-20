package com.barbablanca.mercadotracker.products;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MLProductResponse {
    @JsonProperty("body")
    MLProduct body;
    @JsonProperty("code")
    Integer code;

    MLProductResponse () {}

    MLProductResponse (MLProduct body, Integer code) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public MLProduct getBody() {
        return body;
    }
}
