package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.exceptions.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostProduct {
    @NotNull(message = "Debe ingresar la url del producto")
    @NotBlank(message = "Debe ingresar la url del producto")
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
            throw new CustomException(404, "No pudo recuperar el id del producto de la url");
        }
    }
}
