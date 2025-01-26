package com.projet.ShopConnect.auth.controller;

import com.projet.ShopConnect.auth.model.*;
import com.projet.ShopConnect.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
//TODO: A voir
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @GetMapping("/health")
    public String getHealth() {
        return "The API is healthy";
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authentication(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authentication(request));
    }
}
