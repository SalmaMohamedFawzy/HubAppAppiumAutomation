package Steps;

import io.cucumber.java.en.Given;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.time.Duration;
import Hooks.Hooks;
import static Hooks.Hooks.driver;

public class T01_LoginIntoHubApp {
    @Given("the Hub App is open")
    public void openHubApp() throws MalformedURLException {
        if (driver == null) {
            Hooks hooks = new Hooks();
            hooks.SetUp();
        }
    }
    /*@When("I go to the link")
    public void i_go_to_the_link() {
        driver.get("https://misc-team.noonstg.team/ops#qr");
    }*/

    @When("I scan the first QR code")
    public void scanFirstQRCode() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        WebElement scanFirstQR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB81\uDC32, SCAN FIRST QR\"]\n")));
        scanFirstQR.click();
    }

    @When("I allow camera permission")
    public void allowCameraPermission() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        WebElement allowButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.android.permissioncontroller:id/permission_allow_button")));
        allowButton.click();
    }

    @When("I manually scan the login QR code")
    public void manuallyScanQRCode() throws InterruptedException {
        Thread.sleep(7000);  // Simulating manual scanning
    }

    @When("I scroll down multiple times")
    public void scrollDownMultipleTimes() {
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
    }

    @When("I click on the login link")
    public void clickOnLoginLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='https://auth.noonstg.team/appdl?scheme=noon-team-express-hub']")));
        loginLink.click();
    }

    @Then("I should see the Select Hub page")
    public void verifySelectHubPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-Z1, hub\"]")));
    }
}
