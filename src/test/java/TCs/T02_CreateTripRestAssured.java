package TCs;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class T02_CreateTripRestAssured {
    @Test
    public void CreateTripApi() {
        String cookieName = "Cookie";
        //should be changed every day
        String cookieValue = "_ntrtnetid=nav1.public.eyJyb2xlcyI6WyJsbXMtbWlkZGxlbWlsZS10cmlwLWVkaXRvciIsImxtcy1taWRkbGVtaWxlLWFkbWluIiwibG1zLW1pZGRsZW1pbGUtc3VwZXJ2aXNvciIsImRldmVsb3BlciIsInRlY2gtZWd5cHQiLCJsbXMtYWRtaW4iLCJsbXMtbWlkZGxlbWlsZS1yb3V0ZS1lZGl0b3IiLCJ0ZWNoLWVneXB0LXFhIiwibG1zLWxhc3RtaWxlLWFkbWluIiwiZmluYW5jZS1yZWFkb25seSJdLCJlbWFpbCI6InNuYWd5QG5vb24uY29tIiwiaG9zdHMiOltdLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSzJtdUp3RWRpaEhfdHFELXFEMVQxb01Pckk4RE1Tc0JBY0lHc193blFxNmFwak5LOU09czk2LWMiLCJpYXQiOjE3MzgzMzkwMzgsInR5cGUiOiJkb21haW4iLCJ0aWQiOiIyZDQ1ZjQzNi02ZTdlLTQ0ZWMtYjc3My00MGFjNzRkZjI1N2UiLCJzaWQiOiIyNmIzOWFlNS1jZThjLTRhYWYtODM0NS0zYjdlZWQ5MjRlOWIifUxsWUJWUkNUVlJZeVJYN1NlanBDa1VISHNzbW5MWXZnQWs1dkt1Z2FZMDhnYXhoT2FHMFNyRmh4QmxXOGxZY1RGZ2F1Y1FTM053MkNQb0FvN1I1aU9LUmhqQ2cxWEpwb1YrQXkwYUtYWmpsMGZPYWhCc0pxT2grcGlCVEdtY0p3Uno0NG81eld4MTJobm9LcDZxTGJwOUd3QXB1enlCNDJyRnNRUENyN0tDdzlvMGpFbzVTckJCL0RKZ0xrOTN3S1BoVlcxclVWMnN1Mk1oN0hiT1N4RnhIOWE1TjZ3aE1TWXBHeVRESkpWL0l0RmJUakcxaHVvd0Zia2RabXpuTXlpL1ZKSm42TTFOdk5JU2RsM3dFbFdRbHM0ZnQ5Ujkybzl0Z2tJZVhSVFRKSm41UU5IRjRTdlJPOGtMcGtvQ2hLQ1pqV2V6TDZTblg5Ylltbk5VWGhqZDFacHFKdmswbEIvTzlnYlgzSTZ3RnZncFNIcGVOcUZSWWJDYTNOUzlpcXFheXhEZlNhLy9vTFV3OUxsckhlTkhVSmpOSnQ1VFpxaUd5QVFKZFBmbFBWRVRmL2pzSVJqU0JyVENxRkZvVy9raE8yU2JHZm1hUlBiRFFZTm1uTUF4RDhKSVA4ditQVGRNMFBRL2IwYjltVGkvbC9BR3d3RGtGSHp4Zm12RWZ6.MQ==; _ntsid=26b39ae5-ce8c-4aaf-8345-3b7eed924e9b; _dd_s=logs=1&id=d898eda1-e30c-43f3-857e-0fd335861759&created=1738343140513&expire=1738344040513\n";
        String CreateTripPayload = "{\n" +
                "  \"stage\": \"trip-created\",\n" +
                "  \"stops\": [\"TEST-A1\", \"TEST-A2\", \"TEST-A3\"],\n" +
                "  \"vendor\": \"noon\",\n" +
                "  \"vehicle_type\": \"canter_4tn\"\n" +
                "}";
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header(cookieName, cookieValue)
                .body(CreateTripPayload)
                .when()
                .post("https://sc-express-api-middlemile.noonstg.team/staging/test-data/trip");

        JsonPath jsonPath = response.jsonPath();
        String tripCode = jsonPath.getString("code");
        System.out.println("Extracted Trip Code: " + tripCode);


    }

}
