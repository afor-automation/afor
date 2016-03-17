@featuretag1 @featuretag2
Feature: Report Feature 2

  As an automated tester
  I want to be able to be able to generate automated reports
  So that I can visualise the results

  # Comment 1
  # Comment 2

  Scenario: Simple scenario
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "Pass" result

  # Comment 3
  Scenario: Simple scenario 2
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "Pass" result

  @tag1 @tag2
  Scenario Outline: Scenario Outline <dataItem>
    Given I have a passing step
    When I continue to have a passing step
    Then I should see a report with a "<dataItem>" result
    Examples:
      | dataItem       |
      | passing test 3 |
      | passing test 4 |

  Scenario: Undefined
    Given I have an undefined step
    When I continue to have a passing step
    Then I should see a report with a "Pass" result

  Scenario: Failed
    Given I have a passing step
    When I continue to have a failing step
    Then I should see a report with a "Pass" result

  Scenario: Table scenario1
    Given I have a passing step
    When I continue to have a passing step with a table:
      | table item 1 | table item 2 |
    Then I should see a report with a "Pass" result

  Scenario: Table scenario2
    Given I have a passing step
    When I continue to have a passing step with a table:
      | row1         | row2         |
      | table item 3 | table item 4 |
    Then I should see a report with a "Pass" result