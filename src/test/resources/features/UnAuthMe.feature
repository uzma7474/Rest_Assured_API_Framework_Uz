Feature: UnAuth Me API


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

    
      
  ###################################################################################################
  # Negative Test cases
  ###################################################################################################
  
  @negative @security @smoke @AuthMe12
  Scenario: Get authenticated user without Authorization header
    Given the request does not contain an Authorization header
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401 
    And the auth response error message should be "Unauthorized" 
    
  @negative @security @AuthMe13
  Scenario: Get authenticated user with empty Authorization header
    Given the request contains the header "Authorization" with an empty value
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Unauthorized"
 
    
  @negative @security @AuthMe14
  Scenario: Get authenticated user without Bearer keyword
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    And the request contains an Authorization header containing only a valid token
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Unauthorized"    
    
    
  @negative @security @AuthMe15
  Scenario: Get authenticated user with empty Bearer token
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    And the request contains the header authorization "Authorization" with value "Bearer"
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Unauthorized"
        
    
  @negative @security @AuthMe16
  Scenario: Get authenticated user with invalid token
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    Given the request contains the Authorization header "Bearer invalid-token"
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Invalid or expired token"
        
    
  @negative @security @AuthMe17
  Scenario: Get authenticated user with random JWT
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    Given the request contains a syntactically invalid JWT
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Invalid or expired token"
    
    
  @negative @security @AuthMe18
  Scenario: Get authenticated user using Basic authentication
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    Given the request contains Basic authentication instead of Bearer authentication
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Unauthorized"
    
      
  @negative @security @AuthMe19
  Scenario: Get authenticated user with malformed Authorization header
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    Given the Authorization header contains extra "Bearer <token> extra-data"
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response error message should be "Invalid or expired token"
       
    
  @negative @security @AuthMe20
  Scenario Outline: Reject invalid Authorization header
     Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And store the token
    Given the Authorization header contains "<authorization>"
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the auth response errors "<error_msg>"
    
    Examples:
      | authorization               | error_msg    |
      | invalid-token               | Unauthorized |

      
  @negative @contract @AuthMe21
  Scenario: Validate unauthorized response
    Given the request does not contain an Authorization header
    When I send a GET request to "/api/auth/me"
    Then the response status code should be 401
    And the response content type should contain "application/json"
    And the response success should be "false"
    And the auth response error message should be "Unauthorized"   
    
    
    
    
    
    
    
    
    
    
    
    
    
    