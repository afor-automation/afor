Feature: Interact with soap APIs

  As an automated tester
  I want to be able to interact with soap services
  So that I can automate application user interfaces

  Scenario: Retrieve data from a SOAP service
    Given I have a mock service running
    When I send a getMockRequest request to the ws endpoint
    Then I should receive a valid SOAP response
