package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.UserDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static helper.RandomUtils.*;
import static helper.PropertiesReader.*;

public class RegistrationTests extends AuthenticationController {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void registrationPositive() {
       // UserDto user = new UserDto(generateEmail(12),"Password123!");
        UserDto user = UserDto.builder()
                .username(generateEmail(12))
                .password("Password123!")
                .build();
        Assert.assertEquals(requestRegLogin(user, REGISTRATION_PATH).getStatusCode(), 200);
    }

    @Test
    public void registrationNegative_wrongEmail() {
        // UserDto user = new UserDto(generateEmail(12),"Password123!");
        UserDto user = UserDto.builder()
                .username(generateString(12))
                .password("Password123!")
                .build();
        Response response = requestRegLogin(user, REGISTRATION_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertEquals(errorMessageDto.getError(), "Bad Request");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("must be a well"));
        softAssert.assertEquals(errorMessageDto.getStatus(), 400);
        softAssert.assertAll();
    }

    @Test
    public void registrationNegative_duplicateUser409() {
        // UserDto user = new UserDto(generateEmail(12),"Password123!");
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password(getProperty("data.properties","password"))
                .build();
        Response response = requestRegLogin(user, REGISTRATION_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertEquals(errorMessageDto.getError(), "Conflict");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("already exists"));
        softAssert.assertEquals(errorMessageDto.getStatus(), 409);
        softAssert.assertAll();
    }

}
