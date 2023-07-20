package com.glzd.demo.testcrudapp.business.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please specify the name of expense")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please specify the type of expense")
    private ExpenseType expenseType;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=15, fraction=2)
    @NotNull(message = "Please specify an amount")
    private BigDecimal amount;

    @NotNull(message = "Date cannot be empty!")
    private LocalDate date;


}
