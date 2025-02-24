@test
Feature: Login to Hub App

  Scenario: User logs into the Hub App successfully
    Given the Hub App is open
    When I scan the first QR code
    And I allow camera permission
    And I manually scan the login QR code
    And I scroll down multiple times
    And I click on the login link
    Then I should see the Select Hub page
