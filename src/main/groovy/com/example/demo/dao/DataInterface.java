package com.example.demo.dao;

import com.example.demo.model.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataInterface extends JpaRepository<Users, Integer> {
    Optional<Users> findByuserName(@NotBlank String userName);
}
