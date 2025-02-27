package TCs;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

import static Hooks.Hooks.driver;
import Hooks.Hooks;
public class T01_LoginIntoHubApp {

    @Test
    public void LoginIntoHubApp() throws InterruptedException, MalformedURLException {
        if (driver == null) {
            Hooks hooks = new Hooks();
            hooks.SetUp();
        }
       /*WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        ((RemoteWebDriver) driver).executeScript("mobile: deepLink", ImmutableMap.of(
                "url", "https://misc-team.noonstg.team/ops",
                "package", "com.android.chrome"
        ));   */

        //click on scan first QR code
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        WebElement ScanFirstQR = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB81\uDC32, SCAN FIRST QR\"]\n"))); //id didn't work
        ScanFirstQR.click();

        //wait until the scanner opens
         wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.TextView")));

       //click to allow scanner
        WebElement allowButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("com.android.permissioncontroller:id/permission_allow_button")));
        allowButton.click();

        // wait untill scan the login QR code manually and login dash appears
        //WebElement loginSuccessElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout\n")));
        Thread.sleep(7000);

        // scroll down 3 times
        int screenHeight = driver.manage().window().getSize().height;
        int screenWidth = driver.manage().window().getSize().width;
        int startX = screenWidth / 2;
        int startY = (int) (screenHeight * 0.8);  // Start at 80% of the screen
        int endY = (int) (screenHeight * 0.2);    // End at 20% of the screen

        TouchAction action = new TouchAction(driver);
        for (int i = 0; i < 3; i++) {
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                    .moveTo(PointOption.point(startX, endY))
                    .release()
                    .perform();
        }
        //click on the login link
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='https://auth.noonstg.team/appdl?scheme=noon-team-express-hub']")));
        loginLink.click();

        // wait until the select hub page loads
         wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"\uDB83\uDF81, TEST-Z1, hub\"]")));
    }

}
