package com.justiny.rpete.service;

import com.justiny.rpete.dto.RegisterRequest;
import com.justiny.rpete.exceptions.RpeteException;
import com.justiny.rpete.model.NotificationEmail;
import com.justiny.rpete.model.Redditor;
import com.justiny.rpete.model.VerificationToken;
import com.justiny.rpete.repository.RedditorRepository;
import com.justiny.rpete.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    public static final String ACCOUNT_VERIFICATION_URL = "http://localhost:8080/api/auth/accountVerification";
    private final PasswordEncoder passwordEncoder;
    private final RedditorRepository redditorRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        Redditor redditor = new Redditor();
        redditor.setUsername(registerRequest.getUsername());
        redditor.setEmail(registerRequest.getEmail());
        redditor.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        redditor.setCreated(Instant.now());
        redditor.setEnabled(false);
        redditorRepository.save(redditor);

        String token = generateVerificationToken(redditor);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", redditor.getEmail(),
                "Thank you for signing up to Rpete, " +
                        "please click on the below url to activate your account : " +
                        ACCOUNT_VERIFICATION_URL + "/" + token));
    }

    private String generateVerificationToken(Redditor redditor) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setRedditor(redditor);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(()-> new RpeteException("Invalid token"));
//        fetchPersonAndEnable
    }
}
