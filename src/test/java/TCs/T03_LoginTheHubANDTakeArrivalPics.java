package TCs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static Hooks.Hooks.driver;
public class T03_LoginTheHubANDTakeArrivalPics {
    @Test
    public void LoginTheHubANDTakeArrivalPics() throws IOException, InterruptedException {
        //login to the first hub using trip code (instead of scan)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
        WebElement FirstHubTESTA1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"TEST-A1\"]")));
        FirstHubTESTA1.click();
        /////////////tc01 send correct trip code
        Thread.sleep(1000);
        WebElement ThethreePointss = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        ThethreePointss.click();
        WebElement EnterTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        EnterTripCodee.click();
        WebElement tripCodeFieldd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeFieldd.sendKeys(T02_CreateTripRestAssured.tripCode);// use trip code from the sec test case
        WebElement submitTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCodee.click();
        //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Entity Details")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));

        //take arrival pics ---- if it is the first time to visit this trip
        WebElement TakeArrivalPics = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"TAKE ARRIVAL PICTURES\"]")));
        TakeArrivalPics.click();
        //send dock number
        WebElement DockNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText")));
        DockNumber.sendKeys("2");
        WebElement SubmitDockNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        SubmitDockNumber.click();
        WebElement CapturePhotosButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Capture photos\"]")));
        CapturePhotosButton.click();
        WebElement CameraButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.PathView")));
        CameraButton.click();
        WebElement UploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Upload photos\"]")));
        UploadButton.click();
        WebElement CompleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Complete\"]")));
        CompleteButton.click();
        /*
        WebElement BackButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        BackButton.click();
        /////////////tc02 send incorrect trip code --- use abc///////////////////////////////////////
        WebElement The3Points = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.GroupView/com.horcrux.svg.GroupView/com.horcrux.svg.CircleView[3]")));
        The3Points.click();
        WebElement EnterTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        EnterTripCode.click();
        WebElement tripCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeField.sendKeys("ABC");
        WebElement submitTripCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCode.click();
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Hub State Error']/android.view.ViewGroup")));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message was not displayed.");
*/
    }
}
