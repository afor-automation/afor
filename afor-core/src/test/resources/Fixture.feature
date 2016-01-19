Feature: Fixtures

  As an automated tester
  I want to be able to use fixtures
  So that I can store and retrieve existing test data

  Scenario: Read fixture information
    Given I have a new fixture instance
    When I read the fixture data
    Then I should have fixture data

  Scenario: Read fixture information using the classpath
    Given I have a new fixture instance
    When I read the fixture data by classpath
    Then I should have fixture data
