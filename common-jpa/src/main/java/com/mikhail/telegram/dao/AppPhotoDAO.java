package com.mikhail.telegram.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mikhail.telegram.entity.AppPhoto;
public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}