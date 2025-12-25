package com.expensetracker.expense_tracker.Service;

import com.expensetracker.expense_tracker.Entity.User;
import com.expensetracker.expense_tracker.Repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        System.out.println("🔍 Login attempt with email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ User NOT found in DB");
                    return new UsernameNotFoundException("User not found");
                });

        System.out.println("✅ User found in DB");
        System.out.println("📧 DB Email: " + user.getEmail());
        System.out.println("🔐 DB Password (encoded): " + user.getPassword());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
