package Steps;

import java.io.File;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import static UploadPicsToURL.UploadPicsToURL.uploadFile;
public class T09_AddPicsToURL {
    private boolean uploadResult;

    @Given("a signed URL is available")
    public void a_signed_url_is_available() {
        Assert.assertNotNull(T08_SignedUrlsCreationRestAssured.SealedImgsURL, "Signed URL should not be null");
        System.out.println("Signed URL for seal: " + T08_SignedUrlsCreationRestAssured.SealedImgsURL);
    }

    @When("I upload the image file {string} using the signed URL")
    public void i_upload_the_image_file_using_the_signed_url(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String contentType = "image/jpeg";
        uploadResult = uploadFile(Steps.T08_SignedUrlsCreationRestAssured.SealedImgsURL, file, contentType);
    }
    @When("I upload the image file for unseal {string} using the signed URL")
    public void i_upload_the_image_file_using_the_signed_url_unseal(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String contentType = "image/jpeg";
        uploadResult = uploadFile(Steps.T08_SignedUrlsCreationRestAssured.UnSealedImgsURL, file, contentType);
    }

    @Then("the file upload should be successful")
    public void the_file_upload_should_be_successful() {
        Assert.assertTrue(uploadResult, "File upload should be successful");
    }
}
