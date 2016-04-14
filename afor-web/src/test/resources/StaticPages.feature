Feature: Interact with static web pages

  As an automated tester
  I want to be able to interact with static web pages
  So that I can automate application user interfaces

  Scenario: Retrieve a static page
    Given I have a mock service running
    When I open the "/plainHtml" page
    Then I the page should have the text "Example with plain text"

  Scenario: Retrieve a page with an http parameter
    Given I have a mock service running
    When I open the "/plainHtmlWithParameter/?parameter=Testing 1 2 3" page
    Then I the page should have the text "Testing 1 2 3"