package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.SensorStatistic;
import java.time.LocalDate;
import java.util.List;

public interface IStatisticService {
    List<SensorStatistic> getStatistics(LocalDate startDate, LocalDate endDate);
    void collectAndSaveStatistics();
}
