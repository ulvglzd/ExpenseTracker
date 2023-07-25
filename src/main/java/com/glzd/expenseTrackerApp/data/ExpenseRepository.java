package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.Expense;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    Iterable<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expense> findAllByOrderByCreationDateDesc();


}
