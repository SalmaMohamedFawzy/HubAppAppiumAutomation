package TCs;

import Hooks.ApiCookieManager;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class T07_SignedUrlsCreationRestAssured {

    public static String SealedImgsURL;
    @Test
    public void SignedUrlsCreation(){
        String cookieName = "Cookie";
        String Payload = "{ \"sign\": { \"" + T05_ExtractEntityKeyRestAssured.EntityKey + "\": { \"trip-sealed.jpg\": 1 } } }";

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
        SealedImgsURL = JsonPath.read(jsonResponse,  "$.signed['" + T05_ExtractEntityKeyRestAssured.EntityKey + "']['trip-sealed.jpg'][0]");
        System.out.println("url is : " +SealedImgsURL);

    }
}
