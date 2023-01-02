package com.poc.authcode.resourceserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @PreAuthorize("hasAuthority('SCOPE_mod_custom')")
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getUser(Principal principal) {
        var user = new HashMap<String, String>();
        user.put("name", principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
