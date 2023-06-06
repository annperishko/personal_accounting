package com.example.accounting_service.repositories;

import com.example.accounting_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
}
