package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISensorService {
    Page<Sensor> getAllSensors(Pageable pageable);
    
    Sensor getSensorById(Long id);
    
    Page<Sensor> searchSensors(String query, Pageable pageable);
    
    Sensor createSensor(Sensor sensor);
    
    Sensor updateSensor(Long id, Sensor updatedSensor);
    
    void deleteSensor(Long id);
}
