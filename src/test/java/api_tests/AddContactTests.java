package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static helper.RandomUtils.*;


public class AddContactTests extends ContactController {

    SoftAssert softAssert = new SoftAssert();
    ErrorMessageDto errorMessageDto;

    @Test
    public void addContactPositiveTest() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(10))
                .build();
        Response response = addContactResponse(contact, token.getToken());
        softAssert.assertEquals(response.getStatusCode(),200);
        ContactsDto contacts = getUserContactsResponse(token.getToken()).as(ContactsDto.class);
        softAssert.assertTrue(contacts.getContacts()[contacts.getContacts().length-1].equals(contact));
        softAssert.assertAll();
    }

    @Test
    public void addContactPositiveTest_requiredFieldsOnly() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .address(generateString(5))
                .build();
        Response response = addContactResponse(contact, token.getToken());
        softAssert.assertEquals(response.getStatusCode(),200);
        ContactsDto contacts = getUserContactsResponse(token.getToken()).as(ContactsDto.class);
        softAssert.assertTrue(contacts.getContacts()[contacts.getContacts().length-1].equals(contact));
        softAssert.assertAll();
    }

    @Test
    public void addContactNegativeTest_emptyName400() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("")
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(10))
                .build();
        Response response = addContactResponse(contact, token.getToken());
        softAssert.assertEquals(response.getStatusCode(),400);
        errorMessageDto = response.as(ErrorMessageDto.class);
        softAssert.assertEquals(errorMessageDto.getError(), "Bad Request");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("must not be blank"));
        softAssert.assertAll();
    }

    @Test
    public void addContactNegativeTest_wrongToken401() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(12))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(10))
                .build();
        Response response = addContactResponse(contact, token.getToken()+"123");
        softAssert.assertEquals(response.getStatusCode(),401);
        errorMessageDto = response.as(ErrorMessageDto.class);
        System.out.println("errorMessageDto --> " + errorMessageDto.toString());
        softAssert.assertEquals(errorMessageDto.getError(), "Unauthorized");
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("JWT signature does not match"));
        softAssert.assertAll();
    }
}
