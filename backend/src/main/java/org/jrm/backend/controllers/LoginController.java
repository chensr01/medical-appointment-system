package org.jrm.backend.controllers;

import lombok.AllArgsConstructor;
import org.jrm.backend.exceptions.UserExistsException;
import org.jrm.backend.exceptions.NotFoundException;
import org.jrm.backend.requests.LoginRequest;
import org.jrm.backend.requests.RegisterRequest;
import org.jrm.backend.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            var patient = loginService.register(request.name(), request.email());
            return ResponseEntity.ok(patient.getId().toString());
        } catch (UserExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            var patient = loginService.login(request.name(), request.email());
            return ResponseEntity.ok(patient.getId().toString());
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("User not found. Please check your credentials.");
        }
    }
}
