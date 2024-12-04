package com.nikita.varlakov.crudproject.repository;

import com.nikita.varlakov.crudproject.model.SensorStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SensorStatisticRepository extends JpaRepository<SensorStatistic, Long> {
    List<SensorStatistic> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
