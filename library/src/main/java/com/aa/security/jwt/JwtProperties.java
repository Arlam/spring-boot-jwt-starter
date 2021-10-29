package com.aa.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {
    private boolean enabled = false;
    private String secret;
    private long expirationMs;
}
