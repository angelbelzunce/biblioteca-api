package com.angel.biblioteca_api.repository;

import com.angel.biblioteca_api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    // El repositorio real, conectado a la base de datos H2 en memoria
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_existingUser_returnsUser() {

        // Guardamos un usuario en la base de datos
        User user = new User(null, "Juan", "a@a.com", "0000", User.Role.USER, new ArrayList<>());

        userRepository.save(user);

        // Lo buscamos por email y comprobamos que lo encuentra
        Optional<User> result = userRepository.findByEmail("a@a.com");

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    void findByEmail_nonExistingUser_returnsEmpty() {

        // Buscamos un usuario inexistente por email y comprobamos que no lo encuentra
        Optional<User> result = userRepository.findByEmail("a@a.com");

        assertTrue(result.isEmpty());
    }
}
