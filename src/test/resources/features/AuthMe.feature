Feature: Auth Me API

  Background:
  	Given the login API endpoint is "/api/auth/login"
    And the request header "Accept" is set to "application/json"
    And the request header "Content-Type" is set to "application/json"
    And the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    And the response should contain a "user" object
    And the response user login email should be "student@example.com" 
    
    
  @positive @smoke @AuthMe01
  Scenario: Successfully retrieve authenticated user
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the response content type should be "application/json"
    And the response field success should be "true"
    And the response should contain user
    
  @positive @AuthMe02
  Scenario: Verify authenticated user ID
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the login user id should be same as auth me user id
    
  @positive @AuthMe03
  Scenario:  Verify authenticated user email
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the login user email should be same as auth me user email
    
  @positive  @AuthMe04
  Scenario: Verify issued-at timestamp
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the response field user.iat should be present
    And the response field user.iat should be a number
    
    
  @positive @AuthMe05
  Scenario: Verify expiration timestamp
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the response field "user.exp" should be present
    And the response field "user.exp" should be a number
      
  @positive @AuthMe06
  Scenario: Verify token issued-at time is before expiration time
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the "user.iat" value should be less than the "user.exp" value
    
  @positive @AuthMe07
  Scenario: Retrieve authenticated user with JSON Accept header
    Given the request contains the header "Accept" with value "application/json"
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    And the response content type should contain "application/json"
    
    
  @positive @regression  @AuthMe08
  Scenario: Successfully retrieve authenticated user multiple times
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    When I send a "GET" request to "/api/auth/me"
    Then the response status code should be 200
    
    
  @positive @contract @AuthMe09
  Scenario: Validate successful response schema
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 200
    And the response should contain field "success"
    And the response should contain field "user"
    And the response should contain field "user.userId"
    And the response should contain field "user.email"
    And the response should contain field "user.iat"
    And the response should contain field "user.exp"
    
    
@positive @contract @AuthMe10
  Scenario: Validate authenticated user email
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 200
    And "user.email" should contain "@"    
    
    
    
    
    
    
    
    
    
    
    
    