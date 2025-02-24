@test
Feature: Extract Entity Key using API

  Scenario: Extract the entity key successfully
    Given I have a valid API cookie and app build number
    When I send a request to extract the entity key using the trip code
    Then I should receive a response containing the entity key
