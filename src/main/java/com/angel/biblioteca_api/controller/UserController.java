package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.UserRequest;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.service.UserService;
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
    public User register(@RequestBody UserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(User.Role.USER);
        return userService.register(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.findById(id);
    }
}
