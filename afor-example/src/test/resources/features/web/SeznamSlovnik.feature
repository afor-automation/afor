Feature: Slovnik Seznam.cz

  As an internet user
  I want to translate an expression from Czech to English language
  So I can understand content of pages written in English

  Scenario Outline: Translate Czech <word> to English
    Given I am on the slovnik landing page
    When I type "<word>" for translation
    Then I should obtain a list of possible English words with at least <number> elements in the result
    Examples:
      | word | number |
      | slovnik | 4   |
      | preklad | 3   |

