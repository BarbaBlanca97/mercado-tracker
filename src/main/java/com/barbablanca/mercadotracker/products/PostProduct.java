package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.exceptions.IdNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostProduct {
    @JsonProperty("url")
    String url;

    public PostProduct() {}

    public PostProduct(String url) {
        this.url = url;
    }

    public String getId() throws Exception {
        Pattern pattern = Pattern.compile("(?<id>MLA(-?)\\d+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group("id").replace("-", "");
        }
        else {
            throw new IdNotFoundException();
        }
    }
}
