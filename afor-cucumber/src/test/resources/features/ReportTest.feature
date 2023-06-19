Feature: Visually show the report

  As an automated tester
  I want to be able to view reports
  So that I can represent my automation to others

  Background:
    Given I am in a state

  Scenario: Single scenario
    When I perform an action
    And I perform a block action:
      | Attribute        | Value        |
      | An attribute     | A value      |
      | Second attribute | Second value |
    Then I should receive a "result"

  @tag1 @tag2 @tag3 @tag4 @tag5 @tag6 @tag7 @tag8 @tag9 @tag10 @tag11 @tag12 @tag13 @tag14 @tag15 @tag16 @tag17 @tag18 @tag19 @tag20 @tag21 @tag22 @tag23 @tag24 @tag25 @tag26 @tag27 @tag28 @tag29 @tag30 @tag31 @tag32 @tag33 @tag34 @tag35 @tag36 @tag37 @tag38 @tag39 @tag40
  Scenario Outline: Scenario outline for <data> data
    When I perform an action
    And I perform another action
    And I perform a block action:
      | Attribute        | Value        |
      | An attribute     | A value      |
      | Second attribute | Second value |
    And I perform a block action:
      | Attribute        | Value        |
      | An attribute     | A value      |
      | Second attribute | Second value |
    Then I should receive a "<data>"
    Examples:
      | data   |
      | result |
      | result |
