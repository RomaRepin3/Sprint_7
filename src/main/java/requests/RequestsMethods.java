package requests;

import dto.IdDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static settings.MainSettings.*;

public class RequestsMethods {

    @Step("Отправка запроса на API методом POST")
    public static Response sendPost(String url, Object body){
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .and()
                .body(body)
                .when()
                .post(url);
    }

    @Step("Отправка запроса на API методом GET (без тела запроса)")
    public static Response sendByGetWithEmptyBody(String url){
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .when()
                .get(url);
    }

    @Step("Login. Десериализация ответа")
    public static int loginResponseDeserialization(String url, Object body){
        return sendPost(url, body).as(IdDto.class).getId();
    }

    @Step("Удаление Курьера по ID")
    public static void sendDeleteWithParamId(int id){
        given().pathParam("id", id).delete("/api/v1/courier/{id}");
//        given()
//                .header(CONTENT_TYPE, APPLICATION_JSON)
//                .when()
//                .delete(url);
    }

    @Step("Проверка статус кода с ожидаемым")
    public static void checkResponseStatusCode(Response response, int statusCode){
        response.then().statusCode(statusCode);
    }

    @Step("Проверка ответа, что успешно создан Курьер")
    public static void checkResponseBodyForCourierCreatePositive(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Проверка ответа, что недостаточно данных для создания учетной записи")
    public static void checkResponseBodyForCourierCreateNegative(Response response){
        response.then().assertThat()
                .body("code", equalTo(BAD_REQUEST_STATUS_CODE))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Проверка ответа, что этот логин уже используется")
    public static void checkResponseBodyForCourierCreateNegativeAlreadyExists(Response response){
        response.then().assertThat()
                .body("code", equalTo(CONFLICT_STATUS_CODE))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Проверка, что произошла успешная аутентификация курьера")
    public static void checkResponseBodyForCourierLoginPositive(Response response){
        response.then().assertThat()
                .body("id", notNullValue())
                .body("id", is(instanceOf(Number.class)));
    }

    @Step("Проверка ответа, что учетная запись не найдена")
    public static void checkResponseBodyForCourierLoginNegativeWrongPasswordAndLogin(Response response){
        response.then().assertThat()
                .body("code", equalTo(NOT_FOUND_STATUS_CODE))
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверка ответа, что недостаточно данных для входа")
    public static void checkResponseBodyForCourierLoginNegativeWithoutPasswordAndLogin(Response response){
        response.then().assertThat()
                .body("code", equalTo(BAD_REQUEST_STATUS_CODE))
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Проверка ответа, что успешно создан заказ с заданым цветом")
    public static void checkResponseBodyForOrderCreatePositive(Response response){
        response.then().assertThat()
                .body("track", notNullValue())
                .body("track", is(instanceOf(Number.class)));
    }

    @Step("Проверка ответа, что успешно получен список заказов (без тела в запросе)")
    public static void checkResponseBodyForOrderList(Response response){
        response.then().assertThat()
                .body("orders", notNullValue())
                .body("orders", is(instanceOf(List.class)));
    }
}
