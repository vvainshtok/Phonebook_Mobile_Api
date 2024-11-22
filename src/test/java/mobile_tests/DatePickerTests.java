package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;

import java.util.Date;

import static helper.PropertiesReader.getProperty;

public class DatePickerTests extends AppiumConfig {


    UserDto user = UserDto.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();
   ContactsScreen contactsScreen;


    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactsScreen = new ContactsScreen(driver);
        contactsScreen.goToDatePicker();

    }

    @Test
    public void datePickerTest() {
        DatePickerScreen datePickerScreen = new DatePickerScreen(driver);
        datePickerScreen.typeDate("01 November 2023");

    }
}
