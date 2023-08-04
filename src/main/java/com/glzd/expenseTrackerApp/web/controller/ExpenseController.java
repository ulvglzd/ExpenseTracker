package com.glzd.expenseTrackerApp.web.controller;

import com.glzd.expenseTrackerApp.business.model.Expense;
import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.business.services.ExpenseService;
import com.glzd.expenseTrackerApp.business.services.ExpenseTypeService;
import com.glzd.expenseTrackerApp.business.services.exceptions.ExpenseTypeAlreadyExistsException;
import com.glzd.expenseTrackerApp.web.helpers.Helpers;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;

@Controller
@RequestMapping
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseTypeService expenseTypeService;
    private static final int PAGE_SIZE = 3;

    public ExpenseController(ExpenseService expenseService, ExpenseTypeService expenseTypeService) {
        this.expenseService = expenseService;
        this.expenseTypeService = expenseTypeService;
    }

    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount(){
        Iterable<Expense> expenses = expenseService.findAll();
        return expenseService.getTotalAmount(expenses);
    }

    @ModelAttribute("expenses")
    public Page<Expense> getExpenses(@PageableDefault(size = PAGE_SIZE) Pageable page){
        return expenseService.findAll(page);
    }

    @GetMapping("/expenses")
    public String showExpenses(){
        return "/expenses";
    }

    @ModelAttribute("expenseTypes")
    public Iterable<ExpenseType> getExpenseTypes() {
        return expenseTypeService.findAll();
    }

    @ModelAttribute
    public Expense getExpense(){
        return new Expense();
    }

    @ModelAttribute
    public ExpenseType getExpenseType(){
        return new ExpenseType();
    }

    @GetMapping("/newExpenseType")
    public String showExpenseTypes(){
        return "/newExpenseType";
    }

    @PostMapping("/newExpenseType")
    public String addExpenseType(@Valid ExpenseType expenseType, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "newExpenseType"; //returns same page to keep data in form fields
        }
        try {
            expenseTypeService.save(expenseType);
        } catch (ExpenseTypeAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "newExpenseType";
        }
        return "redirect:/newExpenseType";
    }


    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteById(id);
        return "redirect:/expenses";
    }

    @PostMapping(value = "newExpenseType/delete/{id}")
    public String deleteExpenseType(@PathVariable("id") Long id){
        expenseTypeService.deleteById(id);
        return "redirect:/newExpenseType";
    }

    @PostMapping("/AddExpense")
    public String addExpense(@Valid Expense expense, Errors errors){
        if (errors.hasErrors()) {
            return "expenses"; //returns same page to keep data in form fields
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
    public String updateExpense(@Valid Expense expense, Errors errors) {
        if (errors.hasErrors()) {
            return "updateExpense"; // Return the Thymeleaf template for the update expense page
        }
        expenseService.save(expense); // Save the updated expense object to the database
        return "redirect:/expenses";
    }

    //Filtering the expenses by date and/or expense type
    @GetMapping("/expenses/filter")
    public String showFilteredExpenses(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "month", required = false) Month month,
                                       @RequestParam(name = "expenseTypeFilter", required = false) String expenseType,
                                       Model model, @PageableDefault(size = PAGE_SIZE) Pageable page) {

        Page<Expense> expenses;
        String monthToDisplay = null;
        String yearToDisplay = null;
        System.out.println(expenseType);

        // If all filters are provided (year, month, and expense type)
        if (year != null && month != null && expenseType != null && !expenseType.isEmpty()) {
            expenses = expenseService.getExpensesByYearMonthAndType(year, month, expenseType, page);
            monthToDisplay = Helpers.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        }
        // If only year and month filters are provided
        else if (year != null && month != null) {
            expenses = expenseService.getExpensesByMonth(year, month, page);
            monthToDisplay = Helpers.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        }
        // If only expense type filter is provided
        else if (expenseType != null && !expenseType.isEmpty()) {
            expenses = expenseService.getExpensesByType(expenseType, page);
        }
        // If no filters are provided, show all expenses
        else {
            expenses = expenseService.findAll(page);
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("month", monthToDisplay);
        model.addAttribute("year", yearToDisplay);
        model.addAttribute("expenseType", expenseType);

        return "expenses";
    }

    @GetMapping("/downloadExpenses")
    public ResponseEntity<Resource> downloadExpenses() {
        // Get all expenses from the database
        Iterable<Expense> expenses = expenseService.findAll();

        // Convert expenses to a CSV format
        String csvData = expenseService.convertToCSV(expenses);

        // Set the CSV data as a ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(csvData.getBytes());

        // Return the CSV data as a downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=expenses.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }



}
