Feature: Search on Google using AI selectors

  As a customer
  I want to be able to search on Google
  So that I can find information in areas of interest

  Scenario: Search for an example piece of text using AI
    Given I am on the home page
    When I search for "afor automation" using ai
    Then I should see some search results using ai
    And the search results should contain a link with the text "Afor Automation" using ai