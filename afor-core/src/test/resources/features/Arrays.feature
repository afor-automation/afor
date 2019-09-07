Feature: Arrays

  As an automated tester
  I want to be able to use helpful array methods
  So that I do not have to write my own array methods

  Scenario: First in a array
    Given I have a new array
    When I get the first array value
    Then the first array value should be returned

  Scenario: Last in a array
    Given I have a new array
    When I get the last array value
    Then the last array value should be returned

  Scenario: Any in a array
    Given I have a new array
    When I get any array value
    Then an array value should be returned