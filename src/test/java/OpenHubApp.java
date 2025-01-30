import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.Duration;

import static io.restassured.RestAssured.given;

public class OpenHubApp {
    AndroidDriver driver;
    @BeforeTest
    public void SetUp() throws MalformedURLException {
        DesiredCapabilities caps=new DesiredCapabilities();
        caps.setCapability("platformName","Android");
        caps.setCapability("appium:platformVersion","10");
        caps.setCapability("appium:deviceName","EDA51K");
        caps.setCapability("appium:app","C:\\Users\\snagy\\Downloads\\HubApp.apk");
        //  caps.setCapability("browserName","Chrome");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);


    }
    @Test
    public void OpenHubApp() throws InterruptedException {
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

        // scroll down 4 times
        int screenHeight = driver.manage().window().getSize().height;
        int screenWidth = driver.manage().window().getSize().width;
        int startX = screenWidth / 2;
        int startY = (int) (screenHeight * 0.8);  // Start at 80% of the screen
        int endY = (int) (screenHeight * 0.2);    // End at 20% of the screen

        TouchAction action = new TouchAction(driver);
        for (int i = 0; i < 5; i++) {
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

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String url = "https://sc-express-api-middlemile.noonstg.team";
        RestAssured.baseURI = url; // Set it as base URI
        String cookieName = "SessionCookie";
        String cookieValue = "_ntrtnetid=nav1.public.eyJob3N0cyI6W10sInJvbGVzIjpbImxtcy1taWRkbGVtaWxlLXRyaXAtZWRpdG9yIiwibG1zLW1pZGRsZW1pbGUtYWRtaW4iLCJsbXMtbWlkZGxlbWlsZS1zdXBlcnZpc29yIiwiZGV2ZWxvcGVyIiwidGVjaC1lZ3lwdCIsImxtcy1hZG1pbiIsImxtcy1taWRkbGVtaWxlLXJvdXRlLWVkaXRvciIsInRlY2gtZWd5cHQtcWEiLCJsbXMtbGFzdG1pbGUtYWRtaW4iLCJmaW5hbmNlLXJlYWRvbmx5Il0sImVtYWlsIjoic25hZ3lAbm9vbi5jb20iLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSzJtdUp3RWRpaEhfdHFELXFEMVQxb01Pckk4RE1Tc0JBY0lHc193blFxNmFwak5LOU09czk2LWMiLCJpYXQiOjE3MzgyMjY5NDEsInR5cGUiOiJkb21haW4iLCJ0aWQiOiI5M2U2NWRmNS0xZjQyLTQzNDgtYmVmMC1jMzY4MjA5YzU5ZDQiLCJzaWQiOiIwYThiNTVhNi04Y2Q4LTQxYzQtOTE1MS0zZGI3YTNiMmY1MjEifUIxbncvZVE4Y2Mvc1RadU83bnFuT1pwV21qU1dla2FEK1hFaGdVdG9HNVRLZFkzbHpGOVFrcmFpZ1ZvbzJSOUVVTjU1elNmcEIrK0dQZ2QzYy9lSGZVdDhMNFgrM1UxV2piSUpLb1lubTRUWlo1OFM0Ukc0OW01VVdRRitnMUttVHowRXA1Q2c0QWhBNG1OSHdNM1Fyc0pXcjZFd3p1UXBNTGVTSjUwNWd1VFFGMWdWSkM3eW92TTZjekZEKzE0dFYwWCtxMC9yTHQrQVhxWC9JR1RkcHVxazUzcmcza1B1cWlxU2ZYeUF2MnBJVG5jM1gxWW5YR2krR1g0RW1lb3F0Q2pRdXUvckhBOExuMnp1SGcvV1k1WXYzemdHNTBzT29PWmcrK01abTkxQVk5U1laMmYvb0pybXp6cHVKR3dQckJ5SUU3ZFAwYkNjRVlIdy9sUnJiNmFqbnpUZjRxSkdqalZyenNsa1dBTXZNTkhodWdQQkk1bjNyWWFkaEwwNXFDNUVyODNITy9ScWlqTjN3NUp3MnpaQ2NMcWhJcWthYjlmQnluS25QNWN1L1AwQlVJWjVucXRnOGxWSUFVTHFRWWtKRDJiQjVqVGZlN3doM2h4U25CYTR3TFh0Slk4K1VLT1A4WCt4S3gwcGo3Q2hNNENRNXI3U0haOFYwNzk2.MQ==; _ntsid=0a8b55a6-8cd8-41c4-9151-3db7a3b2f521; _dd_s=logs=1&id=6c4c3401-12a9-436f-90d8-070cc0854a5c&created=1738241178468&expire=1738244255956";
        String CreateTripPayload = "{\n" +
                "  \"stage\": \"trip-created\",\n" +
                "  \"stops\": [\"TEST-A1\", \"TEST-A2\", \"TEST-A3\"],\n" +
                "  \"vendor\": \"noon\",\n" +
                "  \"vehicle_type\": \"canter_4tn\"\n" +
                "}";
        Response response = given()
                .contentType(ContentType.JSON)
                .cookie(cookieName, cookieValue)
                .body(CreateTripPayload)
                .when()
                .post("/staging/test-data/trip");

        System.out.println("Raw Response Body: " + response.getBody().asString());
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Content-Type: " + response.getContentType());

        JsonPath jsonPath = response.jsonPath();
        String tripCode = jsonPath.getString("code");
        System.out.println("Extracted Trip Code: " + tripCode);


    }
    @AfterTest
    public void tearDown(){
        if (driver != null) {
            driver.quit();
            System.out.println("Driver quit in hooks: " + driver);
            driver = null; // Reset driver to null after quitting
        }
    }
}
