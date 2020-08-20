package com.justiny.rpete.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ExpiringJwtToken {
    String token;
    Instant expiryInstant;
}
