package com.anunciosloc.anunciosloc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anunciosloc.anunciosloc.model.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
