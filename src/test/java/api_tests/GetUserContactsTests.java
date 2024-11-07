package api_tests;

import config.ContactController;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetUserContactsTests extends ContactController {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void getUserContactsPositiveTest() {
        Response response = getUserContactsResponse(token.getToken());
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertTrue(response.getBody().print().contains("contacts"));
        softAssert.assertAll();
    }

    @Test
    public void getUserContactsNegativeTest_wrongToken() {
        Response response = getUserContactsResponse(token.getToken()+"12345");
        softAssert.assertEquals(response.getStatusCode(), 401);
        ErrorMessageDto errorMessage = ErrorMessageDto.builder().build();
        if(response.getStatusCode()==401)
            errorMessage = response.as(ErrorMessageDto.class);
        System.out.println(response);
        softAssert.assertEquals(errorMessage.getError(), "Unauthorized");
        softAssert.assertAll();
    }
}
