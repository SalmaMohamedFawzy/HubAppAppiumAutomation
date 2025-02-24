package TCs;

import Configurations.ApiCookieManager;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class T04_CreatePgroupsRestAssured {
    public static String pgroupCode;

    @Test
    public void CreatePgroupApi() {
        String cookieName = "Cookie";
        String createPgroupPayload = "{\n" +
                "  \"stage\": \"pgroups-created\",\n" +
                "  \"stops\": [\"TEST-A1\", \"TEST-A2\"]\n" +
                "}";

        Response response = given()
                .header("Accept", "application/json")
                .header("N-Hub", "TEST-A1")
                .header("Content-Type", "application/json")
                .header(cookieName, ApiCookieManager.COOKIE_VALUE)
                .body(createPgroupPayload)
                .when()
                .post("https://sc-express-api-middlemile.noonstg.team/staging/test-data/trip");
        String jsonResponse = response.getBody().asString();
        List<String> extractedValues = JsonPath.read(jsonResponse, "$.pgroups_map.TEST-A1.TEST-A2");
         pgroupCode = extractedValues.get(0);
        System.out.println(pgroupCode);
    }
}
