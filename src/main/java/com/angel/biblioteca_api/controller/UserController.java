package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.UserResponse;
import com.angel.biblioteca_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id){
        return UserResponse.fromEntity(userService.findById(id));
    }
}
