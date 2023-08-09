package com.glzd.expenseTrackerApp.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        String message = e.getMessage();
        model.addAttribute("errorMessage", message);
        return "error";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleGetMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return "redirect:/expenses";
    }

}

