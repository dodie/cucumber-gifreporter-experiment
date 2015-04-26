Feature: Unicorns

  Scenario: The blog has unicorns
    Given I am on the blog main page
    When I open the menu
    And I click on the Archive link
    Then I should see unicorns
