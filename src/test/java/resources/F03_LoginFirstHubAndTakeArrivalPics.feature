@test
Feature: Login to the Hub and Take Arrival Pictures

  Scenario: Log in to the first hub using trip code and take arrival pictures successfully
    Given I am on the Hub selection screen
    When I select the first hub "TEST-A1"
    And I enter the trip code from the previous test
    Then I should be logged in successfully


  Scenario: Take arrival pictures in the first hub
    Given I am logged into the first hub
    When I take arrival pictures for the first time
    And I enter dock number "2"
    And I capture and upload the photos
    Then the process should be completed successfully
