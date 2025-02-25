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

public class T08_SignedUrlsCreationRestAssured {
    private Response response;
    public static String SealedImgsURL;
    public static String UnSealedImgsURL;

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
    @Then("I should receive a valid signed URL for {string} in the response")
    public void i_should_receive_a_valid_signed_url_for_in_the_response(String imageName) {
        String jsonResponse = response.getBody().asString();
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + jsonResponse);

        // Check the image name and extract the corresponding signed URL
        if(imageName.equals("trip-sealed.jpg")) {
            SealedImgsURL = JsonPath.read(jsonResponse, "$.signed['" + Steps.T05_ExtractEntityKeyRestAssured.entityKey
                    + "']['" + imageName + "'][0]");
            System.out.println("Signed URL for sealed: " + SealedImgsURL);
            Assert.assertNotNull(SealedImgsURL, "The signed URL for sealed should not be null.");
        } else if(imageName.equals("trip-unsealed.jpg")) {
            UnSealedImgsURL = JsonPath.read(jsonResponse, "$.signed['" + Steps.T05_ExtractEntityKeyRestAssured.entityKey
                    + "']['" + imageName + "'][0]");
            System.out.println("Signed URL for unsealed: " + UnSealedImgsURL);
            Assert.assertNotNull(UnSealedImgsURL, "The signed URL for unsealed should not be null.");
        } else {
            Assert.fail("Unknown image name provided: " + imageName);
        }
    }
}
