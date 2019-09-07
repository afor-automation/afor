Feature: Visually show the report with no background

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Scenario: Single scenario no background
    When I perform an action
    Then I should receive a "result"

  Scenario Outline: Scenario outline for <data> data no background
    When I perform an action
    And I perform another action
    Then I should receive a "<data>"
    Examples:
      | data                   |
      | result no background 1 |
      | result no background 2 |

