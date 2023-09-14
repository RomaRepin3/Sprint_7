package settings;

public class MainSettings {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
    public static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
    public static final String COURIER_URL = "/api/v1/courier";
    public static final String ORDERS_URL = "/api/v1/orders";
    public static final String LOG_IN_BY_COURIER_URL = "api/v1/courier/login";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final int CREATED_STATUS_CODE = 201;
    public static final int BAD_REQUEST_STATUS_CODE = 400;
    public static final int NOT_FOUND_STATUS_CODE = 404;
    public static final int CONFLICT_STATUS_CODE = 409;
}
