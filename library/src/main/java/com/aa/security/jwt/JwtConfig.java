package com.aa.security.jwt;

import com.aa.security.jwt.filters.JwtTokenFilter;
import com.aa.security.jwt.handler.LoginFailureHandler;
import com.aa.security.jwt.handler.LoginSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(value = "app.security.jwt.enabled", havingValue = "true")
public class JwtConfig {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;

    public JwtConfig(
            UserDetailsService userDetailsService,
            JwtProperties jwtProperties) {
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LoginFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil(jwtProperties);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenUtil(), userDetailsService);
    }

}
