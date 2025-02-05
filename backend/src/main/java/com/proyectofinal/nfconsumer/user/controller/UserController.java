package com.proyectofinal.nfconsumer.user.controller;


import com.proyectofinal.nfconsumer.user.repository.UserRepository;
import com.proyectofinal.nfconsumer.user.repository.UserResponse;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;

    @GetMapping
    public List<UserResponse> changePassword() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getName(), user.getEmail()))
                .toList();
    }

}
