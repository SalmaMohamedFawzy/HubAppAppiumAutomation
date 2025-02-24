package Steps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import Configurations.ApiCookieManager;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

public class T02_CreateTripRestAssured {
    public static String tripCode;
    private Response response;
    private final String apiEndpoint = "https://sc-express-api-middlemile.noonstg.team/staging/test-data/trip";

    @Given("I have a valid API cookie")
    public void iHaveAValidApiCookie() {
        assertNotNull("API Cookie should not be null", ApiCookieManager.COOKIE_VALUE);
    }

    @When("I send a request to create a trip with stops {string}, {string}, {string}")
    public void iSendARequestToCreateATrip(String stop1, String stop2, String stop3) {
        String createTripPayload = "{\n" +
                "  \"stage\": \"trip-created\",\n" +
                "  \"stops\": [\"" + stop1 + "\", \"" + stop2 + "\", \"" + stop3 + "\"],\n" +
                "  \"vendor\": \"noon\",\n" +
                "  \"vehicle_type\": \"canter_4tn\"\n" +
                "}";

        response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(createTripPayload)
                .when()
                .post(apiEndpoint);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        System.out.println("Response Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);
    }

    @Then("I should receive a response with a trip code")
    public void iShouldReceiveAResponseWithTripCode() {
        JsonPath jsonPath = response.jsonPath();
        tripCode = jsonPath.getString("code");

        System.out.println("Extracted Trip Code: " + tripCode);
        assertNotNull("Trip code should not be null", tripCode);
    }
}
