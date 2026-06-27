package com.angel.biblioteca_api.service;

import com.angel.biblioteca_api.exception.ResourceNotFoundException;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;

    public User register (User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("A user with this email already exists");
        }
        // TODO: encriptar la contraseña aquí cuando lleguemos a la Fase 3 (seguridad)
        return userRepository.save(user);
    }

    public User findById (Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
