import dto.*;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import requests.RequestsMethods;
import settings.MainSettings;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CreateCourierTest extends BaseTest {
    List<CreateCourierDto> courierList = new ArrayList<>();

    @After
    public void tearDown() {
        for (CreateCourierDto createCourierDto : courierList) {
            LoginCourierDto loginCourierDto = new LoginCourierDto(createCourierDto.getLogin(), createCourierDto.getPassword());
            int id = RequestsMethods.loginResponseDeserialization(MainSettings.LOG_IN_BY_COURIER_URL, loginCourierDto);
            RequestsMethods.sendDeleteWithParamId(id);
        }
    }

    @Test
    public void createCourier() {
        CreateCourierDto createCourierDto = new CreateCourierDto(Utils.getRandomString(), Utils.getRandomString());
        courierList.add(createCourierDto);

        Response response = RequestsMethods.sendPost(MainSettings.COURIER_URL, createCourierDto);
        RequestsMethods.checkResponseStatusCode(response, MainSettings.CREATED_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierCreatePositive(response);
    }

    @Test
    public void tryCreateTwoIdenticalCouriers() {
        String identicalLogin = Utils.getRandomString();
        CreateCourierDto firstCreateCourierDto = new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString());
        CreateCourierDto secondCreateCourierDto = new CreateCourierDto(identicalLogin, Utils.getRandomString(), Utils.getRandomString());
        courierList.add(firstCreateCourierDto);
        courierList.add(secondCreateCourierDto);

        Response firstResponse = RequestsMethods.sendPost(MainSettings.COURIER_URL, firstCreateCourierDto);
        RequestsMethods.checkResponseStatusCode(firstResponse, MainSettings.CREATED_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierCreatePositive(firstResponse);

        Response secondResponse = RequestsMethods.sendPost(MainSettings.COURIER_URL, secondCreateCourierDto);
        RequestsMethods.checkResponseStatusCode(secondResponse, MainSettings.CONFLICT_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierCreateNegativeAlreadyExists(secondResponse);
    }

    @Test
    public void tryCreateCourierWithoutLoginField() {
        Response response = RequestsMethods.sendPost(
                MainSettings.COURIER_URL,
                new CreateCourierWithoutLoginDto(Utils.getRandomString(),Utils.getRandomString())
        );
        RequestsMethods.checkResponseStatusCode(response, MainSettings.BAD_REQUEST_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierCreateNegative(response);
    }

    @Test
    public void tryCreateCourierWithoutPasswordField() {
        Response response = RequestsMethods.sendPost(
                MainSettings.COURIER_URL,
                new CreateCourierWithoutPasswordDto(Utils.getRandomString(),Utils.getRandomString())
        );
        RequestsMethods.checkResponseStatusCode(response, MainSettings.BAD_REQUEST_STATUS_CODE);
        RequestsMethods.checkResponseBodyForCourierCreateNegative(response);
    }
}
