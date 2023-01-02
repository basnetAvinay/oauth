package com.poc.authcode.oktawebclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/token").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
        return http.build();
    }

    @Bean
    ReactiveClientRegistrationRepository clientRegistrations(
            @Value("${spring.security.oauth2.client.provider.okta.token-uri}") String tokenUri,
            @Value("${spring.security.oauth2.client.registration.okta.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.okta.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.client.registration.okta.scope}") Set<String> scope,
            @Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}") String authorizationGrantType,
            @Value("${spring.security.oauth2.client.registration.okta.redirect-uri}") String redirectUri,
            @Value("${spring.security.oauth2.client.provider.okta.authorization-uri}") String authorizationUri) {
        var registration = ClientRegistration
                .withRegistrationId("okta")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationUri(authorizationUri)
                .scope(scope)
                .redirectUri(redirectUri)
                .tokenUri(tokenUri)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }
}
