Feature: Search on google

  As a customer
  I want to be able to search on google
  So that I can find information in areas of interest

  Scenario: Search for an example piece of text
    Given I am on the home page
    When I search for "afor automation"
    Then I should see some search results