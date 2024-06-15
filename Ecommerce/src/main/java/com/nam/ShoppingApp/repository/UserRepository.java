package com.nam.ShoppingApp.repository;

import com.nam.ShoppingApp.entity.User;
import com.nam.ShoppingApp.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    Optional<User> findByRole(UserRole userRole);
}
