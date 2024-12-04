package com.nikita.varlakov.crudproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String model;

    @NotNull
    private Integer rangeFrom;

    @NotNull
    private Integer rangeTo;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(length = 20)
    private SensorType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Unit unit;

    @Size(max = 40)
    private String location;

    @Size(max = 200)
    private String description;

    public enum SensorType {
        PRESSURE,
        TEMPERATURE,
        HUMIDITY,
        VOLTAGE
    }

    public enum Unit {
        BAR,
        VOLTAGE,
        CELSIUS,
        PERCENT
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 3, max = 30) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(min = 3, max = 30) String name) {
        this.name = name;
    }

    public @NotBlank @Size(max = 15) String getModel() {
        return model;
    }

    public void setModel(@NotBlank @Size(max = 15) String model) {
        this.model = model;
    }

    public @NotNull Integer getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(@NotNull Integer rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public @NotNull Integer getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(@NotNull Integer rangeTo) {
        this.rangeTo = rangeTo;
    }

    public @Size(max = 40) String getLocation() {
        return location;
    }

    public void setLocation(@Size(max = 40) String location) {
        this.location = location;
    }

    public @Size(max = 200) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 200) String description) {
        this.description = description;
    }

    public @NotNull SensorType getType() {
        return type;
    }

    public void setType(@NotNull SensorType type) {
        this.type = type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}