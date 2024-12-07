package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.Sensor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.varlakov.crudproject.model.SensorStatistic;
import com.nikita.varlakov.crudproject.repository.SensorStatisticRepository;
import com.nikita.varlakov.crudproject.service.IStatisticService;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatisticService implements IStatisticService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticService.class);

    private final SensorService sensorService;
    private final SensorStatisticRepository statisticRepository;
    private final JdbcTemplate jdbcTemplate;

    public StatisticService(SensorService sensorService, SensorStatisticRepository statisticRepository, JdbcTemplate jdbcTemplate) {
        this.sensorService = sensorService;
        this.statisticRepository = statisticRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SensorStatistic> getStatistics(LocalDate startDate, LocalDate endDate) {
        return statisticRepository.findByDateBetween(startDate, endDate);
    }

    @Transactional
    public void collectAndSaveStatistics() {
        logger.info("Starting statistics collection");
        try {
            List<Sensor> sensors = sensorService.getAllSensors(PageRequest.of(0, Integer.MAX_VALUE)).getContent();

            if (sensors == null || sensors.isEmpty()) {
                logger.warn("No sensors available for statistics collection");
                return;
            }

            int totalCount = sensors.size();
            logger.debug("Found {} sensors for statistics", totalCount);

            Map<String, Long> typeStatistics = sensors.stream()
                    .collect(Collectors.groupingBy(
                            sensor -> sensor.getType().toString(),
                            Collectors.counting()
                    ));
            
            logger.debug("Calculated type statistics: {}", typeStatistics);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonTypeStatistics = objectMapper.writeValueAsString(typeStatistics);

            String insertQuery = "INSERT INTO sensor_statistics (date, total_count, type_statistics) VALUES (?, ?, ?::jsonb)";
            jdbcTemplate.update(insertQuery, LocalDate.now(), totalCount, jsonTypeStatistics);
            logger.info("Successfully saved sensor statistics for date: {}", LocalDate.now());
        } catch (JsonProcessingException e) {
            logger.error("Error while processing JSON statistics", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Unexpected error while collecting statistics", e);
            throw e;
        }
    }
}
