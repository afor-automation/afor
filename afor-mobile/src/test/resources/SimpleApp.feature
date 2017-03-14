Feature: Interact with sample app

  As an automated tester
  I want to be able to interact with android devices
  So that I can automate android user interfaces

  Scenario: Fill in a field
    Given I have an android device running
    When I fill in a field
    Then the field should be filled in