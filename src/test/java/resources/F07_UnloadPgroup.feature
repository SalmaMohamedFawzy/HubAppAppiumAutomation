@test
Feature: Unload Pgroups

  Scenario: Unload a pgroup via API
    Given the necessary unload details are available
    When I send an unload request for the pgroup
    Then I should receive a successful unload response

  Scenario: Verify the Unload status via the UI
    Given I navigate to the entity details page
    When I refresh the trip details using the trip code
    Then I should see that the trip is unloaded on the UI

  Scenario: Load a Pgroup again via API to complete the flow
    Given a Pgroup exists
    When I load the Pgroup using the API
    Then the API response should be successful
    And I ensure that the Pgroup is loaded on the UI