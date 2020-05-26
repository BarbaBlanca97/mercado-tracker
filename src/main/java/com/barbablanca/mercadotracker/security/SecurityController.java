package com.barbablanca.mercadotracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barbablanca.mercadotracker.exceptions.CustomException;
import com.barbablanca.mercadotracker.mailing.MailSender;
import com.barbablanca.mercadotracker.users.UserEntity;
import com.barbablanca.mercadotracker.users.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@RestController
public class SecurityController {

    Logger log = LoggerFactory.getLogger(SecurityController.class);

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final MailSender mailSender;
    private final Environment env;
    private final BCryptPasswordEncoder passwordEncoder;

    SecurityController(
            UserRepository userRepository,
            PasswordResetRepository passwordResetRepository,
            MailSender mailSender,
            Environment env,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.mailSender = mailSender;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("api/login")
    public ResponseEntity<UserEntity> login(@Valid @RequestBody LoginCredentials credentials) throws CustomException {

        UserEntity user = userRepository.findByNameOrEmail(credentials.getUsername(), credentials.getUsername())
                    .orElseThrow(() -> new CustomException(401, "Usuario y/o contraseña incorrectos"));

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            throw new CustomException(401, "Usuario y/o contraseña incorrectos");

        Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(env.getProperty("JWT_SECRET")));

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
    public Boolean registerPasswordReset (@Valid @RequestBody PasswordResetRequest passwordResetRequest) throws IOException {
        UserEntity user = userRepository.findByEmail(passwordResetRequest.getEmail());

        if (user != null) {
            try {
                PasswordReset passwordReset = new PasswordReset(user);
                Integer code = passwordReset.generateCode();
                passwordReset.setCodeHash(DigestUtils.sha256Hex(code.toString()));

                passwordResetRepository.save(passwordReset);
                mailSender.sendPasswordResetCode(user, code);
            }
            catch (DataAccessException exception) {
                log.error(exception.getMessage());
            }
        }

        return true;
    }

    @PostMapping("api/reset/make")
    public Boolean makePasswordReset(@Valid @RequestBody PasswordResetMake passwordResetMake) throws CustomException {

        String codeHash = DigestUtils.sha256Hex(passwordResetMake.getCode().toString());

        PasswordReset passwordReset = passwordResetRepository.findByCodeHash(codeHash)
                .orElseThrow(() -> new CustomException(400, "El codigo ingresado no parece ser válido"));

        UserEntity user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetMake.getNewPassword()));

        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);

        return true;
    }
}
