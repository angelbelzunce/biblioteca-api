package com.angel.biblioteca_api.service;

import com.angel.biblioteca_api.exception.InsufficientStockException;
import com.angel.biblioteca_api.exception.LoanAlreadyReturnedException;
import com.angel.biblioteca_api.exception.ResourceNotFoundException;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.model.Loan;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.repository.BookRepository;
import com.angel.biblioteca_api.repository.LoanRepository;
import com.angel.biblioteca_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Tests de LoanService sin arrancar Spring ni tocar la base de datos.
// Activamos Mockito para poder usar @Mock e @InjectMocks.
@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    // Repositorios falsos. Solo responden lo que les digamos en cada test,
    // y si no les decimos nada devuelven null o vacío.
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    // El servicio real, que recibe los repositorios falsos de arriba.
    @InjectMocks
    private LoanService loanService;

    @Test
    void createLoan_userNotFound_throwsException() {
        // Cuando busque el usuario 99, que no encuentre nada (Optional vacío).
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Comprobamos que crear el préstamo lanza esta excepción.
        assertThrows(ResourceNotFoundException.class, () -> loanService.createLoan(99L, 1L));
    }

    @Test
    void createLoan_bookNotFound_throwsException() {
        // El usuario existe pero el libro no.
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.createLoan(1L, 99L));
    }

    @Test
    void createLoan_insufficientStock_throwsException() {
        // Todo existe pero no quedan ejemplares del libro.
        User user = new User();
        Book book = new Book();
        book.setStock(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(InsufficientStockException.class, () -> loanService.createLoan(1L, 1L));

        // Y como ha fallado, no se tiene que haber guardado ningún préstamo.
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void returnLoan_loanAlreadyReturned_throwsException() {
        // Un préstamo que ya estaba devuelto no se puede devolver otra vez.
        Loan loan = new Loan();
        loan.setReturned(true);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Comprobamos que lanza la excepción de préstamo ya devuelto
        assertThrows(LoanAlreadyReturnedException.class, () -> loanService.returnLoan(1L));
    }

    @Test
    void createLoan_validData_createsLoanAndDecreasesStock() {
        // Caso normal: usuario y libro existen y hay 3 ejemplares.
        User user = new User();
        Book book = new Book();
        book.setStock(3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Al guardar un préstamo, que devuelva ese mismo préstamo (como la base de datos real).
        // Usamos any() porque el préstamo lo crea el servicio y no lo tenemos desde aquí.
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan result = loanService.createLoan(1L, 1L);

        // El stock baja de 3 a 2 y el préstamo se crea sin devolver.
        assertEquals(2, book.getStock());
        assertNotNull(result);
        assertFalse(result.getReturned());

        // assertSame comprueba que es el mismo objeto, no solo uno parecido.
        assertSame(user, result.getUser());
        assertSame(book, result.getBook());

        // Comprobamos que el servicio ha llamado a save para guardar el libro y el préstamo.
        verify(bookRepository).save(book);
        verify(loanRepository).save(any(Loan.class));
    }
}
