package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static helper.RandomUtils.generateEmail;
import static helper.RandomUtils.generateString;

public class DeleteContactTests extends ContactController {

    ContactsDto contacts;
    SoftAssert softAssert = new SoftAssert();
    ErrorMessageDto errorMessageDto;

    @BeforeMethod
    public void getContact() {
        Response response = getUserContactsResponse(token.getToken());
        if(response.getStatusCode()==200) {
            contacts = response.as(ContactsDto.class);
        }
        else
            System.out.println("Something went wrong, code --> " + response.getStatusCode());
    }

    @Test
    public void deleteContactPositiveTest() {
        int number = new Random().nextInt(contacts.getContacts().length);
        ContactDtoLombok contact = contacts.getContacts()[number];
        Response response = deleteContactResponse(contact, token.getToken());
        softAssert.assertEquals(response.getStatusCode(),200);
        softAssert.assertFalse(getUserContactsResponse(token.getToken()).as(ContactsDto.class)
                .getContacts()[number].equals(contact));
        softAssert.assertAll();
    }

    @Test
    public void deleteContactNegativeTest_wrongId400() {
        int number = new Random().nextInt(contacts.getContacts().length);
        ContactDtoLombok contact = contacts.getContacts()[number];
        contact.setId("wrong_id");
        Response response = deleteContactResponse(contact, token.getToken());
        softAssert.assertEquals(response.getStatusCode(),400);
        softAssert.assertTrue(getUserContactsResponse(token.getToken()).as(ContactsDto.class)
                .getContacts()[number].equals(contact));
        softAssert.assertAll();
    }

    @Test
    public void deleteContactNegativeTest_wrongToken401() {
        int number = new Random().nextInt(contacts.getContacts().length);
        ContactDtoLombok contact = contacts.getContacts()[number];
        contact.setId("wrong_id");
        Response response = deleteContactResponse(contact, token.getToken()+"123");
        softAssert.assertEquals(response.getStatusCode(),401);
        errorMessageDto = response.as(ErrorMessageDto.class);
        System.out.println("errorMessageDto --> " + errorMessageDto.toString());
        softAssert.assertEquals(errorMessageDto.getError(), "Unauthorized");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("JWT signature does not match"));
        softAssert.assertTrue(getUserContactsResponse(token.getToken()).as(ContactsDto.class)
                .getContacts()[number].equals(contact));
        softAssert.assertAll();
    }


}
