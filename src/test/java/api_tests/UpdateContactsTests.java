package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import interfaces.BaseApi;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static helper.RandomUtils.*;


public class UpdateContactsTests extends ContactController {

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
    public void updateContactPositiveTest() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id -->" + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("New name "+generateString(5))
                .lastName("New last name " +generateString(5))
                .email(generateEmail(12))
                .phone("101234567890")
                .address("New address " + generateString(5))
                .description("description")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(),200);
        softAssert.assertTrue(getUserContactsResponse(token.getToken()).as(ContactsDto.class)
                        .getContacts()[0].equals(contact));
        softAssert.assertAll();
    }

    @Test
    public void updateContactPositiveTest_requiredFields() {
        int number = new Random().nextInt(contacts.getContacts().length);
        String idUpdateContact = contacts.getContacts()[number].getId();
        System.out.println("id -->" + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("New name "+generateString(5))
                .lastName("New last name " +generateString(5))
                .address("New address " + generateString(5))
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(),200);
        softAssert.assertTrue(getUserContactsResponse(token.getToken()).as(ContactsDto.class)
                .getContacts()[number].equals(contact));
        softAssert.assertAll();
    }


    @Test
    public void updateContactNegativeTest_wrongId() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id -->" + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact+"12345")
                .name("New name "+generateString(5))
                .lastName("New last name " +generateString(5))
                .email(generateEmail(12))
                .phone("101234567890")
                .address("New address " + generateString(5))
                .description("description")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(),400);
        if(response.getStatusCode() == 400) {
           errorMessageDto = response.as(ErrorMessageDto.class);
        }
        softAssert.assertAll();
    }

    @Test
    public void updateContactNegativeTest_wrongToken401() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id -->" + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("New name "+generateString(5))
                .lastName("New last name " +generateString(5))
                .email(generateEmail(12))
                .phone("101234567890")
                .address("New address " + generateString(5))
                .description("description")
                .build();
        Response response = updateContactResponse(contact, token.getToken()+"12345");
        softAssert.assertEquals(response.getStatusCode(),401);
        if(response.getStatusCode() == 400) {
            errorMessageDto = response.as(ErrorMessageDto.class);
        }
        softAssert.assertAll();
    }

    @Test
    public void updateContactNegativeTest_nameIsNull400() {
        int number = new Random().nextInt(contacts.getContacts().length);
        String idUpdateContact = contacts.getContacts()[number].getId();
        System.out.println("id -->" + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .lastName("New last name " +generateString(5))
                .email(generateEmail(12))
                .phone("101234567890")
                .address("New address " + generateString(5))
                .description("description")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(),400);
        if(response.getStatusCode() == 400) {
            errorMessageDto = response.as(ErrorMessageDto.class);
        }
        softAssert.assertAll();
    }
}
