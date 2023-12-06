package com.mikhail.telegram.dao;

import com.mikhail.telegram.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findUserByTelegramUserId(Long id);
    Optional<AppUser> findUserById(Long id);
    Optional<AppUser> findUserByEmail(String email);
}