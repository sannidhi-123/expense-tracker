package com.expensetracker.expense_tracker.Service;

import com.expensetracker.expense_tracker.Entity.Budget;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // ✅ SAVE OR UPDATE BUDGET (CORE LOGIC)
    public Budget saveOrUpdateBudget(User user, int month, int year, double amount) {

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(user, month, year)
                .orElse(new Budget());

        budget.setUser(user);
        budget.setMonth(month);
        budget.setYear(year);
        budget.setLimitAmount(amount);

        return budgetRepository.save(budget);
    }

    // ✅ GET MONTHLY BUDGET
    public Optional<Budget> getMonthlyBudget(User user, int month, int year) {
        return budgetRepository.findByUserAndMonthAndYear(user, month, year);
    }
}
