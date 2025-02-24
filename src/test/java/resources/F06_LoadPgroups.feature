@test
Feature: Load Pgroups and verify loading on UI

  Scenario: Load a Pgroup via API and verify it on the UI
    Given a Pgroup exists
    When I load the Pgroup using the API
    Then the API response should be successful
    And I ensure that the Pgroup is loaded on the UI
