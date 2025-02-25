@test
Feature: Signed URLs Creation

  Scenario: Create a signed URL for Seal process
    Given a valid entity key and a valid cookie
    When I send a request to create a signed URL for "trip-sealed.jpg"
    Then I should receive a valid signed URL for "trip-sealed.jpg" in the response
