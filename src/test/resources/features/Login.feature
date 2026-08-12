Feature: Login API

  Background:
    Given the login API endpoint is "/auth/login"
    And the request header "Accept" is set to "application/json"
    And the request header "Content-Type" is set to "application/json"
    
    
  @positive @smoke @login01
  Scenario: Successfully login with valid email and password
    Given the login request contains:
      | email    | student@example.com |
      | password | secret123           |
    When the user sends a POST request to the login endpoint
    Then the response status code should be 200
    And the response "success" should be true
    And the response should contain a non-empty "token"
    And the response should contain a "user" object
    And the response user email should be "student@example.com"    