package com.glzd.expenseTrackerApp.web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(Model model) {
        String errorMessage = "Oops! Something went wrong.";
        model.addAttribute("errorMessage", errorMessage);
        return "errorPage";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleGetMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return "redirect:/expenses";
    }

}

