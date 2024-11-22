Feature: Find elements on a page using AI

  As an automated tester
  I want to be able to interact with forms using AI
  So that I can automate application user interfaces

  Scenario: Retrieve a field by text reference using AI
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the text "Example with plain text"
    Then the AI query "Find the input field associated with the label 'Label ref by association'" should find the field with the attribute "id" value "labelRefByAssociation"

  Scenario: Retrieve a button using AI
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the text "Example with plain text"
    Then the AI query "Find the Login button" should find the field with the attribute "value" value "Login"

  Scenario: Retrieve a button using AI under a parent item
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the text "Example with plain text"
    Then the AI query "Find the Login button" under the parent AI query "Find the form" should find the field with the attribute "value" value "Login"
