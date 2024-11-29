package config;

import dto.ContactDtoLombok;
import dto.TokenDto;
import dto.UserDto;
import interfaces.BaseApi;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static helper.PropertiesReader.getProperty;
import static io.restassured.RestAssured.given;


public class ContactController implements BaseApi {

    protected TokenDto token;
    private RequestSpecification requestSpecWithToken;

    @BeforeSuite(alwaysRun = true)
    public void login() {
        UserDto user = UserDto.builder()
                .username(getProperty("data.properties","email"))
                .password(getProperty("data.properties","password"))
                .build();
        Response response = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + LOGIN_PATH)
                .thenReturn();
        if(response.getStatusCode()==200) {
            token = response.as(TokenDto.class);
            requestSpecWithToken = new RequestSpecBuilder()
                    .addParam("Authorization", token.getToken())
                    .setContentType(ContentType.JSON)
                    .build();
        }
        else
            System.out.println("Something went wrong, status code --> " + response.getStatusCode());
    }

    protected Response getUserContactsResponse(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response updateContactResponseWithToken(ContactDtoLombok contact) {
        return given()
                .body(contact)
                .header("Authorization", token.getToken())
                .contentType(ContentType.JSON)
                //.spec(requestSpecWithToken)
                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response updateContactResponse(ContactDtoLombok contact, String token) {
        return given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                //.spec(requestSpecWithToken)
                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response addContactResponse(ContactDtoLombok contact, String token) {
        return given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .post(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response deleteContactResponse(ContactDtoLombok contact, String token) {
        return given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .delete(BASE_URL + GET_ALL_CONTACTS_PATH + "/" + contact.getId())
                .thenReturn();
    }

}
