package Steps;
import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.testng.Assert;

public class T07_SignedUrlsCreationRestAssured {
    private Response response;
    public static String SealedImgsURL;

    @Given("a valid entity key and a valid cookie")
    public void a_valid_entity_key_and_a_valid_cookie() {
        System.out.println(Steps.T05_ExtractEntityKeyRestAssured.entityKey);
        Assert.assertNotNull(Steps.T05_ExtractEntityKeyRestAssured.entityKey, "Entity key should not be null");
        Assert.assertNotNull(ApiCookieManager.COOKIE_VALUE, "Cookie value should not be null");
    }

    @When("I send a request to create a signed URL for {string}")
    public void i_send_a_request_to_create_a_signed_url_for(String imageName) {
        String cookieName = "Cookie";
        String payload = "{ \"sign\": { \"" + Steps.T05_ExtractEntityKeyRestAssured.entityKey + "\": { \"" + imageName + "\": 1 } } }";

        response = given()
                .header("accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(payload)
                .when()
                .post("https://express-api.noonstg.team/hub/signed-urls");
    }
    @Then("I should receive a valid signed URL in the response")
    public void i_should_receive_a_valid_signed_url_in_the_response() {
        String jsonResponse = response.getBody().asString();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        System.out.println("Response: " + jsonResponse);
        SealedImgsURL = JsonPath.read(jsonResponse, "$.signed['" + T05_ExtractEntityKeyRestAssured.entityKey + "']['trip-sealed.jpg'][0]");
        System.out.println("Signed URL: " + SealedImgsURL);
        Assert.assertNotNull(SealedImgsURL, "The signed URL should not be null.");
    }
}
