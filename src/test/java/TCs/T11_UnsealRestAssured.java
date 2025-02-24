package TCs;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

import static Hooks.Hooks.driver;
import static TCs.T07_SignedUrlsCreationRestAssured.SealedImgsURL;
import static io.restassured.RestAssured.given;
import static TCs.T08_AddPicsToURL.uploadFile;
public class T11_UnsealRestAssured {
    public static String UnSealedImgsURL;

    //same flow as seal -- using same sealnr

    @Test
    public void T1_UnsealSignedUrlsCreation(){
        String cookieName = "Cookie";
        String Payload = "{ \"sign\": { \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\": { \"trip-unsealed.jpg\": 1 } } }";

        Response response = given()
                .header("accept", "application/json")
                .header("X-App-Build", "238")
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(Payload)
                .when()
                .post("https://express-api.noonstg.team/hub/signed-urls");
        String jsonResponse = response.getBody().asString();
        System.out.println(jsonResponse);
        UnSealedImgsURL = JsonPath.read(jsonResponse,  "$.signed['" + T05_ExtractEntityKeyRestAssured.EntityKey + "']['trip-unsealed.jpg'][0]");
        System.out.println("url is : " +UnSealedImgsURL);
    }
    @Test
    public void T2_testUploadFile() {
        // Load the file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("black_img.jpg").getFile());

        String contentType = "image/jpeg";
        boolean result = uploadFile(UnSealedImgsURL, file, contentType);

        Assert.assertTrue(result, "File upload should be successful");
    }
    @Test
    public void T3_UnSealApi() throws InterruptedException {

        System.out.println("Entity Key: " + T05_ExtractEntityKeyRestAssured.EntityKey);
        System.out.println("Seal Number: " + T09_ExtractSealNrRestAssured.SealNr);
        System.out.println("Sealed Image URL: " + UnSealedImgsURL);
        Thread.sleep(500);

        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/hub/actions";
        String requestBody = "{"
                + "\"actions\": {"
                + "  \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\": {"
                + "    \"action_code\": \"unseal\","
                + "    \"seal_nr\": \"" + T09_ExtractSealNrRestAssured.SealNr + "\","
                + "    \"entity_type\": \"seal_nr\","
                + "    \"media_urls\": {"
                + "      \"trip-unsealed.jpg\": ["
                + "        \"" + UnSealedImgsURL + "\""
                + "      ]"
                + "    }"
                + "  }"
                + "}"
                + "}";
        System.out.println("Final Request Body: " + requestBody);

        Response response = given()
                .header("accept", "application/json")
                .header("X-Facility-Type", "hub")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
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
    public void T4_ensureTripIsUnSealed() {
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
