package com.poc.authcode.oktawebclient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.authcode.oktawebclient.dto.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

@Service
public class TokenService {

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.okta.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.okta.client-secret}")
    String clientSecret;

    @Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}")
    String authorizationGrantType;

    @Value("${spring.security.oauth2.client.registration.okta.redirect-uri}")
    String redirectUri;

    @Value("${spring.security.oauth2.client.provider.okta.token-uri}")
    String tokenUri;


    public Mono<String> exchangeCodeForTokenAndGetClaims(String code, String state) {
        logger.info("Authorization code: " + code);

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));

        return webClient.post()
                .uri(tokenUri)
                .headers(h -> {
                    h.setAccept(List.of(MediaType.APPLICATION_JSON));
                    h.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
                    h.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                })
                .body(BodyInserters
                        .fromFormData(GRANT_TYPE, authorizationGrantType)
                        .with(REDIRECT_URI, redirectUri)
                        .with(CODE, code))
                .retrieve()
                .bodyToMono(Token.class)
                .flatMap(token -> {
                    decodeAndLogTokenInfo(token);
                    return getClaims(token.getAccess_token());
                });
    }

    private Mono<String> getClaims(String accessToken) {
        return webClient.get()
                .uri("http://localhost:9091/claims")
                .headers(h -> {
                    h.setAccept(List.of(MediaType.APPLICATION_JSON));
                    h.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                })
                .retrieve()
                .bodyToMono(String.class);
    }

    private void decodeAndLogTokenInfo(Token token) {
        logger.info("\n\nExchanging authorization code for token completed");
        try {
            // Chunk 0 is header and chunk 1 is payload
            String[] chunks = token.getId_token().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            ObjectMapper mapper = new ObjectMapper();
            String payload = new String(decoder.decode(chunks[1]));
            Map<String, Object> payloadMap = mapper.readValue(payload, new TypeReference<>() {
            });
            logger.info("Hey " + payloadMap.get("firstName") + " " + payloadMap.get("lastName"));
            logger.info("Your email is " + payloadMap.get("userEmail"));
        } catch (JsonProcessingException e) {
            logger.error("An error occurred: " + e.getMessage());
        }
        logger.info("Access Token: " + token.getAccess_token());
        logger.info("Access Token Expires in: " + token.getExpires_in());
        logger.info("Refresh Token: " + token.getRefresh_token());
        logger.info("ID Token: " + token.getId_token() + "\n\n");
    }
}
