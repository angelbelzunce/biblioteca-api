package com.angel.biblioteca_api.dto;

import com.angel.biblioteca_api.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanResponse {
    private Long id;
    private UserResponse user;
    private BookResponse book;
    private LocalDate loanDate;
    private LocalDate renturnDate;
    private Boolean returned;

    public static LoanResponse fromEntity(Loan loan){
        return new LoanResponse(
                loan.getId(),
                UserResponse.fromEntity(loan.getUser()),
                BookResponse.fromEntity(loan.getBook()),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getReturned());
    }
}
