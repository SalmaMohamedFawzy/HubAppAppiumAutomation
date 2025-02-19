package TCs;

import Hooks.ApiCookieManager;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static Hooks.Hooks.driver;
import static io.restassured.RestAssured.given;

public class T06_LoadPgroupsRestAssured {
    @Test
    public void T1_LoadPgroups() {
        String cookieName = "Cookie";
        String url = "https://express-api.noonstg.team/hub/actions";
        System.out.println(T05_ExtractEntityKeyRestAssured.EntityKey );
        System.out.println(T04_CreatePgroupsRestAssured.pgroupCode );
        String actionPayload = "{\n" +
                "  \"actions\": {\n" +
                "    \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\": {\n" +
                "      \"action_code\": \"load\",\n" +
                "      \"awb_nr\": \"" + T04_CreatePgroupsRestAssured.pgroupCode + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Response response = given()
                .header("accept", "application/json")
                .header("X-App-Build", "238")
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
    @Test
    public void T2_ensurePgroupisLoaded() {
        //ensure that page is fully loaded
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entityDetails = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));
        //go back &login again to refresh
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.RectView")));
        backButton.click();
        WebElement ThethreePointss = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")));
        ThethreePointss.click();
        WebElement EnterTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        EnterTripCodee.click();
        WebElement tripCodeFieldd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeFieldd.sendKeys(T02_CreateTripRestAssured.tripCode);// use trip code from the sec test case
        WebElement submitTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCodee.click();
    }
}