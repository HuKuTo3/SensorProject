package com.nikita.varlakov.crudproject.controller;

import com.nikita.varlakov.crudproject.model.Sensor;
import com.nikita.varlakov.crudproject.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(summary = "Получить список датчиков", description = "Возвращает все датчики или результат поиска по имени/модели датчика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список датчиков успешно возвращён"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @GetMapping
    public String listSensors(
            @RequestParam(value = "query", required = false) String query, Model model,
            Authentication authentication) {
        List<Sensor> sensors;

        if (query != null && !query.isEmpty()) {
            sensors = sensorService.searchSensors(query);
        } else {
            sensors = sensorService.getAllSensors();
        }

        model.addAttribute("sensors", sensors);
        model.addAttribute("query", query);

        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
            return "sensor/list-admin";
        }

        return "sensor/list-viewer";
    }

    @Operation(summary = "Показать форму для создания датчика", description = "Показывает форму для добавления нового датчика")
    @ApiResponse(responseCode = "200", description = "Форма для создания датчика отображена")
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String showCreateForm(Model model) {
        model.addAttribute("sensor", new Sensor());
        model.addAttribute("types", Sensor.SensorType.values());
        model.addAttribute("units", Sensor.Unit.values());
        return "sensor/create";
    }

    @Operation(summary = "Создать новый датчик", description = "Сохраняет новый датчик в базу данных после валидации данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Датчик успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка при валидации данных датчика")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String createSensor(@ModelAttribute("sensor") Sensor sensor, Model model) {
        System.out.println("Win");
        if (sensor.getRangeFrom() != null && sensor.getRangeTo() != null &&
                sensor.getRangeFrom() >= sensor.getRangeTo()) {
            model.addAttribute("error", "The minimum range must be less than the maximum range.");
            return "sensor/create";
        }

        sensorService.createSensor(sensor);
        return "redirect:/sensors";
    }

    @Operation(summary = "Показать форму для редактирования датчика", description = "Отображает форму для редактирования датчика с указанным ID")
    @ApiResponse(responseCode = "200", description = "Форма для редактирования датчика отображена")
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Sensor sensor = sensorService.getSensorById(id);
        model.addAttribute("sensor", sensor);
        model.addAttribute("types", Sensor.SensorType.values());
        model.addAttribute("units", Sensor.Unit.values());
        return "sensor/edit";
    }

    @Operation(summary = "Обновить информацию о датчике", description = "Обновляет данные существующего датчика в базе данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Датчик успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка при валидации данных датчика")
    })
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String updateSensor(@PathVariable Long id, @ModelAttribute("sensor") Sensor sensor, Model model) {
        if (sensor.getRangeFrom() != null && sensor.getRangeTo() != null &&
                sensor.getRangeFrom() >= sensor.getRangeTo()) {
            model.addAttribute("error", "The minimum range must be less than the maximum range.");
            return "sensor/edit";
        }

        sensorService.updateSensor(id, sensor);
        return "redirect:/sensors";
    }

    @Operation(summary = "Удалить датчик", description = "Удаляет датчик с указанным ID из базы данных")
    @ApiResponse(responseCode = "200", description = "Датчик успешно удалён")
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String deleteSensor(@PathVariable Long id) {
        sensorService.deleteSensor(id);
        return "redirect:/sensors";
    }
}
