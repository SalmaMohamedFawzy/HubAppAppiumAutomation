@test
Feature: Trip Seal Extraction

  Scenario: Generate a trip seal batch and extract the seal number
    Given I have generated a trip seal batch with hub "TEST-A2" destination and quantity 1
    When I retrieve the trip seal batch using the batch number
    Then I should extract a valid trip seal number
