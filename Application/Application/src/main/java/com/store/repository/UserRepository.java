package com.store.repository;

import com.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by username
    User findByUsername(String username);

    // Find a user by email
    User findByEmail(String email);
}
