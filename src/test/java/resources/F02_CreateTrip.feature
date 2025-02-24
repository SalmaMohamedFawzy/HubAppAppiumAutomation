@test
Feature: Create a Trip using API

  Scenario: Create a trip successfully
    Given I have a valid API cookie
    When I send a request to create a trip with stops "TEST-A1", "TEST-A2", "TEST-A3"
    Then I should receive a response with a trip code
