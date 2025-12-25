package com.expensetracker.expense_tracker.Repository;

import com.expensetracker.expense_tracker.Entity.Category;
import com.expensetracker.expense_tracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);
}
