Feature: Manage Customer Account and Electricity Plan

  As a Flick customer,
  I want to manage my account, view my latest bill, check my current electricity plan,
  and automatically switch plans based on the day of the week
  so that I can benefit from the most cost-effective pricing.

  Background:
    Given I am a registered Flick customer
    And I am logged into my Flick account

  Scenario: View account details
    When I navigate to the "Account" section
    Then I should see my account information including current electricity plan
    And I should be able to update my personal details if needed

  Scenario: View the latest electricity bill
    When I navigate to the "Billing" section
    Then I should see the latest bill details including the amount owing and credit

  Scenario: Ensure Flat Rates plan is set on weekdays
    Given today is a weekday (Monday, Tuesday, Wednesday, Thursday, or Friday)
    Then my current plan should be Flat Rates

#  Scenario Outline: Verify plan changes based on the day of the week
#    Given today is <day>
#    And my current plan is <current_plan>
#    When I request to change my plan to <requested_plan>
#    And the system processes the change at midnight
#    Then my plan should update to <requested_plan> at midnight tonight
#    Examples:
#      | day      | current_plan | requested_plan |
#      | Friday   | Flat         | Peak           |
#      | Sunday   | Peak         | Flat           |