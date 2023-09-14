import dto.CreateOrderDto;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import requests.RequestsMethods;
import settings.MainSettings;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest  extends BaseTest{

    private final CreateOrderDto createOrderDto;

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
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.address().streetName(),
                faker.phoneNumber().phoneNumber(),
                1,
                "2020-06-06",
                faker.food().dish(),
                colours
        );
    }

    @Test
    public void createOrder() {
        Response response = RequestsMethods.sendPost(MainSettings.ORDERS_URL, createOrderDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.CREATED_STATUS_CODE);
        RequestsMethods.checkResponseBodyForOrderCreatePositive(response);
    }
}
