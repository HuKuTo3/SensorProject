package com.nikita.varlakov.crudproject.scheduler;

import com.nikita.varlakov.crudproject.service.StatisticService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticScheduler {

    private final StatisticService statisticService;

    public StatisticScheduler(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void collectStatistics() {
        statisticService.collectAndSaveStatistics();
    }
}