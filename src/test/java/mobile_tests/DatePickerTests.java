package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;

import static helper.PropertiesReader.getProperty;

public class DatePickerTests extends AppiumConfig {

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
        contactsScreen = new ContactsScreen(driver);
        contactsScreen.goToDatePicker();
    }

    @Test(groups="smoke")
    // This test will fail because of the application bug
    // (Wrong date is displayed after setting -- month is one less that should be,
    // so if month is January or date like 31/10, the application crushes)
    public void datePickerPositiveTest() {
        String date = "02 August 2026";
        DatePickerScreen datePickerScreen = new DatePickerScreen(driver);
        datePickerScreen.typeDate(date);
        Assert.assertTrue(datePickerScreen.validateDateSetByDatePicker(date));
    }
}
