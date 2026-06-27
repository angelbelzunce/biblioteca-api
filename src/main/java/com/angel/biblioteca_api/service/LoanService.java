package com.angel.biblioteca_api.service;

import com.angel.biblioteca_api.exception.InsufficientStockException;
import com.angel.biblioteca_api.exception.ResourceNotFoundException;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.model.Loan;
import com.angel.biblioteca_api.model.User;
import com.angel.biblioteca_api.repository.BookRepository;
import com.angel.biblioteca_api.repository.LoanRepository;
import com.angel.biblioteca_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public Loan createLoan(Long userId, Long bookId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (book.getStock() <= 0){
            throw new InsufficientStockException("No copies available for book: " + book.getTitle());
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setReturned(false);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnLoan(Long loanId){
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

        if (Boolean.TRUE.equals(loan.getReturned())){
            throw new RuntimeException("This loan has already been returned");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);

    }

    public List<Loan> findByUserId(Long userId){
        return loanRepository.findByUserId(userId);
    }
}
