package com.glzd.demo.testcrudapp.web.controller;

import com.glzd.demo.testcrudapp.business.services.ExpenseService;
import com.glzd.demo.testcrudapp.business.model.Expense;
import com.glzd.demo.testcrudapp.web.helpers.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Controller
@RequestMapping
public class ExpenseController {


    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping("/expenses")
    public String showExpenses(Model model){
        populateExpensesData(model);
        return "/expenses";
    }

    @ModelAttribute
    public Expense getExpense(){
        return new Expense();
    }

    private void populateExpensesData(Model model) {
        Iterable<Expense> expenses = expenseService.findAll();
        BigDecimal totalAmount = expenseService.getTotalAmount(expenses);

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("expenses", expenses);
    }


    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteById(id);
        return "redirect:/expenses";
    }

    @PostMapping("/AddExpense")
    public String addExpense(@Valid Expense expense, Errors errors, Model model){
        if (errors.hasErrors()) {
            // If there are validation errors, add the expenses data to the model
            populateExpensesData(model);
            return "expenses";
        }
        expenseService.save(expense);
        return "redirect:/expenses";
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

    //Get expenses by month
    @GetMapping("/expenses/month")
    public String showExpensesByMonth(@RequestParam(name = "year", required = false) Integer year,
                                      @RequestParam(name = "month", required = false) Month month,
                                      Model model) {
        Iterable<Expense> expenses;
        String monthToDisplay = null;
        String yearToDisplay = null;

        if (year != null && month != null) {
            expenses = expenseService.getExpensesByMonth(year, month);
            monthToDisplay = Helpers.toSentenceCase(month.toString());
            yearToDisplay = year.toString();

        } else {
            expenses = expenseService.findAll();
        }

        BigDecimal totalAmount = expenseService.getTotalAmount(expenses);

        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("expenses", expenses);
        model.addAttribute("month", monthToDisplay);
        model.addAttribute("year", yearToDisplay);

        return "expenses";
    }

}
