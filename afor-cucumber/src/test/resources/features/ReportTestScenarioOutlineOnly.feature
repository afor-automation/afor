Feature: Visually show the report with scenario outlines only in a feature

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Scenario Outline: Scenario outline for <data> data
    When I perform an action
    Then I should receive a "<result>" result
    And I should receive a "<data>"
    Examples:
      | data     | result |
      | result 1 | pass   |
      | result 2 | pass   |
      | result 3 | pass   |
      | result 3 | pass   |
      | result 4 | assert |