Feature: Find elements on a page using AI

  As an automated tester
  I want to be able to interact with forms using AI
  So that I can automate application user interfaces

  Scenario: Retrieve a field by text reference using AI
    Given I have an android device running
    Then the AI query "Find the Email field" should find the field with the attribute "resource-id" value "nz.co.afor.mock.androidmock:id/email"

  Scenario: Retrieve a button using AI
    Given I have an android device running
    Then the AI query "Find the Sign In button" should find the field with the attribute "resource-id" value "nz.co.afor.mock.androidmock:id/email_sign_in_button"