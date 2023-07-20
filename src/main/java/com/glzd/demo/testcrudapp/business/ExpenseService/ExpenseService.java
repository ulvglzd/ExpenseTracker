package com.glzd.demo.testcrudapp.business.ExpenseService;

import com.glzd.demo.testcrudapp.business.model.Expense;
import com.glzd.demo.testcrudapp.business.model.ExpenseType;
import com.glzd.demo.testcrudapp.data.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense save(Expense entity) {
        return expenseRepository.save(entity);
    }

    public Optional<Expense> findById(Long aLong) {
        return expenseRepository.findById(aLong);
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

}
