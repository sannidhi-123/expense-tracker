package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Entity.Expense;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Service.CategoryService;
import com.expensetracker.expense_tracker.Service.ExpenseService;
import com.expensetracker.expense_tracker.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseController(ExpenseService expenseService,
                             UserService userService,
                             CategoryService categoryService) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // 📄 List all expenses
    @GetMapping
    public String listExpenses(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("expenses", expenseService.getExpensesByUser(user));
        return "expenses";
    }

    // ➕ Show add expense form (IMPORTANT)
    @GetMapping("/add")
    public String addExpense(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName()).orElse(null);

        model.addAttribute("expense", new Expense());
        model.addAttribute("categories",
                categoryService.getCategoriesByUser(user));

        return "add-expense";
    }

    // 💾 Save expense
    @PostMapping("/save")
    public String saveExpense(@ModelAttribute Expense expense, Principal principal) {

        User user = userService.findByEmail(principal.getName()).orElse(null);
        expense.setUser(user);
        expense.setDate(LocalDate.now());

        // ✅ IMPORTANT FIX
        if (expense.getCategory() != null &&
                expense.getCategory().getId() == null) {
            expense.setCategory(null);
        }

        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

}
