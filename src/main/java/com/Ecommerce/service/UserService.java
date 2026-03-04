package com.Ecommerce.service;


import com.Ecommerce.entity.User;
import com.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register (User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole()== null){
            user.setRole("USER");

        }
        return repository.save(user);
    }

    public String login (String email, String password){

        return jwtService.generateToken(email);
    }
}
