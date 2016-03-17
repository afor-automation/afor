Feature: Report Feature 1

  As an automated tester
  I want to be able to be able to generate automated reports
  So that I can visualise the results

  Scenario: Simple scenario
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "Pass" result

  Scenario: Simple scenario 2
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "Pass" result

  Scenario Outline: Scenario Outline <dataItem>
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "<dataItem>" result
    Examples:
      | dataItem       |
      | passing test 1 |
      | passing test 2 |

  Scenario: 2 second scenario
    Given I have a 1 second sleep

  Scenario: 2 second scenario
    Given I have a 1 second sleep

  Scenario: 2 second scenario
    Given I have a 1 second sleep