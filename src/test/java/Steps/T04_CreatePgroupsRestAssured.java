package Steps;

import Configurations.ApiCookieManager;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

public class T04_CreatePgroupsRestAssured {
    public static String PgroupCode;
    private Response response;
    private final String apiEndpoint = "https://sc-express-api-middlemile.noonstg.team/staging/test-data/trip";

    @When("I send a request to create a Pgroup with stops {string} and {string}")
    public void iSendARequestToCreateAPgroup(String stop1, String stop2) {
        String createPgroupPayload = "{\n" +
                "  \"stage\": \"pgroups-created\",\n" +
                "  \"stops\": [\"" + stop1 + "\", \"" + stop2 + "\"]\n" +
                "}";

        response = given()
                .header("Accept", "application/json")
                .header("N-Hub", stop1)
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(createPgroupPayload)
                .when()
                .post(apiEndpoint);
    }
    @Then("I should receive a response containing a Pgroup code")
    public void iShouldReceiveAResponseContainingAPgroupCode() {
        String jsonResponse = response.getBody().asString();
        int statusCode = response.getStatusCode();
        System.out.println(jsonResponse);
        System.out.println(statusCode);

        List<String> extractedValues = JsonPath.read(jsonResponse, "$.pgroups_map.TEST-A1.TEST-A2");
        PgroupCode = extractedValues.get(0);
        System.out.println("Extracted Pgroup Code: " + PgroupCode);
        assertNotNull("Pgroup code should not be null", PgroupCode);
    }
}
