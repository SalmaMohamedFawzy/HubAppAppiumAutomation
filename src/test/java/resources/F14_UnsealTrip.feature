@test
Feature: Unseal Trip

  Scenario: Unseal a trip using the API
    Given the necessary trip unsealing details are available
    When I send an unseal action request to the hub
    Then I should receive a successful unseal action response

  Scenario: Verify the unsealed status via the UI
    Given I navigate to the entity details page
    When I refresh the trip details using the trip code
    Then I should see the trip as unsealed on the UI

