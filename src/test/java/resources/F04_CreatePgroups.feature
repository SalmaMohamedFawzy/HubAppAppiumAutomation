@test
Feature: Create Pgroups using API

  Scenario: Create a Pgroup successfully
    Given I have a valid API cookie
    When I send a request to create a Pgroup with stops "TEST-A1" and "TEST-A2"
    Then I should receive a response containing a Pgroup code
