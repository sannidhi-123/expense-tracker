package com.expensetracker.expense_tracker.Repository;

import com.expensetracker.expense_tracker.Entity.Budget;
import com.expensetracker.expense_tracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserAndMonthAndYear(User user, int month, int year);
}

