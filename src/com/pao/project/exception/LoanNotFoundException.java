package com.pao.project.exception;

import com.pao.project.model.Loan;

public class LoanNotFoundException extends RuntimeException{
    public LoanNotFoundException(String message){
        super(message);
    }
}
