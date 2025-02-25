@test
Feature: Upload Image Using Signed URL for Unseal
  Scenario: Upload an image file via signed URL for unseal
    Given a signed URL for unseal is available
    When I upload the image file for unseal "black_img.jpg" using the signed URL
    Then the file upload should be successful
