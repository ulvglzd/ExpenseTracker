package com.glzd.demo.testcrudapp.data;

import com.glzd.demo.testcrudapp.business.model.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.context.annotation.RequestScope;


public interface ExpenseRepository extends CrudRepository<Expense, Long> {
}
