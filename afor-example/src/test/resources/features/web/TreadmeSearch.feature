Feature: Ability te search for a product on Tradme

  As an online shopper, I want to search for a product,
  being able to limit the result by some filtering criteria
  and buying the product

  Scenario: Search for a product and filter and add to card
    When I open Tradme website
    Then I should see a search field
    When I type a product name
    Then I should see a list of products with filtering criteria
