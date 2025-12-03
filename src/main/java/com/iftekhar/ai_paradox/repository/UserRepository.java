package com.iftekhar.ai_paradox.repository;


import com.iftekhar.ai_paradox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
