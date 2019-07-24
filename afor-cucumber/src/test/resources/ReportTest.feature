Feature: Visually show the report

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Background:
    Given I am in a state

  Scenario: Single scenario
    When I perform an action
    Then I should receive a "result"

  Scenario Outline: Scenario outline for <data> data
    When I perform an action
    And I perform another action
    Then I should receive a "<data>"
    Examples:
      | data   |
      | result |
      | result |
