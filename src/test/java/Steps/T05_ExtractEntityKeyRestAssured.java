package Steps;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
public class T05_ExtractEntityKeyRestAssured {
    private Response response;
    public static String entityKey;
    private final String apiEndpoint = "https://express-api.noonstg.team/hub/scan/entity";

    @Given("I have a valid API cookie and app build number")
    public void iHaveAValidApiCookieAndAppBuildNumber() {
        assertNotNull("API Cookie should not be null", ApiCookieManager.COOKIE_VALUE);
        assertNotNull("App Build Number should not be null", BuildNumberManager.APP_BUILD_NUMBER);
    }

    @When("I send a request to extract the entity key using the trip code")
    public void iSendARequestToExtractTheEntityKeyUsingTheTripCode() {
        String scanPayload = "{ \"scan\": \"MMTR::" + T02_CreateTripRestAssured.tripCode + "\" }";

        response = given()
                .header("Accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(scanPayload)
                .when()
                .post(apiEndpoint);
    }
    @Then("I should receive a response containing the entity key")
    public void iShouldReceiveAResponseContainingTheEntityKey() {
        String jsonResponse = response.getBody().asString();
        entityKey = JsonPath.read(jsonResponse, "$.entity_key");

        System.out.println("Extracted Entity Key: " + entityKey);
        assertNotNull("Entity Key should not be null", entityKey);
    }
}
