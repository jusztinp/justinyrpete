package com.justiny.rpete.security;

import com.justiny.rpete.exceptions.RpeteException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;
import java.util.stream.Stream;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

    @Value(value = "${keystore.location}")
    private String keystoreLocation;

    @Value(value = "${keystore.password}")
    private String password;

    @Value(value = "${keystore.alias}")
    private String alias;

    @Value(value = "${jwt.expiration.time}")
    private Long jwtExpirationTime;

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream(keystoreLocation);
            keyStore.load(inputStream, password.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RpeteException("Exception occurred while loading keystore");
        }
    }

    public ExpiringJwtToken generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();
        return generateTokenWithUsername(username);
    }

    @NotNull
    public ExpiringJwtToken generateTokenWithUsername(String username) {
        Instant expiryInstant = Instant.now().plusMillis(jwtExpirationTime);
        String token = Jwts.builder()
                .setSubject(username)
                .signWith(getPrivateKey())
                .setExpiration(Date.from(expiryInstant))
                .compact();

        return new ExpiringJwtToken(token, expiryInstant);
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RpeteException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RpeteException("Exception occurred while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
