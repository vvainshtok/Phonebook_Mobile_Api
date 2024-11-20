package mobile_tests;

import config.AppiumConfig;
import data_provider.ContactDP;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.UserDto;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;
import utils.RetryAnalyzer;

import java.util.Arrays;
import java.util.List;

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
                .name("edit-" + generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage("Contact was updated"));
    }


    @Test
    public void editContactNegativeTest_emptyName() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("")
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(new ErrorScreen(driver).validateErrorMessage("must not be blank", 3));
    }

    @Test
    public void editContactPositiveTest_validateApi() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("edit-" + generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeEditContactForm(contact);
        editContactScreen.clickBtnUpdate();

        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        List<ContactDtoLombok> contactList = Arrays.asList(contactsDto.getContacts());
        Assert.assertTrue(contactList.contains(contact));
    }

    // ===================================================================================
        @Test
        public void editContactNegativeTest_wrongEmail() {
            ContactDtoLombok contact = ContactDtoLombok.builder()
                    .name("edit-" + generateString(10))
                    .lastName(generateString(10))
                    .email(generateString(12))
                    .phone(generatePhone(10))
                    .address(generateString(5) + " app." + generatePhone(2))
                    .description(generateString(15))
                    .build();
            contactScreen.goToEditScreen();
            editContactScreen = new EditContactScreen(driver);
            editContactScreen.typeEditContactForm(contact);
            editContactScreen.clickBtnUpdate();
            Assert.assertTrue(new ErrorScreen(driver)
                    .validateErrorMessage("must be a well-formed", 3));
        }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void editContactNegativeTest_wrongPhone() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("edit-" + generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(8))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("must contain", 3));
    }

    @Test(dataProvider = "addNewContactDPFile", dataProviderClass = ContactDP.class)
    public void addNewContactNegative_emptyOrWrongField(ContactDtoLombok contact) {
        contactScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.typeEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(
                new ErrorScreen(driver)
                        .validateErrorMessage("must not be blank", 3)
                        || new ErrorScreen(driver)
                        .validateErrorMessage("well-formed email address", 3)
                        || new ErrorScreen(driver)
                        .validateErrorMessage("number must contain", 3));
    }

}
