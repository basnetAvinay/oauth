package com.poc.authcode.oktawebclient.controller;

import com.poc.authcode.oktawebclient.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/oauth2/token")
    public void getToken(@RequestParam("code") String code, @RequestParam("state") String state) {
        tokenService.exchangeCodeForToken(code, state);
    }
}
