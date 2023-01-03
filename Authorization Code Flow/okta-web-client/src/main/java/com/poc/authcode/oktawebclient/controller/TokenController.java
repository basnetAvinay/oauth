package com.poc.authcode.oktawebclient.controller;

import com.poc.authcode.oktawebclient.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/oauth2/token")
    public Mono<String> exchangeCodeForTokenAndGetClaims(@RequestParam("code") String code,
                                                         @RequestParam("state") String state) {
        return tokenService.exchangeCodeForTokenAndGetClaims(code, state);
    }
}
