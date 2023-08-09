package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {

    /**
     * Check if an ExpenseType with the given expense category (case-insensitive) exists.
     *
     * This method queries the database to determine whether an ExpenseType record
     * with the specified expense category (ignoring case) exists or not.
     *
     * @param expenseCategory The expense category to check for existence.
     * @return {@code true} if an ExpenseType with the specified expense category
     *         (case-insensitive) exists, otherwise {@code false}.
     */
    @Query("SELECT COUNT(et) > 0 FROM ExpenseType et WHERE LOWER(et.expenseCategory) = LOWER(:expenseCategory)")
    boolean existsByExpenseCategoryIgnoreCase(String expenseCategory);
}
