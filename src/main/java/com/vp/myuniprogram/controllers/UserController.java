package com.vp.myuniprogram.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vp.myuniprogram.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    @PutMapping("/{id}/change/password")
    public ResponseEntity registerAdmin(@PathVariable Integer id, @RequestBody ObjectNode oldPasswordAndNewPasswordInJson) {
        return userService.updateUserPassword(id, oldPasswordAndNewPasswordInJson);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }

}
