package com.glzd.demo.testcrudapp.business.services;

import com.glzd.demo.testcrudapp.business.model.Expense;
import com.glzd.demo.testcrudapp.data.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense save(Expense entity) {
        return expenseRepository.save(entity);
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);

    }

    public Iterable<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Iterable<Expense> findAllById(Iterable<Long> longs) {
        return expenseRepository.findAllById(longs);
    }

    public void deleteById(Long aLong) {
        expenseRepository.deleteById(aLong);
    }

    public BigDecimal getTotalAmount(Iterable<Expense> expenses){
        BigDecimal totalAmount = StreamSupport.
                stream(expenses.spliterator(), false)
                .toList()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAmount;
    }



    public Iterable<Expense> getExpensesByMonth(int year, Month month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseRepository.findByDateBetween(startDate, endDate);
    }

}
