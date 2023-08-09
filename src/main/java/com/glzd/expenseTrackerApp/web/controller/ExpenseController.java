package com.glzd.expenseTrackerApp.web.controller;

import com.glzd.expenseTrackerApp.business.model.Expense;
import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.business.services.ExpenseService;
import com.glzd.expenseTrackerApp.business.services.ExpenseTypeService;
import com.glzd.expenseTrackerApp.business.exceptions.ExpenseTypeAlreadyExistsException;
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
    private static final int PAGE_SIZE = 8; //number of records per page

    public ExpenseController(ExpenseService expenseService, ExpenseTypeService expenseTypeService) {
        this.expenseService = expenseService;
        this.expenseTypeService = expenseTypeService;
    }


    /**
     * This method retrieves the total amount of all expenses and then adds it into a model
     * so that it could be accessed and displayed in view
     *
     *
     * @return The total amount of all expenses as a {@link BigDecimal} value.
     */
    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount(){
        Iterable<Expense> expenses = expenseService.findAll();
        return expenseService.getTotalAmount(expenses);
    }


    /**
     * This method fetches a page of expenses from the expense service using the
     * provided pagination settings. The fetched expenses are then added to the
     * model, making them available for display in the view.
     *
     * @param page The pagination settings, including page number and page size.
     * @return A Page object containing the fetched expenses.
     */
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


    /**
     * Handles the submission of a new expense type.
     *
     * @param expenseType The submitted ExpenseType object.
     * @param errors      The validation errors from the submitted form.
     * @param model       The model to add attributes and messages.
     * @return A view name for redirection based on the processing outcome.
     */
    @PostMapping("/newExpenseType")
    public String addExpenseType(@Valid ExpenseType expenseType, Errors errors, Model model){
        // Checking for validation errors
        if (errors.hasErrors()) {
            //returns same page to keep data in form fields and show errors
            return "newExpenseType";
        }
        try {
            // Attempt to save the new expense type
            expenseTypeService.save(expenseType);
        } catch (ExpenseTypeAlreadyExistsException e) {
            // Add error message to the model in case of existing expense type
            model.addAttribute("errorMessage", e.getMessage());
            return "newExpenseType";
        }
        // Redirect to the "newExpenseType" page after successful processing
        return "redirect:/newExpenseType";
    }


    /**
     * Handles the deletion of an individual expense by its unique identifier.
     *
     * @param id The unique identifier of the expense to be deleted.
     * @return A view name for redirection to the "expenses" page.
     */
    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteById(id);
        return "redirect:/expenses";
    }

    /**
     *
     * Handles the deletion of an individual expense type by its unique identifier.
     *
     * @param id The unique identifier of the expense type to be deleted.
     * @return A view name for redirection to the "newExpenseType" page.
     */
    @PostMapping(value = "newExpenseType/delete/{id}")
    public String deleteExpenseType(@PathVariable("id") Long id){
        expenseTypeService.deleteById(id);
        return "redirect:/newExpenseType";
    }

    /**
     * Handles the addition of a new expense record.
     *
     * @param expense The expense object to be added.
     * @param errors  The validation errors, if any, from the submitted expense.
     * @return A view name for redirection to the "expenses" page.
     */
    @PostMapping("/AddExpense")
    public String addExpense(@Valid Expense expense, Errors errors){
        if (errors.hasErrors()) {
            return "expenses"; //returns same page to keep data in form fields and to show errors
        }
        expenseService.save(expense);

        return "redirect:/expenses";
    }


    /**
     * Displays the update expense form.
     *
     * The expense data is then pre-populated in the updateExpense form fields for editing.
     * The user is presented with the pre-filled form to make updates to the expense details.
     *
     * @param id    The ID of the expense to be updated.
     * @param model The Spring model to add attributes for the view.
     * @return The name of the view for updating an expense record.
     */
    @GetMapping("/update/{id}")
    public String showUpdateExpenseForm(@PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);

        // Retrieve the expense object by ID and add it to the model
        Expense expense = expenseService.findById(longId);
        model.addAttribute("expense", expense);

        return "updateExpense";

    }

    @PostMapping("/update")
    public String updateExpense(@Valid Expense expense, Errors errors) {
        if (errors.hasErrors()) {
            return "updateExpense";
        }
        expenseService.save(expense); // Save the updated expense object to the database
        return "redirect:/expenses";
    }

    /**
     * Handles filtering and displaying expenses based on the provided filter parameters.
     *
     * @param year         The year filter for expenses.
     * @param month        The month filter for expenses.
     * @param expenseType  The expense type filter for expenses.
     * @param model        Model object for adding attributes.
     * @param page         Pageable object specifying the requested page number and page size.
     * @return The view name to display the filtered expenses along with filter options.
     */
    @GetMapping("/expenses/filter")
    public String showFilteredExpenses(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "month", required = false) Month month,
                                       @RequestParam(name = "expenseTypeFilter", required = false) String expenseType,
                                       Model model, @PageableDefault(size = PAGE_SIZE) Pageable page) {

        Page<Expense> expenses;
        String monthToDisplay = null;
        String yearToDisplay = null;

        // If all filters are provided (year, month, and expense type)
        if (year != null && month != null && expenseType != null && !expenseType.isEmpty()) {
            expenses = expenseService.getExpensesByYearMonthAndType(year, month, expenseType, page);
            monthToDisplay = Helpers.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        }
        // If only year and month filters are provided
        else if (year != null && month != null) {
            expenses = expenseService.getExpensesByYearMonth(year, month, page);
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

    /**
     * This method retrieves all expenses from the database, converts them to CSV format,
     * and returns the CSV data as a downloadable file.
     *
     * @return ResponseEntity containing the CSV data as a downloadable file attachment.
     */
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
