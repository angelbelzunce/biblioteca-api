package com.angel.biblioteca_api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcrypt {
    public static void main(String[] args) {
        String rawPassword = "1234";
        String storedHash = "$2a$10$TQePtwN/Mp4ZoXwpJQAcxOs/rZMw7z.nxNdzyALNesqra92BfP6TK"; // pega aquí el hash completo de la BD

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(rawPassword, storedHash);

        System.out.println("¿Coincide? " + matches);
    }
}