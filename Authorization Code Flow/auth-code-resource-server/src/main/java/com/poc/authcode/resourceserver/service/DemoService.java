package com.poc.authcode.resourceserver.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoService {

    public List<Map<String, Object>> getClaims() {
        return List.of(
                Map.of("claimDescription", "Medical", "allowedAmount", 16123939.8,
                        "totalPaid", 3988879.4, "costPerUnit", 119.71),
                Map.of("claimDescription", "Pharmacy", "allowedAmount", 5384456.19,
                        "totalPaid", 4830431.85, "costPerUnit", 1.881)
        );
    }
}
