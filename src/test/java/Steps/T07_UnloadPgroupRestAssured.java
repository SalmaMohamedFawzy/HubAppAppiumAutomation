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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class T07_UnloadPgroupRestAssured {
    private Response unloadResponse;

    @Given("the necessary unload details are available")
    public void the_necessary_unload_details_are_available() {
        assertNotNull("Entity Key should not be null",Steps.T05_ExtractEntityKeyRestAssured.entityKey);
        assertNotNull("Pgroup Code should not be null", Steps.T04_CreatePgroupsRestAssured.PgroupCode);
    }

    @When("I send an unload request for the pgroup")
    public void i_send_an_unload_request_for_the_pgroup() {
        String cookieName = "Cookie";
        String url = "https://express-api.noonstg.team/hub/scan/action";
        String requestBody = "{"
                + "\"entity_key\": \"" + Steps.T05_ExtractEntityKeyRestAssured.entityKey + "\","
                + "\"action_code\": \"unload\","
                + "\"scans\": [\"" + Steps.T04_CreatePgroupsRestAssured.PgroupCode + "\"],"
                + "\"scans_media\": {}"
                + "}";
        unloadResponse = given()
                .header("accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(url);
    }

    @Then("I should receive a successful unload response")
    public void i_should_receive_a_successful_unload_response() {
        System.out.println("Response Status Code: " + unloadResponse.getStatusCode());
        System.out.println("Response Body: " + unloadResponse.getBody().asString());
        assertEquals("Expected status code 200", 200, unloadResponse.getStatusCode());
    }

    @Then("I should see that the trip is unloaded on the UI")
    public void i_should_see_that_the_trip_is_unloaded_on_the_ui() {
        String entityDetailsXpath = "//android.widget.TextView[@text='Entity Details']";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement entityDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(entityDetailsXpath)));
        assertTrue("Entity Details should be visible on the screen", entityDetails.isDisplayed());

        String loadButtonXpath = "//android.widget.TextView[@text=\"LOAD\"]\n";
        WebElement loadButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(loadButtonXpath)));
        assertTrue("Load button should be visible, indicating that the trip is unloaded", loadButton.isDisplayed());
    }

}
