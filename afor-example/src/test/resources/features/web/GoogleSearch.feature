Feature: Search on Google

  As a customer
  I want to be able to search on Google
  So that I can find information in areas of interest

  Scenario: Search for an example piece of text
    Given I am on the home page
    When I search for "afor automation"
    Then I should see some search results
    And the search results should contain a link with the text "A for Automation"