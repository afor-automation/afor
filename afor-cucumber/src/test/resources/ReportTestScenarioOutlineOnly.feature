Feature: Visually show the report with scenario outlines only in a feature

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Scenario Outline: Scenario outline for <data> data
    When I perform an action
    Then I should receive a "<data>"
    Examples:
      | data     |
      | result 1 |
      | result 2 |
      | result 3 |