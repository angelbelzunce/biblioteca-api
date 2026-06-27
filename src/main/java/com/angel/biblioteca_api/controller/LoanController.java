package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.LoanResponse;
import com.angel.biblioteca_api.model.Loan;
import com.angel.biblioteca_api.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse create(@RequestParam Long userId, @RequestParam Long bookId){
        return LoanResponse.fromEntity(loanService.createLoan(userId, bookId));
    }

    @PutMapping("/{id}/return")
    public LoanResponse returnLoan (@PathVariable Long id){
        return LoanResponse.fromEntity(loanService.returnLoan(id));
    }

    @GetMapping("/user/{userId}")
    public List<LoanResponse> getByUSer(@PathVariable Long userId){
        return loanService.findByUserId(userId).stream()
                .map(LoanResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
