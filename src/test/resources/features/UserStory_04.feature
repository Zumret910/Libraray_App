Feature: : As a librarian, I want to create a new user using add_user endpoint so that I can add new users to the
  system.

  @regression
 Scenario Outline:Create a valid new user API
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I create a random "<user Type>" as request body
    When I send POST request to "/add_user" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The user has been created."
    And "user_id" field should not be null
    Examples:
      | user Type |
      | librarian |
      | student   |

  @regression
  Scenario: Create a invalid new user API
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I create a random "admin" as request body
    When I send POST request to "/add_user" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "error" path should be equal to "You do not add/edit admins."


   @db @ui
  Scenario Outline: Create a new user ALL LAYERS
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I create a random "<user Type>" as request body
    When I send POST request to "/add_user" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The user has been created."
    And "user_id" field should not be null
    And created user information should match with Database
    And created user should be able to login Library UI
    And created user name should appear in Dashboard Page
    Examples:
      | user Type |
      | librarian |
      | student   |
