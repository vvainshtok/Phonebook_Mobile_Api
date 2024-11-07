package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.TokenDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static helper.PropertiesReader.*;

public class LoginTests extends AuthenticationController {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void loginPositive() {
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password(getProperty("data.properties","password"))
                .build();
        Assert.assertEquals(requestRegLogin(user, LOGIN_PATH).getStatusCode(), 200);
    }

    @Test
    public void loginNegative_wrongEmailOrPassword401() {
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password("wrong_password")
                .build();
        Response response = requestRegLogin(user, LOGIN_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertEquals(errorMessageDto.getError(), "Unauthorized");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("Login or Password incorrect"));
        softAssert.assertEquals(errorMessageDto.getStatus(), 401);
        softAssert.assertAll();
    }
}
