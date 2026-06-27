package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.model.Loan;
import com.angel.biblioteca_api.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan create(@RequestParam Long userId, @RequestParam Long bookId){
        return loanService.createLoan(userId, bookId);
    }

    @PutMapping("/{id}/return")
    public Loan returnLoan (@PathVariable Long id){
        return loanService.returnLoan(id);
    }

    @GetMapping("/user/{userId}")
    public List<Loan> getByUSer(@PathVariable Long userId){
        return loanService.findByUserId(userId);
    }

}
