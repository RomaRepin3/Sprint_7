import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class LoginCourierTest {
    private final String LOGIN_COURIER_PATH = "/api/v1/courier/login";

    private final String CONTENT_TYPE_STRING = "Content-type";
    private final String CONTENT_TYPE_OBJECT = "application/json";

    private final int NOT_FOUND_CODE = 404;
    private final int BAD_CODE = 400;

    private final String NOT_FOUND_MESSAGE = "Учетная запись не найдена";
    private final String NOT_ENOUGH_DATA = "Недостаточно данных для входа";

    private final String CODE_FIELD = "code";
    private final String MESSAGE_FIELD = "message";

    private CreateCourierDto createCourierDto;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createCourierDto = new CreateCourierDto(Utils.getRandomString(), Utils.getRandomString());

        given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(createCourierDto)
                .post("/api/v1/courier");
    }

    @After
    public void tearDown() {
        LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword());
        IdDto idDto = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierDto)
                .post("/api/v1/courier/login").as(IdDto.class);
        given().pathParam("id", idDto.getId()).delete("/api/v1/courier/{id}");
    }

    @Test
    public void loginCourier() {
        Response loginCourierResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword()))
                .post(LOGIN_COURIER_PATH);
        loginCourierResponse.then().statusCode(200);
        loginCourierResponse.then().assertThat().body("id", instanceOf(int.class));
    }

    @Test
    public void tryLoginCourierWithIncorrectLogin() {
        Response loginCourierResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new LoginCourierDto(Utils.getRandomString(), createCourierDto.getPassword()))
                .post(LOGIN_COURIER_PATH);
        loginCourierResponse.then().statusCode(NOT_FOUND_CODE);
        loginCourierResponse.then().assertThat().body(CODE_FIELD, equalTo(NOT_FOUND_CODE));
        loginCourierResponse.then().assertThat().body(MESSAGE_FIELD, equalTo(NOT_FOUND_MESSAGE));
    }

    @Test
    public void tryLoginCourierWithIncorrectPassword() {
        Response loginCourierResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new LoginCourierDto(createCourierDto.getLogin(), Utils.getRandomString()))
                .post(LOGIN_COURIER_PATH);
        loginCourierResponse.then().statusCode(NOT_FOUND_CODE);
        loginCourierResponse.then().assertThat().body(CODE_FIELD, equalTo(NOT_FOUND_CODE));
        loginCourierResponse.then().assertThat().body(MESSAGE_FIELD, equalTo(NOT_FOUND_MESSAGE));
    }

    @Test
    public void tryLoginCourierWithoutLogin() {
        Response loginCourierResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new LoginCourierWithoutLoginDto(createCourierDto.getPassword()))
                .post(LOGIN_COURIER_PATH);
        loginCourierResponse.then().statusCode(BAD_CODE);
        loginCourierResponse.then().assertThat().body(CODE_FIELD, equalTo(BAD_CODE));
        loginCourierResponse.then().assertThat().body(MESSAGE_FIELD, equalTo(NOT_ENOUGH_DATA));
    }

    @Test
    public void tryLoginCourierWithoutPassword() {
        Response loginCourierResponse = given()
                .header(CONTENT_TYPE_STRING, CONTENT_TYPE_OBJECT)
                .and()
                .body(new LoginCourierWithoutPasswordDto(createCourierDto.getLogin()))
                .post(LOGIN_COURIER_PATH);
        loginCourierResponse.then().statusCode(BAD_CODE);
        loginCourierResponse.then().assertThat().body(CODE_FIELD, equalTo(BAD_CODE));
        loginCourierResponse.then().assertThat().body(MESSAGE_FIELD, equalTo(NOT_ENOUGH_DATA));
    }
}
