package Steps;
import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static Hooks.Hooks.driver;
import static io.restassured.RestAssured.given;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

import java.time.Duration;

public class T12_UnsealRestAssured {
    private Response unsealResponse;

    @Given("a signed URL for unseal is available")
    public void a_signed_url_is_available_Unseal() {
        Assert.assertNotNull(Steps.T08_SignedUrlsCreationRestAssured.UnSealedImgsURL, "Signed URL should not be null");
        System.out.println("Signed URL for unseal: " + Steps.T08_SignedUrlsCreationRestAssured.UnSealedImgsURL);
    }
    @Given("the necessary trip unsealing details are available")
    public void the_necessary_trip_unsealing_details_are_available() {
        assertNotNull("Entity Key should not be null", Steps.T05_ExtractEntityKeyRestAssured.entityKey);
        assertNotNull("Seal Number should not be null",  Steps.T10_ExtractSealNrRestAssured.SealNr);
        assertNotNull("UnSealed Image URL should not be null", Steps.T08_SignedUrlsCreationRestAssured.UnSealedImgsURL);
    }
    @When("I send an unseal action request to the hub")
    public void i_send_an_unseal_action_request_to_the_hub() {
        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/hub/actions";
        String requestBody = "{"
                + "\"actions\": {"
                + "  \"" + Steps.T05_ExtractEntityKeyRestAssured.entityKey + "\": {"
                + "    \"action_code\": \"unseal\","
                + "    \"seal_nr\": \"" + Steps.T10_ExtractSealNrRestAssured.SealNr + "\","
                + "    \"entity_type\": \"seal_nr\","
                + "    \"media_urls\": {"
                + "      \"trip-unsealed.jpg\": ["
                + "        \"" + Steps.T08_SignedUrlsCreationRestAssured.UnSealedImgsURL + "\""
                + "      ]"
                + "    }"
                + "  }"
                + "}"
                + "}";

        System.out.println("Unseal Request Body: " + requestBody);

        unsealResponse = given()
                .header("accept", "application/json")
                .header("X-Facility-Type", "hub")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(baseUrl + endpoint);

        System.out.println("Response Status Code: " + unsealResponse.getStatusCode());
        System.out.println("Response Body: " + unsealResponse.getBody().asString());
    }
    @Then("I should receive a successful unseal action response")
    public void i_should_receive_a_successful_unseal_action_response() {
        assertEquals(200, unsealResponse.getStatusCode());
    }
    @Then("I should see the trip as unsealed on the UI")
    public void i_should_see_the_trip_as_unsealed_on_the_ui() {
        String entityDetailsXpath = "//android.widget.TextView[@text='Entity Details']";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement entityDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(entityDetailsXpath)));
        assertTrue("Entity Details should be visible on the screen", entityDetails.isDisplayed());

        String loadButtonXpath = "//android.widget.TextView[@text=\"LOAD\"]\n";
        WebElement loadButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loadButtonXpath)));
        assertTrue("Load button should be visible, indicating that the trip is unloaded", loadButton.isDisplayed());

    }
}
