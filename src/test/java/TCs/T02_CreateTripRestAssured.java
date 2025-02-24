package TCs;

import Configurations.ApiCookieManager;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class T02_CreateTripRestAssured {
    public static String tripCode;

    @Test
    public void CreateTripApi() {
        String cookieName = "Cookie";
        //should be changed every day
                String CreateTripPayload = "{\n" +
                "  \"stage\": \"trip-created\",\n" +
                "  \"stops\": [\"TEST-A1\", \"TEST-A2\", \"TEST-A3\"],\n" +
                "  \"vendor\": \"noon\",\n" +
                "  \"vehicle_type\": \"canter_4tn\"\n" +
                "}";
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(CreateTripPayload)
                .when()
                .post("https://sc-express-api-middlemile.noonstg.team/staging/test-data/trip");

        JsonPath jsonPath = response.jsonPath();
        tripCode = jsonPath.getString("code");
        System.out.println("Extracted Trip Code: " + tripCode);

    }
}