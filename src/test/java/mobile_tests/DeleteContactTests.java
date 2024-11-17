package mobile_tests;

import config.AppiumConfig;
import dto.ContactsDto;
import dto.UserDto;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import java.util.Arrays;

import static helper.PropertiesReader.getProperty;

public class DeleteContactTests extends AppiumConfig {

    UserDto user = UserDto.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();

    ContactsScreen contactScreen;

    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactScreen = new ContactsScreen(driver);
    }

    @Test
    public void deleteContactPositiveTest() {
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);

        int quantityContactsBeforeDelete = contactsDto.getContacts().length;

        contactScreen.deleteContact();
        contactScreen.clickBtnYes();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Response responseGetAfter = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDtoAfter = responseGetAfter.as(ContactsDto.class);
        int quantityContactsAfterDelete = contactsDtoAfter.getContacts().length;
        System.out.println(" before -> " + quantityContactsBeforeDelete + " after -> " + quantityContactsAfterDelete);
        Assert.assertEquals(quantityContactsBeforeDelete - quantityContactsAfterDelete, 1);
    }

    @Test
    public void deleteContactPositiveTest_validateWithoutApi() {
        String[] namesBefore, namesAfter;
        namesBefore = contactScreen.get5ElementsNames();
        contactScreen.deleteContact();
        contactScreen.clickBtnYes();
        namesAfter = contactScreen.get5ElementsNames();
        Assert.assertFalse(Arrays.equals(namesBefore, namesAfter));
    }
}