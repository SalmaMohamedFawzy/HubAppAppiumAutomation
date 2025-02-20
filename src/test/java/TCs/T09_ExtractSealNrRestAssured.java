package TCs;

import Hooks.ApiCookieManager;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import com.jayway.jsonpath.JsonPath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T09_ExtractSealNrRestAssured {
    public static String SealNr;
    public String BatchNumber;

    @Test
    public void T1_generateTripSealBatch() {

        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/control/pregen/tripseal-batch";
        String requestBody = "{ \"hub_dst\": \"TEST-A2\", \"qty\": 1 }";

        Response response = given()
                .header("accept", "application/json")
                .header("N-Hub", "TEST-A1")
                .header("Content-Type", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .body(requestBody)
                .when()
                .post(baseUrl + endpoint);

        String jsonResponse = response.getBody().asString();
        String pregenBatchKey = JsonPath.read(jsonResponse, "$.pregen_batch_key");
        BatchNumber = pregenBatchKey.split("/")[1];
        System.out.println("Extracted Batch Number: " + BatchNumber);
    }
    @Test
    public void T2_extractsealnr(){
        String baseUrl = "https://express-api.noonstg.team";
        String endpoint = "/control/pregen/tripseal-batch/"+BatchNumber;

        Response response = given()
                .header("accept", "application/json")
                .header("Cookie", ApiCookieManager.COOKIE_VALUE)
                .when()
                .get(baseUrl + endpoint);

        String jsonResponse = response.getBody().asString();
        Pattern pattern = Pattern.compile("(MMTS::\\S+)");
        Matcher matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            SealNr = matcher.group(1).replaceAll("\"$", "");
        } else {
            SealNr = "Not Found";
        }
        System.out.println("Extracted Trip Seal Number: " + SealNr);

    }

}
