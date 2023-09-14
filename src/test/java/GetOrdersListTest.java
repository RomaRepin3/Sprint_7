import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import requests.RequestsMethods;
import settings.MainSettings;

public class GetOrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrdersList() {
        Response response = RequestsMethods.sendByGetWithEmptyBody(MainSettings.ORDERS_URL);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.SUCCESS_STATUS_CODE);
        RequestsMethods.checkResponseBodyForOrderList(response);
    }
}
