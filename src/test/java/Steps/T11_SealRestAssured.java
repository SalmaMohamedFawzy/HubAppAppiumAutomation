package Steps;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static Hooks.Hooks.driver;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class T11_SealRestAssured {
    private Response sealResponse;

    @Given("the necessary trip sealing details are available")
    public void the_necessary_trip_sealing_details_are_available() {
        assertNotNull("Entity Key should not be null", Steps.T05_ExtractEntityKeyRestAssured.entityKey);
        assertNotNull("Seal Number should not be null",  Steps.T10_ExtractSealNrRestAssured.SealNr);
        assertNotNull("Sealed Image URL should not be null", Steps.T08_SignedUrlsCreationRestAssured.SealedImgsURL);
    }

    @When("I send a seal action request to the hub")
    public void i_send_a_seal_action_request_to_the_hub() {
        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/hub/actions";
        String requestBody = "{"
                + "\"actions\": {"
                + "  \"" + Steps.T05_ExtractEntityKeyRestAssured.entityKey + "\": {"
                + "    \"action_code\": \"seal\","
                + "    \"seal_nr\": \"" + T10_ExtractSealNrRestAssured.SealNr + "\","
                + "    \"entity_type\": \"seal_nr\","
                + "    \"media_urls\": {"
                + "      \"trip-sealed.jpg\": ["
                + "        \"" + T08_SignedUrlsCreationRestAssured.SealedImgsURL + "\""
                + "      ]"
                + "    }"
                + "  }"
                + "}"
                + "}";

        System.out.println("Seal Request Body: " + requestBody);

        sealResponse = given()
                .header("accept", "application/json")
                .header("X-Facility-Type", "hub")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(baseUrl + endpoint);

        System.out.println("Response Status Code: " + sealResponse.getStatusCode());
        System.out.println("Response Body: " + sealResponse.getBody().asString());
    }

    @Then("I should receive a successful seal action response")
    public void i_should_receive_a_successful_seal_action_response() {
        assertEquals(200, sealResponse.getStatusCode());
    }

    @Given("I navigate to the entity details page")
    public void i_navigate_to_the_entity_details_page() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entityDetails = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));
        assertTrue("Entity Details page is not loaded", entityDetails.isDisplayed());
    }

    @When("I refresh the trip details using the trip code")
    public void i_refresh_the_trip_details_using_the_trip_code() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.RectView")));
        backButton.click();

        WebElement threeDots = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        threeDots.click();

        WebElement enterTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCode.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys(Steps.T02_CreateTripRestAssured.tripCode);

        WebElement submitTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCode.click();
    }

    @Then("I should see the trip as sealed on the UI")
    public void i_should_see_the_trip_as_sealed_on_the_ui() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement unsealButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='󰛕, UNSEAL']")));
        assertTrue("Unseal button should be visible, to indicate that trip is sealed", unsealButton.isDisplayed());
    }
}
