package mobile_tests;

import config.AppiumConfig;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.UserDto;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AddNewContactScreen;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class AddNewContactTests extends AppiumConfig {

    UserDto user = UserDto.builder()
            .username(getProperty("data.properties","email"))
            .password(getProperty("data.properties","password"))
            .build();
    AddNewContactScreen addNewContactScreen;


    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        new ContactsScreen(driver).clickBtnAddNewContact();
        addNewContactScreen = new AddNewContactScreen(driver);
    }

    @Test
    public void addNewContactPositive() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage());
    }

    @Test
    public void addNewContactPositive_validateContactApi() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        // Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage());
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
                break;
            }
        }
        Assert.assertTrue(flag);
    }



}
