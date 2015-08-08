Feature: Spring dependencies

  As an automated tester
  I want to be able to use the spring framework
  So that I can write well structured tests

  @log
  Scenario Outline: Logging levels
    Given I have a new log instance
    When I log to <logLevel>
    Then I my information should be logged
    Examples:
      | logLevel |
      | debug    |
      | info     |
      | error    |