package mobile_tests;

import config.AppiumConfig;
import data_provider.ContactDP;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import dto.UserDto;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import screens.*;

import java.util.Arrays;
import java.util.List;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class AddNewContactTests extends AppiumConfig {

    SoftAssert softAssert = new SoftAssert();

    UserDto user = UserDto.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();
    AddNewContactScreen addNewContactScreen;
    ContactsScreen contactsScreen;


    @BeforeMethod(alwaysRun = true)
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactsScreen = new ContactsScreen(driver);
        contactsScreen.clickBtnAddNewContact();
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
        Assert.assertTrue(new ContactsScreen(driver).validatePopUpMessage("Contact was added"));
    }

    @Test(groups="smoke")
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

        List<ContactDtoLombok> contactList = Arrays.asList(contactsDto.getContacts());
        Assert.assertTrue(contactList.contains(contact));
    }



    @Test
    public void addNewContactNegative_emptyName() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("")
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("must not be blank", 3));
    }

    @Test
    public void addNewContactNegative_wrongEmail() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateString(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("must be a well-formed", 3));
    }

    @Test
    public void addNewContactNegative_wrongPhone() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(8))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        Assert.assertTrue(new ErrorScreen(driver)
                .validateErrorMessage("must contain", 3));
    }

    @Test
    public void addNewContactNegative_emptyLastName_validateContactApi() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName("")
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();

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
        // int numberContact = Arrays.asList(contactsDto.getContacts()).indexOf(contact);
        Assert.assertFalse(flag);
    }

    @Test(dataProvider = "addNewContactDPFile", dataProviderClass = ContactDP.class)
    public void addNewContactNegative_emptyField(ContactDtoLombok contact) {
        addNewContactScreen.typeContactForm(contact);
        addNewContactScreen.clickBtnCreateContact();
        Assert.assertTrue(
                new ErrorScreen(driver)
                        .validateErrorMessage("must not be blank", 3)
                        || new ErrorScreen(driver)
                        .validateErrorMessage("well-formed email address", 3)
                        || new ErrorScreen(driver)
                        .validateErrorMessage("phone number must contain", 3)
        );
    }

    @Test
    public void addNewContactNegative_duplicateContactA() {
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        if(responseGet.getStatusCode() == 200) {
            ContactsDto contactsDto = responseGet.as(ContactsDto.class);
            ContactDtoLombok contactApi = contactsDto.getContacts()[0];
            ContactDtoLombok contact = ContactDtoLombok.builder()
                    .name(contactApi.getName())
                    .lastName(contactApi.getLastName())
                    .email(contactApi.getEmail())
                    .phone(contactApi.getPhone())
                    .address(contactApi.getAddress())
                    .description(contactApi.getDescription())
                    .build();
            addNewContactScreen.typeContactForm(contact);
            addNewContactScreen.clickBtnCreateContact();
            //bug - будет false
            Assert.assertTrue(new ErrorScreen(driver)
                            .validateErrorMessage("duplicate contact", 3));

        }
    }

    @Test
    public void addNewContactPositive_validateUIListContact() {
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
        softAssert.assertTrue(new ContactsScreen(driver).validatePopUpMessage("Contact was added"));
        softAssert.assertTrue(contactsScreen.validateUIListContact(contact));
        softAssert.assertAll();
    }
}
