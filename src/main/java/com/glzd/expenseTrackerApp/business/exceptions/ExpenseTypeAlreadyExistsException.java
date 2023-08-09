package com.glzd.expenseTrackerApp.business.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ExpenseTypeAlreadyExistsException extends Exception {
    public ExpenseTypeAlreadyExistsException(String message) {
        super(message);
    }

}
