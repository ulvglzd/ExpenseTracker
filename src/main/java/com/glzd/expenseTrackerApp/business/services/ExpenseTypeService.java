package com.glzd.expenseTrackerApp.business.services;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.data.ExpenseTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseTypeService {
    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeService(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    public ExpenseType save(ExpenseType entity) {
        return expenseTypeRepository.save(entity);
    }

    public Iterable<ExpenseType> findAll() {
        return expenseTypeRepository.findAll();
    }

    public void deleteById(Long aLong) {
        expenseTypeRepository.deleteById(aLong);
    }

    public void delete(ExpenseType entity) {
        expenseTypeRepository.delete(entity);
    }
}
