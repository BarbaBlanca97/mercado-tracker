package com.barbablanca.mercadotracker.users;

import com.barbablanca.mercadotracker.exceptions.CustomException;
import com.barbablanca.mercadotracker.security.PasswordResetRepository;
import com.barbablanca.mercadotracker.security.PrincipalCredentials;
import com.barbablanca.mercadotracker.mailing.MailSender;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController {

    private final UserRepository repository;
    private final MailSender mailSender;
    private final PasswordResetRepository passwordResetRepository;

    UserController(UserRepository repository, MailSender mailSender, PasswordResetRepository passwordResetRepository) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.passwordResetRepository = passwordResetRepository;
    }

    @PatchMapping("/api/users/{id}")
    public boolean getUser(@Valid @RequestBody UserChangePassword userChangePassword) throws CustomException {
        Integer userId = ((PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con id "+ userId));

        if (user.getPassword().equals(userChangePassword.getOldPassword())) {
            user.setPassword(userChangePassword.getNewPassword());
            repository.save(user);
        }
        else throw new CustomException(400, "Su antigua contraseña es incorrecta");

        return true;
    }

    @PostMapping("/api/users")
    public UserEntity createUser(@Valid @RequestBody PostUser postInfo) throws ConstraintViolationException, IOException {
        UserEntity newUser = repository.save(postInfo.asUserEntity());
        mailSender.sendVerificationMail(newUser.getEmail(), newUser.getId());

        return newUser;
    }

    @Transactional
    @DeleteMapping("/api/users")
    public Boolean deleteUser() {
        Integer userId = ((PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        passwordResetRepository.deleteByUserId(userId);
        repository.deleteById(userId);

        return true;
    }
}
