Feature: Library instantiation values

  As an automated tester
  I want to be able to use libraries with automatically instantiated values
  So that I can use configuration to inject data to match my test criteria

  Scenario: Date format
    Given I have a new JSON parser instance
    And I have JSON which matches the configuration date format
    When I parse the JSON
    Then the JSON date format should match the configuration
