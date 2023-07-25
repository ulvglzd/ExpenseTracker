package com.glzd.expenseTrackerApp.business.services;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.data.ExpenseTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


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
        Iterable<ExpenseType> allExpenses = expenseTypeRepository.findAll();
        if (((Collection<?>) allExpenses).size() != 0){
            return allExpenses;
        }
        else {
           return List.of(expenseTypeRepository.save(new ExpenseType(null, "Kommunal")));
        }
    }

    public void deleteById(Long aLong) {
        expenseTypeRepository.deleteById(aLong);
    }

    public void delete(ExpenseType entity) {
        expenseTypeRepository.delete(entity);
    }
}
