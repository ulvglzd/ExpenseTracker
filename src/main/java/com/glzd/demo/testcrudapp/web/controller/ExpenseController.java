package com.glzd.demo.testcrudapp.web.controller;

import com.glzd.demo.testcrudapp.business.ExpenseService.ExpenseService;
import com.glzd.demo.testcrudapp.business.model.Expense;
import com.glzd.demo.testcrudapp.data.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class ExpenseController {

    @Autowired
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @ModelAttribute("expenses")
    public Iterable<Expense> getExpenses(){
        return expenseService.findAll();
    }


    @GetMapping("/expenses")
    public String showExpenses(){
        return "expenses";
    }

    @ModelAttribute
    public Expense getExpense(){
        return new Expense();
    }


    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteById(id);
        return "redirect:/expenses";
    }



    @PostMapping("/expenses")
    public String addExpense(@Valid Expense expense, Errors errors){
        if (!errors.hasErrors()) {
            try {
                expenseService.save(expense);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "redirect:/expenses";
        }
        return "expenses";
    }

    @GetMapping("/update/{id}")
    public String showUpdateExpenseForm(@PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);
        // Add the expense object to the model to pre-populate the form fields
        try {
            Expense expense = expenseService.findById(longId).orElseThrow(EntityNotFoundException::new);
            model.addAttribute("expense", expense);
            return "updateExpense"; // Return the Thymeleaf template for the update expense page
        } catch (EntityNotFoundException e) {
            String errorMessage = "Sorry, Expense was not found.";
            model.addAttribute("errorMessage", errorMessage);
            return "errorPage";
        }

    }

    @PostMapping("/update")
    public String updateExpense(@ModelAttribute @Valid Expense expense, Errors errors) {
        // Save the updated expense object to the database
        if (errors.hasErrors()) {
            System.out.println("expense date = " + expense.getDate());
            return "updateExpense"; // Return the Thymeleaf template for the update expense page
        }
        try {
            expenseService.save(expense);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/expenses";
    }

}
