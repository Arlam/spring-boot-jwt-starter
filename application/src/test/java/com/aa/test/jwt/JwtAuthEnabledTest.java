package com.aa.test.jwt;

import com.aa.security.jwt.JwtTokenUtil;
import com.aa.security.jwt.UserDetailsImpl;
import com.aa.test.jwt.controller.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JwtAuthEnabledTest extends BaseTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Credentials credentials = Credentials.builder()
                .username("user")
                .password("password")
                .build();
        final var url = "http://localhost:" + port + "/auth/login";
        final var response = this.restTemplate
                .exchange(url, HttpMethod.POST, new HttpEntity<>(credentials, headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void defaultLoginDisabled() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final var url = "http://localhost:" + port + "/login";
        final var response = this.restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testApiSecurityEnabled() {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final var url = "http://localhost:" + port + "/hello";
        final var response = this.restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hello, World");
    }

    @Test
    public void testApiSecurityEnabledUnauthorized() {
        final var response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/hello", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private String getToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("user")
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        return jwtTokenUtil.generateJwtToken(authentication);
    }
}
