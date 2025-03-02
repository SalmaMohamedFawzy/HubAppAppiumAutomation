@test
Feature: Seal Trip

  Scenario: Seal a trip using the API
    Given the necessary trip sealing details are available
    When I send a seal action request to the hub
    Then I should receive a successful seal action response

  Scenario: Verify the sealed status via the UI
    Given I navigate to the entity details page
    When I refresh the trip details using the trip code
    Then I should see the trip as sealed on the UI

  Scenario: Logout, Login, and Capture Photo
    Given I am logged into the current hub
    When I logout and login to the TEST-A2 hub
    And I enter the middle mile trip code and submit
    Then I capture and upload the arrival photos successfully