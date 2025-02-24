@test
Feature: Upload Image Using Signed URL

  Scenario: Upload an image file via signed URL
    Given a signed URL is available
    When I upload the image file "black_img.jpg" using the signed URL
    Then the file upload should be successful
