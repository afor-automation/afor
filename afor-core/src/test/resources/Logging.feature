Feature: Spring dependencies

  As an automated tester
  I want to be able to use the spring framework
  So that I can write well structured tests

  @log
  Scenario Outline: Logged logging levels
    Given I have a new log instance
    When I log to <logLevel>
    Then I my information should be logged
    Examples:
      | logLevel |
      | debug    |
      | info     |
      | error    |

  @log
  Scenario: Ignored logging levels
    Given I have a new log instance
    When I log to trace
    Then I my information should not be logged
