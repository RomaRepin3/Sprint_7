import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrdersList() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().statusCode(200);
        response.then().assertThat().body("orders", notNullValue());
    }
}
