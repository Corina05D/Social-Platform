package com.utcn.socialplatform.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.utcn.socialplatform.model.Constants.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider userAuthenticationProvider;
    private final JwtAuthenticationTokenFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authz) -> authz
                            .requestMatchers(API_V1+AUTHENTICATION+"/**").permitAll()
                            .requestMatchers(API_V1+REGISTER+RESET_PASSWORD+"/**").permitAll()
                            .requestMatchers(API_V1+SOCKET+"/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(userAuthenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            ;
            return http.build();
        }
    }
}