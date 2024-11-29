package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;

public class LogoutTests extends AppiumConfig {

    UserDto user = UserDto.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();
    ContactsScreen contactsScreen;

    @BeforeMethod(alwaysRun = true)
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
    }

    @Test(groups="smoke")
    public void logout() {
        contactsScreen = new ContactsScreen(driver);
        contactsScreen.logout();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        Assert.assertTrue(authenticationScreen.isAuthScreenOpen());

    }
}
