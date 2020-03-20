package com.barbablanca.mercadotracker.users;

import com.barbablanca.mercadotracker.exceptions.BadCredentialsException;
import com.barbablanca.mercadotracker.exceptions.PatchException;
import com.barbablanca.mercadotracker.exceptions.SinginException;
import com.barbablanca.mercadotracker.security.PrincipalCredentials;
import com.barbablanca.mercadotracker.mailing.MailSender;
import org.apache.catalina.User;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.postgresql.util.PSQLException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {

    private final UserRepository repository;
    private final MailSender mailSender;

    UserController(UserRepository repository, MailSender mailSender) {
        this.repository = repository;
        this.mailSender = mailSender;
    }

    @PatchMapping("/api/users/{id}")
    public boolean getUser(@RequestBody UserChangePassword userChangePassword) throws BadCredentialsException, PatchException {
        Integer userId = ((PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserEntity user = repository.findById(userId).orElseThrow(EntityNotFoundException::new);
        boolean saved = false;

        if (userChangePassword.isValid()) {
            if (user.getPassword().equals(userChangePassword.getOldPassword())) {
                user.setPassword(userChangePassword.getNewPassword());
                repository.save(user);

                saved = true;
            }
            else throw new BadCredentialsException(BadCredentialsException.Field.PASSWORD);
        }
        else throw new PatchException("La nueva contraseña no es valida");


        return saved;
    }

    @PostMapping("/api/users")
    public UserEntity createUser(@RequestBody PostUser postInfo) throws ConstraintViolationException, IOException, SinginException {
        if (postInfo.isValid()) {
            UserEntity newUser = repository.save(postInfo.asUserEntity());
            mailSender.sendVerificationMail(newUser.getEmail(), newUser.getId());

            return newUser;
        }
        else
            throw new SinginException(postInfo.getError());
    }

    @DeleteMapping("/api/users")
    public Boolean deleteUser() {
        Integer userId = ((PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        repository.deleteById(userId);

        return true;
    }
}
