Feature: Login API

  Background:
    Given the login API endpoint is "/api/auth/login"
    And the request header "Accept" is set to "application/json"
    And the request header "Content-Type" is set to "application/json"
    
    
  @positive @smoke @login01
  Scenario: Successfully login with valid email and password
    Given the login request contains:
      | email               | password   |
      |  student@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And the response should contain a "user" object
    And the response user login email should be "student@example.com"    
    

  @positive @smoke @login02
  Scenario: Successfully login with valid email and password
    Given the login request contains:
      | email                  | password     |
      |  student@example.com   | secret123    |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And the response should contain a "user" object
    And the response user login email should be "student@example.com" 
	And the response contain accept is "application/json"

  @positive @smoke @login03
  Scenario: Successfully login with valid email and password
    Given the login request contains:
      | email                  | password     |
      |  student@example.com   | secret123    |
    When the user sends a "POST" request 
    Then the login response status code should be 200
    And the response success should be "true"
    And the login response should contain a non-empty token
    And the response should contain a "user" object
    And the response user login email should be "student@example.com" 
    And the response user login user id 4
	And the response contain accept is "application/json"




###########################################################################################
# Negative Test cases
#############################################################################################

    
  @negative @smoke @login04
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email               | password   |
      | student@example.com | s123       |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"  
    
  @negative @smoke @login05
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email                    | password   |
      |  non_existing@example.com| secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Invalid email or password"     
    And the details object in response is null or empty
    
  @negative @smoke @login06
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email              | password   |
      | wrong@example.com  | wrongpwd   |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Invalid email or password"     
    And the details object in response is null or empty
       
   @negative @smoke @login07
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email              | password   |
      |                    | wrongpwd   |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"     
    And the details object "email" message should be "A valid email is required"
    
    
   @negative @smoke @login08
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email                | password   |
      | student@example.com  |            |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"     
    And the details object "password" message should be "Password must be at least 6 characters"
    
   @negative @smoke @login09
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email          | password   |
      |                |            |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"  
    And the response field "email" should be "A valid email is required"         
    And the response field "password" should be "Password must be at least 6 characters"
    

   @negative @smoke @login10
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email          | password   |
      | null           | secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"  
    And the response field "email" should be "A valid email is required"         
    
   @negative @smoke @login11
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email                | password   |
      |  student@example.com |  null      |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Validation failed"          
    And the response field "password" should be "Password must be at least 6 characters"
            
   @negative @smoke @login12
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email            | password   |
      | test@example.com | secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Invalid email or password" 
    And the details object in response is null or empty        
        
   @negative @smoke @login13
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email               | password   |
      | student@example.com | testabcd  |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Invalid email or password" 
    And the details object in response is null or empty        
        
   @negative @smoke @login14
  Scenario: Login with valid email and invalid password
    Given the login request contains:
      | email                      | password   |
      | completely_new@example.com | secret123  |
    When the user sends a "POST" request 
    Then the login response status code should be 400
    And the response success should be "false"
    And the response field "error" should be "Invalid email or password"
         
        
            
    