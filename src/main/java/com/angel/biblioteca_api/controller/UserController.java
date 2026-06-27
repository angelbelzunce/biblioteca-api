package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.UserRequest;
import com.angel.biblioteca_api.dto.UserResponse;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(User.Role.USER);
        return UserResponse.fromEntity(userService.register(user));
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id){
        return UserResponse.fromEntity(userService.findById(id));
    }
}
