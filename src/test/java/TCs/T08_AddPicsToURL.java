package TCs;

import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static TCs.T07_SignedUrlsCreationRestAssured.SealedImgsURL;

public class T08_AddPicsToURL {
    @Test
    public void testUploadFile() {

        File file = new File("C:\\Users\\snagy\\Downloads\\black_img.jpg");
        String contentType = "image/jpeg";
        boolean result = uploadFile(SealedImgsURL, file, contentType);
        Assert.assertTrue(result, "File upload should be successful");
    }

    public boolean uploadFile(String signedUrl, File fileToUpload, String contentType) {
        HttpURLConnection connection = null;
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            // Check if file exists
            if (!fileToUpload.exists() || !fileToUpload.isFile()) {
                System.out.println("Error: File does not exist or is not a valid file.");
                return false;
            }

            // Create connection
            URL url = new URL(signedUrl);
            connection = (HttpURLConnection) url.openConnection();

            // Set up the request
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", contentType);

            // Open file input stream
            fileInputStream = new FileInputStream(fileToUpload);
            byte[] buffer = new byte[8192];
            int bytesRead;

            // Write file data to connection output stream
            outputStream = connection.getOutputStream();
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            // Get response code
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + responseMessage);

            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            System.err.println("File upload failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (outputStream != null) outputStream.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

}
