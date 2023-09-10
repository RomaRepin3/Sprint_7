import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final CreateOrderDto createOrderDto;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    public CreateOrderTest(List<String> colours) {
        createOrderDto = new CreateOrderDto(
                Utils.getRandomString(),
                Utils.getRandomString(),
                Utils.getRandomString(),
                Utils.getRandomString(),
                Utils.getRandomString(),
                1,
                "2020-06-06",
                Utils.getRandomString(),
                colours
        );
    }

    @Test
    public void createOrder() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderDto)
                .post("/api/v1/orders");
        response.then().statusCode(201);
        response.then().assertThat().body("track", instanceOf(int.class));

    }
}
