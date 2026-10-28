package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.exception.LoanAlreadyReturnedException;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.model.Loan;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.security.CustomUserDetailsService;
import com.angel.biblioteca_api.security.JwtUtil;
import com.angel.biblioteca_api.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Tests del LoanController haciendo peticiones HTTP de prueba.
// Spring arranca solo la parte web (este controller y el manejo de errores), sin base de datos.
@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    // Nos permite hacer peticiones al controller sin levantar un servidor, como un Postman dentro del test.
    @Autowired
    private MockMvc mockMvc;

    // El controller necesita un LoanService. Le damos uno falso para no depender de la lógica real.
    @MockitoBean
    private LoanService loanService;

    // Spring también carga el filtro de JWT, y este necesita estas dos piezas para arrancar.
    // Como no estamos probando la seguridad, le damos versiones falsas.
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    void getByUser_returns200() throws Exception {
        // Hacemos un GET a la URL y comprobamos que responde 200 (OK).
        // No preparamos el servicio, así que devuelve una lista vacía y el controller responde [].
        mockMvc.perform(get("/api/loans/user/1")).andExpect(status().isOk());
    }

    @Test
    void returnLoan_loanAlreadyReturned_returns409() throws Exception {
        when(loanService.returnLoan(1L)).thenThrow(new LoanAlreadyReturnedException("This loan has already been returned"));

        mockMvc.perform(put("/api/loans/1/return")).andExpect(status().isConflict());
    }

    @Test
    void create_validData_returns201() throws Exception {
        Loan loan = new Loan();
        User user = new User();
        Book book = new Book();
        book.setTitle("El Quijote");
        loan.setUser(user);
        loan.setBook(book);
        loan.setReturned(false);

        when(loanService.createLoan(1L,1L)).thenReturn(loan);

        mockMvc.perform(post("/api/loans").param("userId", "1").param("bookId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.returned").value(false))
                .andExpect(jsonPath("$.book.title").value("El Quijote"));
    }
}
