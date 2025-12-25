package com.expensetracker.expense_tracker.Repository;

import com.expensetracker.expense_tracker.Entity.Expense;
import com.expensetracker.expense_tracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndType(User user, String type);
}
