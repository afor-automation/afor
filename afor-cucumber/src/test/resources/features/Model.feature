Feature: Access the data model

  Scenario: Access the Customers data model
    When I add a new item to the customers data model
    Then the customers data model should have 1 entry
    When I get from the customers data model
    Then the data should match the item added

  Scenario: Access the Customers data model
    When I add a 5 new items to the customers data model
    Then the customers data model should have 5 entries
    When I get from the customers data model
    Then the data should match the item added