package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseTypeRepository extends CrudRepository<ExpenseType, Long> {
}
