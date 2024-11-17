package mobile_tests;

import config.AppiumConfig;
import dto.ContactDtoLombok;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.EditContactScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;
import static helper.RandomUtils.generateString;

public class EditContactTests extends AppiumConfig {

    UserDto user = UserDto.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();

    ContactsScreen contactScreen;
    EditContactScreen editContactScreen;

    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactScreen = new ContactsScreen(driver);
    }

    @Test
    public void editContactPositiveTest() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactScreen.selectContactToEdit();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage("Contact was updated"));
    }
}
