package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Entity.Budget;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Service.BudgetService;
import com.expensetracker.expense_tracker.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    // 📄 SHOW BUDGET PAGE
    @GetMapping
    public String budgetPage(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName()).orElse(null);

        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        budgetService.getMonthlyBudget(user, currentMonth, currentYear)
                .ifPresent(budget -> model.addAttribute("budget", budget));

        model.addAttribute("month", currentMonth);
        model.addAttribute("year", currentYear);

        return "budget";
    }

    // 💾 SAVE / UPDATE BUDGET
    @PostMapping("/save")
    public String saveBudget(@RequestParam int month,
                             @RequestParam int year,
                             @RequestParam double amount,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(principal.getName()).orElse(null);

        budgetService.saveOrUpdateBudget(user, month, year, amount);

        // ✅ Flash message
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Budget updated successfully for " + month + "/" + year
        );

        return "redirect:/budget";
    }

}
