package com.barbablanca.mercadotracker.users;

import com.barbablanca.mercadotracker.exceptions.CustomException;
import com.barbablanca.mercadotracker.security.PasswordResetRepository;
import com.barbablanca.mercadotracker.security.PrincipalCredentials;
import com.barbablanca.mercadotracker.mailing.MailSender;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    UserController(
            UserRepository repository,
            MailSender mailSender,
            PasswordResetRepository passwordResetRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PatchMapping("/api/users/{id}")
    public boolean getUser(@Valid @RequestBody UserChangePassword userChangePassword) throws CustomException {
        Integer userId = ((PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con id "+ userId));

        if (passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
            repository.save(user);
        }
        else throw new CustomException(400, "Su antigua contraseña es incorrecta");

        return true;
    }

    @PostMapping("/api/users")
    public UserEntity createUser(@Valid @RequestBody PostUser postInfo) throws ConstraintViolationException, IOException {

        UserEntity newUser = new UserEntity(
                postInfo.getUsername(),
                postInfo.getEmail(),
                passwordEncoder.encode(postInfo.getPassword()),
                false
        );

        newUser = repository.save(newUser);
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
