package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Entity.Category;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Service.CategoryService;
import com.expensetracker.expense_tracker.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String categories(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("categories", categoryService.getCategoriesByUser(user));
        model.addAttribute("category", new Category());
        return "categories";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(null);
        category.setUser(user);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }
}
