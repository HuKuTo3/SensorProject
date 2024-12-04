package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.Sensor;
import com.nikita.varlakov.crudproject.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<Sensor> searchSensors(String query) {
        return sensorRepository.findByNameContainingIgnoreCaseOrModelContainingIgnoreCase(query, query);
    }

    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
    }

    public Sensor updateSensor(Long id, Sensor updatedSensor) {
        Sensor sensor = getSensorById(id);
        sensor.setName(updatedSensor.getName());
        sensor.setModel(updatedSensor.getModel());
        sensor.setType(updatedSensor.getType());
        return sensorRepository.save(sensor);
    }

    public void deleteSensor(Long id) {
        sensorRepository.deleteById(id);
    }
}