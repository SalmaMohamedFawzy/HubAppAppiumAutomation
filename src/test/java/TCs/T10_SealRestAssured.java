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

public class T10_SealRestAssured {
    @Test
    public void T1_SealApi() throws InterruptedException {

        System.out.println("Entity Key: " + T05_ExtractEntityKeyRestAssured.EntityKey);
        System.out.println("Seal Number: " + T09_ExtractSealNrRestAssured.SealNr);
        System.out.println("Sealed Image URL: " + T07_SignedUrlsCreationRestAssured.SealedImgsURL);
        Thread.sleep(500);
        /*String entityKey = "middlemile_trip:1022";
        String sealNumber = "MMTS::TSG261535654350A";
        String mediaUrl = "https://storage.googleapis.com/noon-staging-express-eu/middlemile/trip/ae/TEST-A1/2025-02-17/1022/trip-sealed/1739791586705_1.jpg?Expires=1739795186&GoogleAccessId=sc-express-api%40noonstg-sc.iam.gserviceaccount.com&Signature=xPXcRerNGh2okCevJnyMW%2FsXjOc4HQ1b0EMN02BCgY%2BU8bBAAtTTtvCJnmFii4WTd6hb5r%2B6DzQHpYCT5FSRKEVjgIaQ2Yyw1Uj3s9FmNzleIBuCUuVxQZCcA4ioOuGGrhESe2HIp7YAI5nrvNobYhlUe4vNeXazXLJRUVwHHl2XhgrK%2BYKCbNN7JWidyuWu6QaMByz1lESqOloS9%2Bi5Bel6Wh7s3N5gbtUz%2FuWspyzEKj3D1ygIM5HGRs%2B5I%2BCDEOAJESEGOcVRXo03FSaIBD4FQQEPhQ5o094rzn12Ar5Sxci%2BFIw%2BkPHymnQ5c1591vBIdaKYdJLTQlFjXYqfVQ%3D%3D";
         */

        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/hub/actions";
        String requestBody = "{"
                + "\"actions\": {"
                + "  \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\": {"
                + "    \"action_code\": \"seal\","
                + "    \"seal_nr\": \"" + T09_ExtractSealNrRestAssured.SealNr + "\","
                + "    \"entity_type\": \"seal_nr\","
                + "    \"media_urls\": {"
                + "      \"trip-sealed.jpg\": ["
                + "        \"" + T07_SignedUrlsCreationRestAssured.SealedImgsURL + "\""
                + "      ]"
                + "    }"
                + "  }"
                + "}"
                + "}";
        System.out.println("Final Request Body: " + requestBody);

        Response response = given()
                .header("accept", "application/json")
                .header("X-Facility-Type", "hub")
                .header("X-App-Build", "238")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(baseUrl + endpoint);
        String jsonResponse = response.getBody().asString();
        System.out.println(jsonResponse);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }
    @Test
    public void T2_ensureTripIsSealed() {
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
