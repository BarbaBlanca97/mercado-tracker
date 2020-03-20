package com.barbablanca.mercadotracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barbablanca.mercadotracker.exceptions.BadCredentialsException;
import com.barbablanca.mercadotracker.exceptions.BadPasswordResetAttempt;
import com.barbablanca.mercadotracker.mailing.MailSender;
import com.barbablanca.mercadotracker.users.UserEntity;
import com.barbablanca.mercadotracker.users.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
public class SecurityController {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final MailSender mailSender;

    SecurityController(UserRepository userRepository, PasswordResetRepository passwordResetRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("api/login")
    public ResponseEntity<UserEntity> login(@RequestBody LoginCredentials credentials) throws BadCredentialsException {

        UserEntity user = userRepository.findByNameAndPassword(credentials.username, credentials.getPassword())
                    .orElseThrow(BadCredentialsException::new);

        Algorithm algorithm = Algorithm.HMAC256("secret");

        Date expiresAt = new Date();
        expiresAt.setTime(expiresAt.getTime() + 900000);

        String token = JWT.create()
                .withExpiresAt(expiresAt)
                .withClaim("userId", user.getId())
                .withClaim("username", user.getName())
                .sign(algorithm);

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", token);

        return new ResponseEntity<UserEntity>(user, headers, HttpStatus.OK);
    }

    @PostMapping("api/authenticate-token")
    public UserEntity authTest() {
        PrincipalCredentials principal =
                (PrincipalCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(principal.getId()).orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("api/reset/solicite")
    public ResponseEntity registerPasswordReset (@RequestBody PasswordResetRequest passwordResetRequest) throws IOException {
        UserEntity user = userRepository.findByEmail(passwordResetRequest.getEmail());

        if (user != null) {
            try {
                PasswordReset passwordReset = new PasswordReset(user);
                Integer code = passwordReset.generateCode();

                passwordResetRepository.save(passwordReset);
                mailSender.sendPasswordResetCode(user, code);
            }
            catch (DataAccessException exception) { System.out.println(exception.getMessage()); }
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("api/reset/make")
    public Boolean makePasswordReset(@RequestBody PasswordResetMake passwordResetMake) throws BadPasswordResetAttempt {
        if (!passwordResetMake.isValid()) {
            throw new BadPasswordResetAttempt(passwordResetMake.getError());
        }

        String codeHash = Hashing.sha256()
                .hashString(passwordResetMake.getCode().toString(), StandardCharsets.UTF_8)
                .toString();

        PasswordReset passwordReset = passwordResetRepository.findByCodeHash(codeHash)
                .orElseThrow(() -> new BadPasswordResetAttempt("El codigo ingresado no parece ser válido"));

        UserEntity user = passwordReset.getUser();
        user.setPassword(passwordResetMake.getNewPassword());

        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);

        return true;
    }
}
