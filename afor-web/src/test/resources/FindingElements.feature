Feature: Find elements on a page

  As an automated tester
  I want to be able to interact with forms
  So that I can automate application user interfaces

  Scenario: Retrieve a field by text reference
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the label with text "by id" and field id "labelRefById" by reference

  Scenario: Retrieve a field by exact text reference
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the label with exact text "Label ref by id" and field id "labelRefById" by reference

  Scenario: Retrieve a field by text association
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the label with text "Label ref by association" and field id "labelRefByAssociation" by association

  Scenario: Retrieve a field by exact text association
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then the page should have the label with exact text "Exact label ref by association" and field id "exactLabelRefByAssociation" by association
