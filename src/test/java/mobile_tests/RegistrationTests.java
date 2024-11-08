package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.SplashScreen;

import static helper.RandomUtils.generateEmail;

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
    }

}
