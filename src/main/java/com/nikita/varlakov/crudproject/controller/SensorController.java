package com.nikita.varlakov.crudproject.controller;

import com.nikita.varlakov.crudproject.model.Sensor;
import com.nikita.varlakov.crudproject.service.SensorService;
import com.nikita.varlakov.crudproject.constants.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(AppConstants.SENSORS_BASE_PATH)
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
            @RequestParam(value = AppConstants.QUERY_PARAM, required = false) String query,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.SORT_PARAM) String sort,
            Model model,
            Authentication authentication) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Sensor> sensorPage;

        if (query != null && !query.isEmpty()) {
            sensorPage = sensorService.searchSensors(query, pageable);
        } else {
            sensorPage = sensorService.getAllSensors(pageable);
        }

        model.addAttribute(AppConstants.SENSORS_ATTRIBUTE, sensorPage);
        model.addAttribute(AppConstants.QUERY_ATTRIBUTE, query);

        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(AppConstants.ROLE_ADMINISTRATOR))) {
            return AppConstants.SENSOR_LIST_ADMIN_VIEW;
        }

        return AppConstants.SENSOR_LIST_VIEWER_VIEW;
    }

    @Operation(summary = "Показать форму для создания датчика", description = "Показывает форму для добавления нового датчика")
    @ApiResponse(responseCode = "200", description = "Форма для создания датчика отображена")
    @GetMapping(AppConstants.SENSORS_CREATE_PATH)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String showCreateForm(Model model) {
        model.addAttribute(AppConstants.SENSOR_ATTRIBUTE, new Sensor());
        model.addAttribute(AppConstants.TYPES_ATTRIBUTE, Sensor.SensorType.values());
        model.addAttribute(AppConstants.UNITS_ATTRIBUTE, Sensor.Unit.values());
        return AppConstants.SENSOR_CREATE_VIEW;
    }

    @Operation(summary = "Создать новый датчик", description = "Сохраняет новый датчик в базу данных после валидации данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Датчик успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка при валидации данных датчика")
    })
    @PostMapping(AppConstants.SENSORS_CREATE_PATH)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String createSensor(@ModelAttribute(AppConstants.SENSOR_ATTRIBUTE) Sensor sensor, Model model) {
        if (sensor.getRangeFrom() != null && sensor.getRangeTo() != null &&
                sensor.getRangeFrom() >= sensor.getRangeTo()) {
            model.addAttribute(AppConstants.ERROR_ATTRIBUTE, AppConstants.RANGE_ERROR_MESSAGE);
            return AppConstants.SENSOR_CREATE_VIEW;
        }

        sensorService.createSensor(sensor);
        return "redirect:" + AppConstants.SENSORS_BASE_PATH;
    }

    @Operation(summary = "Показать форму для редактирования датчика", description = "Отображает форму для редактирования датчика с указанным ID")
    @ApiResponse(responseCode = "200", description = "Форма для редактирования датчика отображена")
    @GetMapping(AppConstants.SENSORS_EDIT_PATH)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Sensor sensor = sensorService.getSensorById(id);
        model.addAttribute(AppConstants.SENSOR_ATTRIBUTE, sensor);
        model.addAttribute(AppConstants.TYPES_ATTRIBUTE, Sensor.SensorType.values());
        model.addAttribute(AppConstants.UNITS_ATTRIBUTE, Sensor.Unit.values());
        return AppConstants.SENSOR_EDIT_VIEW;
    }

    @Operation(summary = "Обновить информацию о датчике", description = "Обновляет данные существующего датчика в базе данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Датчик успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка при валидации данных датчика")
    })
    @PostMapping(AppConstants.SENSORS_EDIT_PATH)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String updateSensor(@PathVariable Long id, @ModelAttribute(AppConstants.SENSOR_ATTRIBUTE) Sensor sensor, Model model) {
        if (sensor.getRangeFrom() != null && sensor.getRangeTo() != null &&
                sensor.getRangeFrom() >= sensor.getRangeTo()) {
            model.addAttribute(AppConstants.ERROR_ATTRIBUTE, AppConstants.RANGE_ERROR_MESSAGE);
            return AppConstants.SENSOR_EDIT_VIEW;
        }

        sensorService.updateSensor(id, sensor);
        return "redirect:" + AppConstants.SENSORS_BASE_PATH;
    }

    @Operation(summary = "Удалить датчик", description = "Удаляет датчик с указанным ID из базы данных")
    @ApiResponse(responseCode = "200", description = "Датчик успешно удалён")
    @GetMapping(AppConstants.SENSORS_DELETE_PATH)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String deleteSensor(@PathVariable Long id) {
        sensorService.deleteSensor(id);
        return "redirect:" + AppConstants.SENSORS_BASE_PATH;
    }
}
