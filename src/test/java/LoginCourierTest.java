import dto.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import requests.RequestsMethods;
import settings.MainSettings;
import utils.Utils;

public class LoginCourierTest extends BaseTest{
    private CreateCourierDto createCourierDto;

    @Before
    public void setUp() {
        createCourierDto = new CreateCourierDto(Utils.getRandomString(), Utils.getRandomString());
        RequestsMethods.sendPost(MainSettings.COURIER_URL, createCourierDto);
    }

    @After
    public void tearDown() {
        LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword());
        int id = RequestsMethods.loginResponseDeserialization(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierDto);
        RequestsMethods.sendDeleteWithParamId(id);
    }

    @Test
    public void loginCourier() {
        LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword());
        Response response = RequestsMethods.sendPost(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.SUCCESS_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierLoginPositive(response);
    }

    @Test
    public void tryLoginCourierWithIncorrectLogin() {
        LoginCourierDto loginCourierDto = new LoginCourierDto(Utils.getRandomString(), createCourierDto.getPassword());
        Response response = RequestsMethods.sendPost(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.NOT_FOUND_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierLoginNegativeWrongPasswordAndLogin(response);
    }

    @Test
    public void tryLoginCourierWithIncorrectPassword() {
        LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), Utils.getRandomString());
        Response response = RequestsMethods.sendPost(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.NOT_FOUND_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierLoginNegativeWrongPasswordAndLogin(response);
    }

    @Test
    public void tryLoginCourierWithoutLogin() {
        LoginCourierWithoutLoginDto loginCourierWithoutLoginDto =  new LoginCourierWithoutLoginDto(createCourierDto.getPassword());
        Response response = RequestsMethods.sendPost(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierWithoutLoginDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.BAD_REQUEST_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierLoginNegativeWithoutPasswordAndLogin(response);
    }

    @Test
    public void tryLoginCourierWithoutPassword() {
        LoginCourierWithoutPasswordDto loginCourierWithoutPasswordDto = new LoginCourierWithoutPasswordDto(createCourierDto.getLogin());
        Response response = RequestsMethods.sendPost(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierWithoutPasswordDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.BAD_REQUEST_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierLoginNegativeWithoutPasswordAndLogin(response);
    }
}
