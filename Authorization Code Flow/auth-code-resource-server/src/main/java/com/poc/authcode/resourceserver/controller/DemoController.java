package com.poc.authcode.resourceserver.controller;

import com.poc.authcode.resourceserver.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/claims")
    public ResponseEntity<List<Map<String, Object>>> claims() {
        return new ResponseEntity<>(demoService.getClaims(), HttpStatus.OK);
    }
}
