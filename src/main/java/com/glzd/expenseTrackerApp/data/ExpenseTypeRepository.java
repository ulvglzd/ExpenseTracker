package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {
    //checks if the custom user input expense type exists in db or not
    @Query("SELECT COUNT(et) > 0 FROM ExpenseType et WHERE LOWER(et.expenseCategory) = LOWER(:expenseCategory)")
    boolean existsByExpenseCategoryIgnoreCase(String expenseCategory);
}
