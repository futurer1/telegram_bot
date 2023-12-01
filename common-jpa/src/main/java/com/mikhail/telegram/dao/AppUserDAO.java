package com.mikhail.telegram.dao;

import com.mikhail.telegram.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}