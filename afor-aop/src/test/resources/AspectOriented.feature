Feature: Timeout and retries for methods

  As an automated tester
  I want to be able to annotate methods with Timeout and Retries
  So that I can write clean, stable code

  Scenario: Exceeding a timeout
    When I call a method which times out
    Then the method should time out

  Scenario: Retrying a method multiple times
    When I call a method which retries 3 times
    Then the method should retry 3 times