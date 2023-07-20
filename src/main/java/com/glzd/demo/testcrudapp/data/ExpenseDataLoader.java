package com.glzd.demo.testcrudapp.data;

import com.glzd.demo.testcrudapp.business.model.Expense;
import com.glzd.demo.testcrudapp.business.model.ExpenseType;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequestScope
@Data
public class ExpenseDataLoader {

    List<Expense> expenseList = List.of(
            new Expense(null, "ishiq", ExpenseType.HOME, new BigDecimal("25"), LocalDate.of(2023, 7, 5)),
            new Expense(null, "qaz", ExpenseType.HOME, new BigDecimal("15"), LocalDate.of(2023, 7, 5)),
            new Expense(null, "denize getmek", ExpenseType.ENTERTAINMENT, new BigDecimal("25"), LocalDate.of(2023, 7, 5))
    );





}
