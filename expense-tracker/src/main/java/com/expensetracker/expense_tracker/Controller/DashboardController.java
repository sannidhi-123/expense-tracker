package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Entity.Budget;
import com.expensetracker.expense_tracker.Entity.Expense;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Service.BudgetService;
import com.expensetracker.expense_tracker.Service.ExpenseService;
import com.expensetracker.expense_tracker.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final UserService userService;

    public DashboardController(
            ExpenseService expenseService,
            BudgetService budgetService,
            UserService userService
    ) {
        this.expenseService = expenseService;
        this.budgetService = budgetService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName()).orElse(null);
        List<Expense> expenses = expenseService.getExpensesByUser(user);

        double totalIncome = expenses.stream()
                .filter(e -> "INCOME".equals(e.getType()))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalExpense = expenses.stream()
                .filter(e -> "EXPENSE".equals(e.getType()))
                .mapToDouble(Expense::getAmount)
                .sum();

        // CATEGORY-WISE EXPENSE
        Map<String, Double> categoryMap = expenses.stream()
                .filter(e -> "EXPENSE".equals(e.getType()) && e.getCategory() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // MONTHLY TREND
        Map<String, Double> monthlyMap = expenses.stream()
                .filter(e -> "EXPENSE".equals(e.getType()))
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth().name(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // 🔥 BUDGET CALCULATION (NO CLAMPING)
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        Budget budget = budgetService
                .getMonthlyBudget(user, month, year)
                .orElse(null);

        double budgetLimit = 0;
        double budgetUsed = 0;
        double budgetRemaining = 0;
        double budgetPercent = 0;

        if (budget != null) {
            budgetLimit = budget.getLimitAmount();

            budgetUsed = expenses.stream()
                    .filter(e -> "EXPENSE".equals(e.getType()))
                    .filter(e -> e.getDate().getMonthValue() == month &&
                            e.getDate().getYear() == year)
                    .mapToDouble(Expense::getAmount)
                    .sum();

            budgetRemaining = budgetLimit - budgetUsed; // ❗ CAN BE NEGATIVE
            budgetPercent = (budgetUsed / budgetLimit) * 100; // ❗ CAN EXCEED 100
        }

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", totalIncome - totalExpense);

        model.addAttribute("categoryData", categoryMap);
        model.addAttribute("monthlyData", monthlyMap);

        model.addAttribute("budgetLimit", budgetLimit);
        model.addAttribute("budgetUsed", budgetUsed);
        model.addAttribute("budgetRemaining", budgetRemaining);
        model.addAttribute("budgetPercent", budgetPercent);

        return "dashboard";
    }
}
