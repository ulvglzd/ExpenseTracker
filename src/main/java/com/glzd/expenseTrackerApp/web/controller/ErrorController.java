package com.glzd.expenseTrackerApp.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleExpenseNotFoundException(EntityNotFoundException ex, Model model) {
        String errorMessage = "Oops! Something went wrong.";
        model.addAttribute("errorMessage", errorMessage);
        return "errorPage";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleGetMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return "redirect:/expenses";
    }



}

