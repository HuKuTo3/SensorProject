package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.Sensor;
import org.postgresql.util.PGobject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.varlakov.crudproject.model.SensorStatistic;
import com.nikita.varlakov.crudproject.repository.SensorStatisticRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final SensorService sensorService; // Используем ваш уже существующий сервис
    private final SensorStatisticRepository statisticRepository;
    private final JdbcTemplate jdbcTemplate;

    public StatisticService(SensorService sensorService, SensorStatisticRepository statisticRepository, JdbcTemplate jdbcTemplate) {
        this.sensorService = sensorService;
        this.statisticRepository = statisticRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void collectAndSaveStatistics() {
        try {
            List<Sensor> sensors = sensorService.getAllSensors();

            if (sensors == null || sensors.isEmpty()) {
                System.out.println("No sensors available for statistics.");
                return;
            }

            int totalCount = sensors.size();

            Map<String, Long> typeStatistics = sensors.stream()
                    .collect(Collectors.groupingBy(
                            sensor -> sensor.getType().toString(),
                            Collectors.counting()
                    ));

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonTypeStatistics = objectMapper.writeValueAsString(typeStatistics);

            String insertQuery = "INSERT INTO sensor_statistics (date, total_count, type_statistics) VALUES (?, ?, ?::jsonb)";
            jdbcTemplate.update(insertQuery, LocalDate.now(), totalCount, jsonTypeStatistics);

            System.out.println("Statistics saved successfully.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public List<SensorStatistic> getStatistics(LocalDate startDate, LocalDate endDate) {
        return statisticRepository.findByDateBetween(startDate, endDate);
    }
}
