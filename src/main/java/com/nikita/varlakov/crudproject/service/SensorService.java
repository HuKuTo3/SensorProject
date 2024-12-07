package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.Sensor;
import com.nikita.varlakov.crudproject.repository.SensorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SensorService implements ISensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Page<Sensor> getAllSensors(Pageable pageable) {
        return sensorRepository.findAll(pageable);
    }

    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
    }

    public Page<Sensor> searchSensors(String query, Pageable pageable) {
        return sensorRepository.findByNameContainingIgnoreCaseOrModelContainingIgnoreCase(query, query, pageable);
    }

    @Transactional
    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    @Transactional
    public Sensor updateSensor(Long id, Sensor updatedSensor) {
        Sensor sensor = getSensorById(id);
        sensor.setName(updatedSensor.getName());
        sensor.setModel(updatedSensor.getModel());
        sensor.setType(updatedSensor.getType());
        return sensorRepository.save(sensor);
    }

    @Transactional
    public void deleteSensor(Long id) {
        sensorRepository.deleteById(id);
    }
}