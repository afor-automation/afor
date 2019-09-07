Feature: Lists

  As an automated tester
  I want to be able to use helpful list methods
  So that I do not have to write my own list methods

  Scenario: First in a list
    Given I have a new list
    When I get the first list value
    Then the first list value should be returned

  Scenario: Last in a list
    Given I have a new list
    When I get the last list value
    Then the last list value should be returned

  Scenario: Any in a list
    Given I have a new list
    When I get any list value
    Then a list value should be returned