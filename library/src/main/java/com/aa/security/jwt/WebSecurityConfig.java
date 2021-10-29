package com.aa.security.jwt;

import com.aa.security.jwt.filters.JwtTokenFilter;
import com.aa.security.jwt.handler.LoginFailureHandler;
import com.aa.security.jwt.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@Import(JwtConfig.class)
@ConditionalOnProperty(value = "app.security.jwt.enabled", havingValue = "true")
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public WebSecurityConfig(
            LoginSuccessHandler loginSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            UserDetailsService userDetailsService,
            JwtTokenFilter jwtTokenFilter) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // configure authentication manager
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        authException.getMessage()
                ))

                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
