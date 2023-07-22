package com.glzd.demo.testcrudapp.data;

import com.glzd.demo.testcrudapp.business.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    Iterable<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
