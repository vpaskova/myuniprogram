package com.vp.myuniprogram.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vp.myuniprogram.entities.User;
import com.vp.myuniprogram.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    final AuthService authService;

    @PutMapping("/login")
    public User login(@RequestBody ObjectNode emailAndPasswordInJson) {
        return authService.loginUser(emailAndPasswordInJson);
    }

    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader("session-token") String sessionToken){
        return authService.logoutUser(sessionToken);
    }

    @GetMapping("/get")
    public ResponseEntity getUserBySessionToken(@RequestHeader("session-token") String sessionToken){
        return authService.getUserBySessionToken(sessionToken);
    }
}
