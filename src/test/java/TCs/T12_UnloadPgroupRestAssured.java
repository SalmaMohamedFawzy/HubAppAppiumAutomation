package TCs;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static Hooks.Hooks.driver;
import static io.restassured.RestAssured.given;

public class T12_UnloadPgroupRestAssured {
    @Test
    public void T1_UnLoadPgroups() {
        String cookieName = "Cookie";
        String url = "https://express-api.noonstg.team/hub/scan/action";
       // System.out.println(T05_ExtractEntityKeyRestAssured.EntityKey );
       // System.out.println(T04_CreatePgroupsRestAssured.pgroupCode );
        String requestBody = "{"
                + "\"entity_key\": \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\","
                + "\"action_code\": \"unload\","
                + "\"scans\": [\"" + T04_CreatePgroupsRestAssured.pgroupCode + "\"],"
                + "\"scans_media\": {}"
                + "}";
        Response response = given()
                .header("accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(url);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }
    @Test
    public void T2_ensureTripIsUnLoaded() {
        //ensure that page is fully loaded
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entityDetails = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc='Entity Details']")));
        //go back &login again to refresh
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//com.horcrux.svg.RectView")));
        backButton.click();
        WebElement ThethreePointss = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//com.horcrux.svg.RectView")));
        ThethreePointss.click();
        WebElement EnterTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Enter Middle Mile Trip Code\"]")));
        EnterTripCodee.click();
        WebElement tripCodeFieldd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")));
        tripCodeFieldd.sendKeys(T02_CreateTripRestAssured.tripCode);// use trip code from the sec test case
        WebElement submitTripCodee = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]")));
        submitTripCodee.click();
    }
}
