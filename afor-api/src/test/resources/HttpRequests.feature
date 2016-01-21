Feature: Interact with basic APIs

  As an automated tester
  I want to be able to interact with static web pages
  So that I can automate application user interfaces

  Scenario: Retrieve a static page
    Given I have a mock service running
    When I send a GET request to the "/plainText" endpoint
    Then I should get an HTTP 200 response
    And the response should have the headers
      | Content-Type | text/plain |

  Scenario: Retrieve a page with an http parameter
    Given I have a mock service running
    When I send a POST request to the "/plainText" endpoint
    Then I should get an HTTP 200 response
    And the response should have the headers
      | Content-Type | text/plain |

  Scenario: Set the headers on a GET request
    Given I have a mock service running
    When I send a GET request to the "/plainText" endpoint with the headers
      | Accept | application/json |
    Then I should get an HTTP 200 response
    And the response should have the headers
      | Content-Type | application/json |

  Scenario: Set the headers on a POST request
    Given I have a mock service running
    When I send a POST request to the "/plainText" endpoint with the headers
      | Accept | application/json |
    Then I should get an HTTP 200 response
    And the response should have the headers
      | Content-Type | application/json |