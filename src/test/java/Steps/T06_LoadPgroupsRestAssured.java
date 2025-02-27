package Steps;
import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

import static Hooks.Hooks.driver;
import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.assertTrue;


public class T06_LoadPgroupsRestAssured {
    private Response response;

    @Given("a Pgroup exists")
    public void a_pgroup_exists() {
        Assert.assertNotNull(Steps.T04_CreatePgroupsRestAssured.PgroupCode, "Pgroup code is null");
    }
    @And("Entity page is loaded")
    public void Entitty_pge_is_loaded(){
        String entityDetailsXpath = "//android.widget.TextView[@text='Entity Details']";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement entityDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(entityDetailsXpath)));
        assertTrue("Entity Details should be visible on the screen", entityDetails.isDisplayed());
    }
    @When("I load the Pgroup using the API")
    public void i_load_the_pgroup_using_the_api() {
        String cookieName = "Cookie";
        String url = "https://express-api.noonstg.team/hub/actions";
        System.out.println(Steps.T05_ExtractEntityKeyRestAssured.entityKey);
        System.out.println(Steps.T04_CreatePgroupsRestAssured.PgroupCode);
        String actionPayload = "{\n" +
                "  \"actions\": {\n" +
                "    \"" + Steps.T05_ExtractEntityKeyRestAssured.entityKey + "\": {\n" +
                "      \"action_code\": \"load\",\n" +
                "      \"awb_nr\": \"" + Steps.T04_CreatePgroupsRestAssured.PgroupCode + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        response = given()
                .header("accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(actionPayload)
                .when()
                .post(url);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Then("the API response should be successful")
    public void the_api_response_should_be_successful() {
        if (response.getStatusCode() != 200) {
            throw new AssertionError("Expected status code 200 but got " + response.getStatusCode());
        }
    }

    @Then("I ensure that the Pgroup is loaded on the UI")
    public void i_ensure_that_the_pgroup_is_loaded_on_the_ui() {
        // Wait until the Entity Details element is present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entityDetails = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));

        // Refresh the view by navigating back and then re-login
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//com.horcrux.svg.RectView")));
        backButton.click();

        WebElement threePoints = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")));
        threePoints.click();

        WebElement enterTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCode.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("android.widget.EditText")));
        // Use the trip code from your other test case
        tripCodeField.sendKeys(T01_CreateTripRestAssured.tripCode);

        WebElement submitTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCode.click();
    }
}
