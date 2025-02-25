@test
Feature: Signed URLs Creation for Unseal
  Scenario: Create a signed URL for Unseal process
    Given a valid entity key and a valid cookie
    When I send a request to create a signed URL for "trip-unsealed.jpg"
    Then I should receive a valid signed URL for "trip-unsealed.jpg" in the response



