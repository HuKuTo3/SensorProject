package com.nikita.varlakov.crudproject.constants;

public final class AppConstants {
    // Request parameters
    public static final String QUERY_PARAM = "query";
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final String SORT_PARAM = "id";
    
    // Default values
    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "6";
    
    // View paths
    public static final String SENSOR_LIST_ADMIN_VIEW = "sensor/list-admin";
    public static final String SENSOR_LIST_VIEWER_VIEW = "sensor/list-viewer";
    public static final String SENSOR_CREATE_VIEW = "sensor/create";
    public static final String SENSOR_EDIT_VIEW = "sensor/edit";
    public static final String USER_LOGIN_VIEW = "user/login";
    public static final String USER_REGISTER_VIEW = "user/register";
    
    // Model attributes
    public static final String SENSORS_ATTRIBUTE = "sensors";
    public static final String QUERY_ATTRIBUTE = "query";
    public static final String SENSOR_ATTRIBUTE = "sensor";
    public static final String USER_ATTRIBUTE = "user";
    public static final String TYPES_ATTRIBUTE = "types";
    public static final String UNITS_ATTRIBUTE = "units";
    public static final String ERROR_ATTRIBUTE = "error";
    
    // Roles
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_VIEWER = "ROLE_VIEWER";
    
    // Error messages
    public static final String RANGE_ERROR_MESSAGE = "The minimum range must be less than the maximum range.";
    public static final String USER_REGISTRATION_SUCCESS = "User successfully registered";
    public static final String USER_NOT_FOUND = "User not found: ";
    public static final String USER_EXISTS = "User with username %s already exists";
    
    // URL mappings
    public static final String SENSORS_BASE_PATH = "/sensors";
    public static final String SENSORS_CREATE_PATH = "/create";
    public static final String SENSORS_EDIT_PATH = "/edit/{id}";
    public static final String SENSORS_DELETE_PATH = "/delete/{id}";
    
    // Security paths
    public static final String USERS_BASE_PATH = "/users";
    public static final String USERS_REGISTER_PATH = "/register";
    public static final String USERS_LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";
    
    // Swagger paths
    public static final String[] PUBLIC_PATHS = {
        USERS_BASE_PATH + USERS_REGISTER_PATH,
        USERS_BASE_PATH + USERS_LOGIN_PATH,
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/swagger-resources/**"
    };
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}
