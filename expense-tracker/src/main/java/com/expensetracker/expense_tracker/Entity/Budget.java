package com.expensetracker.expense_tracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "budgets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "month", "year"}
        )
)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int month;   // 1 - 12

    @Column(nullable = false)
    private int year;    // e.g. 2025

    @Column(nullable = false)
    private double limitAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Constructors
    public Budget() {}

    public Budget(int month, int year, double limitAmount, User user) {
        this.month = month;
        this.year = year;
        this.limitAmount = limitAmount;
        this.user = user;
    }

    // ✅ Getters & Setters
    public Long getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
