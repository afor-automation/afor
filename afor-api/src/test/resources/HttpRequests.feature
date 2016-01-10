Feature: Interact with basic APIs

  As an automated tester
  I want to be able to interact with static web pages
  So that I can automate application user interfaces

  Scenario: Retrieve a static page
    Given I have a mock service running
    When I send a GET request to the "/plainText" endpoint
    Then I should get an HTTP 200 response

  Scenario: Retrieve a page with an http parameter
    Given I have a mock service running
    When I send a POST request to the "/plainText" endpoint
    Then I should get an HTTP 200 response