package com.Ecommerce.controller;

import com.Ecommerce.entity.User;
import com.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    @PostMapping("register")
    public User register(@RequestBody User user){
        return service.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                String token = service.login(email, password);
                return token;

        } catch (Exception e) {
            return "Wrong password or user name";
        }
    }
}
