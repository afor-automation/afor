Feature: Artificial Intelligence

  Scenario: Run basic AI features
    When I perform an AI request
    Then the AI response should be successful

  Scenario: Generate email and phone fields for an object
    When I run the AI query "generate realistic email addresses and phone numbers, do not generate addresses"
    Then the AI response should contain populated email addresses and phone numbers
    And the AI response should not contain any addresses