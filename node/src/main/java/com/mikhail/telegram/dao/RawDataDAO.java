package com.mikhail.telegram.dao;

import com.mikhail.telegram.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
