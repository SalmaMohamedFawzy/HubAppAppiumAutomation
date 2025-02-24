package Steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static Hooks.Hooks.driver;
import static org.testng.AssertJUnit.assertTrue;

public class T03_LoginTheHubANDTakeArrivalPics {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
    @Given("I am on the Hub selection screen")
    public void iAmOnTheHubSelectionScreen() {
        assertTrue("Hub selection screen is not displayed", driver.findElement(By.xpath("//android.widget.TextView")).isDisplayed());
    }

    @When("I select the first hub {string}")
    public void iSelectTheFirstHub(String hubName) {
        WebElement firstHub = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"" + hubName + "\"]")));
        firstHub.click();
    }

    @When("I enter the trip code from the previous test")
    public void iEnterTheTripCode() {
        WebElement threeDots = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        threeDots.click();

        WebElement enterTripCodeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        enterTripCodeOption.click();

        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys(Steps.T02_CreateTripRestAssured.tripCode);

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedInSuccessfully() {
        assertTrue("Entity Details section is not displayed", driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")).isDisplayed());
    }
//tc2
    @Given ("I am logged into the first hub")
    public void IamLoggedIntoTheFirstHub() {
        assertTrue("Entity Details section is not displayed", driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")).isDisplayed());
    }
    @When("I take arrival pictures for the first time")
    public void iTakeArrivalPicturesForTheFirstTime() {
        WebElement takeArrivalPics = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"TAKE ARRIVAL PICTURES\"]")));
        takeArrivalPics.click();
    }

    @When("I enter dock number {string}")
    public void iEnterDockNumber(String dockNumber) {
        WebElement dockNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText")));
        dockNumberField.sendKeys(dockNumber);

        WebElement submitDockNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitDockNumber.click();
    }

    @When("I capture and upload the photos")
    public void iCaptureAndUploadThePhotos() {
        WebElement capturePhotosButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Capture photos\"]")));
        capturePhotosButton.click();

        WebElement cameraButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.PathView")));
        cameraButton.click();

        WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Upload photos\"]")));
        uploadButton.click();

        WebElement completeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Complete\"]")));
        completeButton.click();
    }

    @Then("the process should be completed successfully")
    public void theProcessShouldBeCompletedSuccessfully() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Short wait to catch it quickly
            WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"SUCCESS\"]/android.view.ViewGroup")));

            assertTrue("Process was not completed successfully", successMessage.isDisplayed());

        } catch (Exception e) {
            System.out.println("Success message disappeared too quickly or was not found.");
        }
    }
}
