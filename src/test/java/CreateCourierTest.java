import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private final String COURIER_PATH = "/api/v1/courier";
    private final String CONTENT_TYPE_STRING = "Content-type";
    private final String CONTENT_TYPE_OBJECT = "application/json";
    private final int CREATED_CODE = 201;
    private final int BAD_CODE = 400;
    private final String NOT_ENOUGH_DATA_MESSAGE = "Недостаточно данных для создания учетной записи";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourier() {
        Response response = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new CreateCourierDto(Utils.getRandomString(), Utils.getRandomString()))
                .post(COURIER_PATH);
        response.then().statusCode(CREATED_CODE);
        response.then().assertThat().body("ok",equalTo(true));
    }

    @Test
    public void tryCreateTwoIdenticalCouriers() {
        String identicalLogin = Utils.getRandomString();

        Response firstResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString()))
                .post(COURIER_PATH);
        firstResponse.then().statusCode(CREATED_CODE);
        firstResponse.then().assertThat().body("ok",equalTo(true));

        Response secondResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString()))
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
