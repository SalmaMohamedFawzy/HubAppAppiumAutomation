package TCs;
import static io.restassured.RestAssured.given;

import Configurations.ApiCookieManager;
import Configurations.BuildNumberManager;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class T05_ExtractEntityKeyRestAssured {
    public static String EntityKey;

    @Test
    public void getentityid() {
        String cookieName = "Cookie";
        String url = "https://express-api.noonstg.team/hub/scan/entity";
        String scanPayload = "{ \"scan\": \"MMTR::" + T02_CreateTripRestAssured.tripCode + "\" }";
        //String scanPayload = "{ \"scan\": \"MMTR::TEST-A1-R-130-T-144\" }";
        Response response = given()
                .header("accept", "application/json")
                .header("X-App-Build", BuildNumberManager.APP_BUILD_NUMBER)
                .header("X-Facility-Type", "hub")
                .header("X-Facility", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(scanPayload)
                .when()
                .post(url);
        String jsonResponse = response.getBody().asString();
        EntityKey = JsonPath.read(jsonResponse, "$.entity_key");
        System.out.println(EntityKey);
    }
}
