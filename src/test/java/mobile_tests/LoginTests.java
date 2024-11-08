package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import static helper.RandomUtils.generateEmail;

public class LoginTests extends AppiumConfig {

    @Test
    public void loginPositiveTest() {
        UserDto user = UserDto.builder()
                .username("vv17@gmail.com")
                .password("QWErty123!")
                .build();

        new SplashScreen(driver).goToAuthScreen();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        Assert.assertTrue(new ContactsScreen(driver).validatePage());
    }


}
