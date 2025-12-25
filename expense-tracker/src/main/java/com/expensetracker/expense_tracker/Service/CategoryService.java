package com.expensetracker.expense_tracker.Service;

import com.expensetracker.expense_tracker.Entity.Category;
import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findByUser(user);
    }
}
