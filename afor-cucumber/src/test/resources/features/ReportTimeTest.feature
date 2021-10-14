@feature-tag
Feature: Visually show the report

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Background:
    Given I am in a state

  @scenario-tag
  Scenario: Single scenario
    When I wait for 20 milliseconds
    And I wait for 50 milliseconds
    And I wait for 150 milliseconds
    And I wait for 250 milliseconds
    Then I should receive a "pass" result

  @scenario-outline-tag
  Scenario Outline: Single scenario 2
    When I wait for <milliseconds> milliseconds
    Then I should receive a "pass" result

    @example-tag
    Examples:
      | milliseconds |
      | 10           |
      | 20           |

  Scenario: Single scenario 2
    When I wait for 20 milliseconds
    And I wait for 50 milliseconds
    And I wait for 150 milliseconds
    And I wait for 1250 milliseconds
    Then I should receive a "pass" result