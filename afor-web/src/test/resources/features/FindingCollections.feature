Feature: Find collection elements on a page

  As an automated tester
  I want to be able to interact with forms
  So that I can automate application user interfaces

  Scenario: Retrieve a field by css collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "input#username"
    Then the css filter should have one element with the class "simplediv"

  Scenario: Retrieve a field by css regex collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "span" and regex "[sp]{2}an text"
    Then the css filter should have one element with the class "textdiv"

  Scenario: Retrieve a field by css text collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "span" and text "span text"
    Then the css filter should have one element with the class "textdiv"

  Scenario: Retrieve a field by css text collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "span" and text case sensitive "span text"
    Then the css filter should have one element with the class "textdiv"

  Scenario: Retrieve a field by css text collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "span" and exact text "span text"
    Then the css filter should have one element with the class "textdiv"

  Scenario: Retrieve a field by css text collection filtering
    Given I have a mock service running
    When I open the "/plainHtml" page
    And I find a collection of div elements
    When I filter the div elements by the css "span" and exact text case sensitive "span text"
    Then the css filter should have one element with the class "textdiv"
