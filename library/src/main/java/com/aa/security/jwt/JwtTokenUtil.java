package com.aa.security.jwt;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtTokenUtil {
private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

private final JwtProperties jwtProperties;

    public JwtTokenUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateJwtToken(Authentication authentication) {

    UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
            .setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtProperties.getExpirationMs()))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
            .compact();
}

public String getUsername(String token) {
    return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody().getSubject();
}

public boolean validate(String authToken) {
    try {
        Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(authToken);
        return true;
    } catch (SignatureException e) {
        logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
        logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
}
}
