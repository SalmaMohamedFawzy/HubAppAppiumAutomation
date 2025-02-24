package Steps;

import Configurations.ApiCookieManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotEquals;

import com.jayway.jsonpath.JsonPath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T09_ExtractSealNrRestAssured {
    private String batchNumber;
    public static String SealNr;

    @Given("I have generated a trip seal batch with hub {string} destination and quantity {int}")
    public void i_have_generated_a_trip_seal_batch(String hub, int quantity) {
        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/control/pregen/tripseal-batch";
        String requestBody = "{ \"hub_dst\": \"" + hub + "\", \"qty\": " + quantity + " }";

        Response response = given()
                .header("accept", "application/json")
                .header("N-Hub", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(baseUrl + endpoint);

        String jsonResponse = response.getBody().asString();
        String pregenBatchKey = JsonPath.read(jsonResponse, "$.pregen_batch_key");
        // Assuming the pregenBatchKey format is something like "XYZ/BatchNumber"
        batchNumber = pregenBatchKey.split("/")[1];
        System.out.println("Extracted Batch Number: " + batchNumber);
    }

    @When("I retrieve the trip seal batch using the batch number")
    public void i_retrieve_the_trip_seal_batch_using_the_batch_number() {
        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/control/pregen/tripseal-batch/" + batchNumber;

        Response response = given()
                .header("accept", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .when()
                .get(baseUrl + endpoint);

        String jsonResponse = response.getBody().asString();
        Pattern pattern = Pattern.compile("(MMTS::\\S+)");
        Matcher matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            SealNr = matcher.group(1).replaceAll("\"$", "");
        } else {
            SealNr = "Not Found";
        }
        System.out.println("Extracted Trip Seal Number: " + SealNr);
    }

    @Then("I should extract a valid trip seal number")
    public void i_should_extract_a_valid_trip_seal_number() {
        assertNotEquals("Not Found", SealNr);
    }
}
