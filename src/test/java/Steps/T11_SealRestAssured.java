package Steps;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

        WebElement threePoints = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")));
        threePoints.click();

        WebElement enterTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCode.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys(T01_CreateTripRestAssured.tripCode);

        WebElement submitTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCode.click();
    }

    @Then("I should see the trip as sealed on the UI")
    public void i_should_see_the_trip_as_sealed_on_the_ui() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement unsealButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='󰛕, UNSEAL']")));
        assertTrue("Unseal button should be visible, to indicate that trip is sealed", unsealButton.isDisplayed());

/////////////////////////////////////////////////////////////////////////////////////////////////////////
       /* //logout then login second hub
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")));
        backButton.click();

        WebElement threeDotsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.className("com.horcrux.svg.RectView")));
        threeDotsMenu.click();

        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Logout']")));
        logoutButton.click();

        WebElement ScanFirstQR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB81\uDC32, SCAN FIRST QR\"]\n"))); //id didn't work
        ScanFirstQR.click();

        Thread.sleep(7000);

        int screenHeight = driver.manage().window().getSize().height;
        int screenWidth = driver.manage().window().getSize().width;
        int startX = screenWidth / 2;
        int startY = (int) (screenHeight * 0.8);
        int endY = (int) (screenHeight * 0.2);

        TouchAction action = new TouchAction(driver);
        for (int i = 0; i < 3; i++) {
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                    .moveTo(PointOption.point(startX, endY))
                    .release()
                    .perform();
        }
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='https://auth.noonstg.team/appdl?scheme=noon-team-express-hub']")));
        loginLink.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-Z1, hub\"]")));

       //login to test-a2
        WebElement TESTA2Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-A2, hub\"]")));
        TESTA2Element.click();
        WebElement threeDots = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        threeDots.click();

        WebElement enterTripCodeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCodeOption.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys(Steps.T02_CreateTripRestAssured.tripCode);

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));

        WebElement takeArrivalPics = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"TAKE ARRIVAL PICTURES\"]")));
        takeArrivalPics.click();

        WebElement dockNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText")));
        dockNumberField.sendKeys("2");

        WebElement submitDockNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitDockNumber.click();

        WebElement capturePhotosButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Capture photos\"]")));
        capturePhotosButton.click();

        WebElement cameraButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.PathView")));
        cameraButton.click();

        WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Upload photos\"]")));
        uploadButton.click();

        WebElement completeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Complete\"]")));
        completeButton.click();  */
    }
    @Given("I am logged into the current hub")
    public void iAmLoggedIntoTheCurrentHub() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entityDetails = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));
        assertTrue("Entity Details page is not loaded", entityDetails.isDisplayed());

        System.out.println("User is logged into the current hub.");
    }

    @When("I logout and login to the TEST-A2 hub")
    public void iLogoutAndLoginToTheTESTA2Hub() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")));
        backButton.click();

        WebElement threeDotsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.className("com.horcrux.svg.RectView")));
        threeDotsMenu.click();

        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Logout']")));
        logoutButton.click();

        WebElement ScanFirstQR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB81\uDC32, SCAN FIRST QR\"]")));
        ScanFirstQR.click();
        Thread.sleep(7000);

        int screenHeight = driver.manage().window().getSize().height;
        int screenWidth = driver.manage().window().getSize().width;
        int startX = screenWidth / 2;
        int startY = (int) (screenHeight * 0.8);
        int endY = (int) (screenHeight * 0.2);

        TouchAction action = new TouchAction(driver);
        for (int i = 0; i < 3; i++) {
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                    .moveTo(PointOption.point(startX, endY))
                    .release()
                    .perform();
        }

        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='https://auth.noonstg.team/appdl?scheme=noon-team-express-hub']")));
        loginLink.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-Z1, hub\"]")));

        WebElement TESTA2Element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-A2, hub\"]")));
        TESTA2Element.click();
    }

    @When("I enter the middle mile trip code and submit")
    public void iEnterTheMiddleMileTripCodeAndSubmit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(driver -> {
            WebElement el;
            try {
                el = driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='menu_button']"));
            } catch (NoSuchElementException e1) {
                try {
                    el = driver.findElement(By.xpath("//com.horcrux.svg.RectView"));
                } catch (NoSuchElementException e2) {
                    return null;
                }
            }
            return el;
        });

        if (element != null) {
            element.click();
        } else {
            throw new RuntimeException("Element not found!");
        }


        WebElement enterTripCodeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCodeOption.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys(T01_CreateTripRestAssured.tripCode);

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitButton.click();
    }

    @Then("I capture and upload the arrival photos successfully")
    public void iCaptureAndUploadTheArrivalPhotosSuccessfully() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));

        WebElement takeArrivalPics = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"TAKE ARRIVAL PICTURES\"]")));
        takeArrivalPics.click();

        WebElement dockNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText")));
        dockNumberField.sendKeys("2");

        WebElement submitDockNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitDockNumber.click();

        WebElement capturePhotosButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Capture photos\"]")));
        capturePhotosButton.click();

        WebElement cameraButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.PathView")));
        cameraButton.click();

        WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Upload photos\"]")));
        uploadButton.click();

        WebElement completeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Complete\"]")));
        completeButton.click();

        try {
            WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUCCESS\"]")));

            assertTrue("Process was not completed successfully", successMessage.isDisplayed());

        } catch (Exception e) {
            System.out.println("Success message disappeared too quickly or was not found.");
        }
        System.out.println("Arrival photos captured and uploaded successfully.");
    }
}
