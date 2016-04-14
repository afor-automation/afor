Feature: Perform CRUD operations on a RESTful API

  As a customer
  I want to be call REST service operations
  So that I can create, read, update and delete my data

  Scenario: Get a single Post from a REST service
    When I call the GET Posts service with the post id 1
    Then the GET Post response code should be 200
    And the response should have the id 1

  Scenario: Get a list of Posts from a REST service
    When I call the GET Posts service
    Then the GET Posts response code should be 200
    And the response should have a lists of Posts

  Scenario: Create a new Post using the REST service
    Given I have a new Post
    When I call the Create Post service with my Post
    Then the Create Post response code should be 201
