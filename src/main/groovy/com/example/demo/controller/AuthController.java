package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.schema.LoginDto;
import com.example.demo.schema.RegistrationDto;
import com.example.demo.schema.UnlockDto;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto registrationDto) {
        try{
            Users saveduser = authService.registerUser(registrationDto);
            return ResponseEntity.ok("Successfully Registered" + saveduser.getId());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Registration Failed" + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        String result = authService.login(loginDto);
        return ResponseEntity.ok(result);

    }
    @PostMapping("/unlock")
    public ResponseEntity<String> unlockUser(@RequestBody @Valid UnlockDto unlockDto) {
        boolean isUnlocked = authService.unlockUser(unlockDto.getUserName());
        if (isUnlocked) {
            return ResponseEntity.ok("Successfully Unlocked");
        }else {
            return ResponseEntity.badRequest().body("Unlock Failed");
        }
    }
}
