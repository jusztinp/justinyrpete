package com.justiny.rpete.service;

import com.justiny.rpete.dto.AuthenticationResponse;
import com.justiny.rpete.dto.LoginRequest;
import com.justiny.rpete.dto.RefreshTokenRequest;
import com.justiny.rpete.dto.RegisterRequest;
import com.justiny.rpete.exceptions.RpeteException;
import com.justiny.rpete.model.NotificationEmail;
import com.justiny.rpete.model.User;
import com.justiny.rpete.model.VerificationToken;
import com.justiny.rpete.repository.UserRepository;
import com.justiny.rpete.repository.VerificationTokenRepository;
import com.justiny.rpete.security.ExpiringJwtToken;
import com.justiny.rpete.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
                "Thank you for signing up to Rpete, " +
                        "please click on the below url to activate your account : " +
                        ACCOUNT_VERIFICATION_URL + "/" + token));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ExpiringJwtToken expiringJwtToken = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(expiringJwtToken.getToken())
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(expiringJwtToken.getExpiryInstant())
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RpeteException("Invalid token"));
        fetchPersonAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchPersonAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RpeteException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        ExpiringJwtToken expiringJwtToken = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());


        return AuthenticationResponse.builder()
                .authenticationToken(expiringJwtToken.getToken())
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(expiringJwtToken.getExpiryInstant())
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
