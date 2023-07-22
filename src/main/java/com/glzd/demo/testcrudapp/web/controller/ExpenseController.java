package com.glzd.demo.testcrudapp.web.controller;

import com.glzd.demo.testcrudapp.business.services.ExpenseService;
import com.glzd.demo.testcrudapp.business.model.Expense;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping
public class ExpenseController {


    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping("/expenses")
    public String showExpenses(Model model){

        Iterable<Expense> expenses = expenseService.findAll();
        BigDecimal totalAmount = expenseService.getTotalAmount();

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("expenses", expenses);

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
        Expense expense = expenseService.findById(longId);
        model.addAttribute("expense", expense);
        return "updateExpense"; // Return the Thymeleaf template for the update expense page

    }

    @PostMapping("/update")
    public String updateExpense(@ModelAttribute @Valid Expense expense, Errors errors) {
        if (errors.hasErrors()) {
            return "updateExpense"; // Return the Thymeleaf template for the update expense page
        }
        expenseService.save(expense); // Save the updated expense object to the database
        return "redirect:/expenses";
    }

}
