package com.barbablanca.mercadotracker.mailing;

import com.barbablanca.mercadotracker.users.UserEntity;
import com.barbablanca.mercadotracker.users.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
public class MailingController {
    private final PendingVerificationsRepository verificationsRepository;
    private final UserRepository userRepository;

    MailingController(PendingVerificationsRepository verificationsRepository, UserRepository userRepository) {
        this.verificationsRepository = verificationsRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/verify")
    public boolean verify(@RequestBody MailVerification verificationData) {
        try {
            PendingVerificationEntity verification = verificationsRepository.findByToken(verificationData.getToken())
                    .orElseThrow(EntityNotFoundException::new);

            UserEntity user = userRepository.findById(verification.getUserId())
                    .orElseThrow(EntityNotFoundException::new);

            user.setVerified(true);

            userRepository.save(user);
            verificationsRepository.delete(verification);

            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
