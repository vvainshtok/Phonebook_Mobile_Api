package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.ErrorScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.*;
import static helper.RandomUtils.*;



public class LoginTests extends AppiumConfig {

    AuthenticationScreen authenticationScreen;

    @BeforeMethod
    public void openLoginForm() {
        new SplashScreen(driver).goToAuthScreen(5);
        authenticationScreen = new AuthenticationScreen(driver);
    }

    @Test
    public void loginPositiveTest() {
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password(getProperty("data.properties","password"))
                .build();
    authenticationScreen.typeAuthenticationForm(user);
    authenticationScreen.clickBtnLogin();
    Assert.assertTrue(new ContactsScreen(driver).validateHeader());
    }

    @Test
    public void loginNegativeTest_unregEmail() {
        UserDto user = UserDto.builder()
                .username(generateEmail(12))
                .password("QWErty567!")
                .build();
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("Login or Password incorrect", 5));
    }

    @Test
    public void loginNegativeTest_wrongPassword() {
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password("QWErty567!")
                .build();
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("Login or Password incorrect", 5));
    }


}
