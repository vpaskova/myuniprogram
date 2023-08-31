package com.vp.myuniprogram.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vp.myuniprogram.entities.User;
import com.vp.myuniprogram.exceptions.UserAuthenticationException;
import com.vp.myuniprogram.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {

    final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public User loginUser(ObjectNode emailAndPasswordInJson) {
        String email = emailAndPasswordInJson.get("email").asText();
        String password = emailAndPasswordInJson.get("password").asText();
        User user;
        user = authenticateAndReturnUser(email, password);
        user.setSessionToken(generateNewToken());
        userRepository.save(user);
        return user;
    }

    public ResponseEntity logoutUser(String sessionToken) {
        User user = userRepository.findUserBySessionToken(sessionToken);
        if(user == null){
            return new ResponseEntity<>("Неправилен sessionToken",HttpStatus.BAD_REQUEST);
        }
        user.setSessionToken(null);
        userRepository.save(user);

        return new ResponseEntity<>("Успешен изход!",HttpStatus.OK);
    }

    public ResponseEntity getUserBySessionToken(String sessionToken) {
        User user = userRepository.findUserBySessionToken(sessionToken);
        if(user == null){
            return new ResponseEntity<>("Неправилен sessionToken",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    private User authenticateAndReturnUser(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserAuthenticationException("Грешен email или парола.");
        }
        if(encoder.matches(password, user.getPassword())) {
            return user;
        }else {
            throw new UserAuthenticationException("Грешен email или парола.");
        }
    }

    private static String generateNewToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
