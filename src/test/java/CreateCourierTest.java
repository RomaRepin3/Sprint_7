import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private final String COURIER_PATH = "/api/v1/courier";
    private final String CONTENT_TYPE_STRING = "Content-type";
    private final String CONTENT_TYPE_OBJECT = "application/json";
    private final int CREATED_CODE = 201;
    private final int BAD_CODE = 400;
    private final String NOT_ENOUGH_DATA_MESSAGE = "Недостаточно данных для создания учетной записи";
    List<CreateCourierDto> courierList = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @After
    public void tearDown() {
        for (CreateCourierDto createCourierDto : courierList) {
            LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword());
            IdDto idDto = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginCourierDto)
                    .post("/api/v1/courier/login").as(IdDto.class);
            given().pathParam("id", idDto.getId()).delete("/api/v1/courier/{id}");
        }
    }

    @Test
    public void createCourier() {
        CreateCourierDto createCourierDto = new CreateCourierDto(Utils.getRandomString(), Utils.getRandomString());
        courierList.add(createCourierDto);

        Response response = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(createCourierDto)
                .post(COURIER_PATH);
        response.then().statusCode(CREATED_CODE);
        response.then().assertThat().body("ok",equalTo(true));
    }

    @Test
    public void tryCreateTwoIdenticalCouriers() {
        String identicalLogin = Utils.getRandomString();
        CreateCourierDto firstCreateCourierDto = new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString());
        CreateCourierDto secondCreateCourierDto = new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString());
        courierList.add(firstCreateCourierDto);
        courierList.add(secondCreateCourierDto);

        Response firstResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(firstCreateCourierDto)
                .post(COURIER_PATH);
        firstResponse.then().statusCode(CREATED_CODE);
        firstResponse.then().assertThat().body("ok",equalTo(true));

        Response secondResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(secondCreateCourierDto)
                .post(COURIER_PATH);
        secondResponse.then().statusCode(409);
        secondResponse.then().assertThat().body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void tryCreateCourierWithoutLoginField() {
        Response secondResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new CreateCourierWithoutLoginDto(Utils.getRandomString(), Utils.getRandomString()))
                .post(COURIER_PATH);
        secondResponse.then().statusCode(BAD_CODE);
        secondResponse.then().assertThat().body("message",equalTo(NOT_ENOUGH_DATA_MESSAGE));
    }

    @Test
    public void tryCreateCourierWithoutPasswordField() {
        Response secondResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new CreateCourierWithoutPasswordDto(Utils.getRandomString(), Utils.getRandomString()))
                .post(COURIER_PATH);
        secondResponse.then().statusCode(BAD_CODE);
        secondResponse.then().assertThat().body("message",equalTo(NOT_ENOUGH_DATA_MESSAGE));
    }
}
