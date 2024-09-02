package com.ol.chronoshare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;


    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                        .sameOrigin()
                )
        );
        httpSecurity.authorizeRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/back/user/creation-compte")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/back/user/login")).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.logout((logout) -> logout.logoutUrl("/back/logout").logoutSuccessHandler((req, resp, auth) -> resp.setStatus(HttpStatus.OK.value())).deleteCookies(TOKEN_COOKIE));

        return httpSecurity.build();
    }

}