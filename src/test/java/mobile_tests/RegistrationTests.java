package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.ErrorScreen;
import screens.SplashScreen;

import static helper.RandomUtils.generateEmail;
import static helper.RandomUtils.generateString;

public class RegistrationTests extends AppiumConfig {

    @Test
    public void registrationPositiveTest() {
        UserDto user = UserDto.builder()
                .username(generateEmail(10))
                .password("Qwerty123!")
                .build();
        new SplashScreen(driver).goToAuthScreen();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        Assert.assertTrue(new ContactsScreen(driver).validateHeader());
    }

    @Test
    public void registrationNegativeTest_wrongEmail() {
        UserDto user = UserDto.builder()
                .username(generateString(10))
                .password("Qwerty123!")
                .build();
        new SplashScreen(driver).goToAuthScreen();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("must be a well-formed email address", 5));
    }

    @Test
    public void registrationNegativeTest_wrongPassword() {
        UserDto user = UserDto.builder()
                .username(generateEmail(10))
                .password("wrong_password")
                .build();
        new SplashScreen(driver).goToAuthScreen();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("Must contain at least", 5));
    }

}
