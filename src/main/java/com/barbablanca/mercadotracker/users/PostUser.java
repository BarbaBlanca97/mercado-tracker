package com.barbablanca.mercadotracker.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostUser {
    @NotNull( message = "Debe proporcionar un nombre de usuario")
    @NotBlank( message = "Debe proporcionar un nombre de usuario")
    @JsonProperty("username")
    private String username;
    @NotNull
    @Email( message = "El formato de correo electónico no es válido")
    @JsonProperty("email")
    private String email;
    @NotNull( message = "Debe proporcionar una contraseña")
    @NotBlank( message = "Debe proporcionar una contraseña")
    @Size(min = 6, message = "La contraseña debe tener 6 caracteres como mínimo")
    @JsonProperty("password")
    private String password;

    PostUser () {}
}
