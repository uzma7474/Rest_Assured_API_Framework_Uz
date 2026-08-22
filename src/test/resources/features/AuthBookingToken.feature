Feature: Create authentication token

  As an API consumer
  I want to authenticate using valid credentials
  So that I can obtain a token for authorized booking operations
  
  Background:
    Given the Restful Booker API is available
    And the auth endpoint is "/auth"
    And the default Content-Type is "application/json"
    

# ===============================================================================================
# POSITIVE TEST CASES
# ===============================================================================================

 @positive @smoke @Booking01
  Scenario: Create token with valid default credentials
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response status code for auth toke API should be 200
    And the response should contain a "token" field
    And the "token" field should not be null
    And the "token" field should not be empty
    And the "token" field should be of type string

    
  @positive @Booking02
  Scenario: Create token with valid Content-Type application/json
    Given the request header "Content-Type" is "application/json"
    And the request body contains valid username and password
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the response should contain a valid token   
    
    
  @positive @Booking03
  Scenario: Create token using valid username and password
    Given the username is "admin"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the response should contain a token

   @positive @Booking04
  Scenario Outline: Create token multiple times using valid credentials
    Given valid username and password are provided
    		| username   | password   | 
      	| <username> | <password> |
    When I send the POST request to the auth endpoint multiple times
    Then the response status code should be 200
    And each successful response should contain a non-empty token
     
	Examples:
	    | username | password    |
	    | admin    | password123 |
    
    
  @positive @Booking05
 Scenario Outline: Verify token can be extracted from successful response
    Given valid username and password are provided
    		| username   | password   | 
      	| <username> | <password> |
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    When I extract the token from the response
    Then the token should be available for subsequent API requests
    
	Examples:
	    | username | password    |
	    | admin    | password123 |

	    
  @positive @Booking06
  Scenario Outline: Verify successful response Content-Type
    Given valid username and password are provided
    		| username   | password   | 
      	| <username> | <password> |
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the response Content-Type should indicate JSON
	    
	Examples:
	    | username | password    |
	    | admin    | password123 |	    
	    
	    
  @positive @Booking07
  Scenario: Verify successful response contains only expected authentication information
     Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the token creation api response should contain the "token" field
    And the token value should be a string	    
	    
	    
# ====================================================================================================
# NEGATIVE - INVALID CREDENTIALS
# ====================================================================================================

  @negative @smoke @Booking08
  Scenario: Create token with invalid username
    Given the username is "invalidUser"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    And the response should contain an authentication error "Bad credentials"
	    
  @negative @smoke @Booking09
  Scenario: Create token with invalid password
    Given the username is "admin"
    And the password is "invalidPassword"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    And the response should contain an authentication error "Bad credentials"	    
	
  @negative @Booking10
  Scenario: Create token with invalid username and invalid password
    Given the username is "invalidUser"
    And the password is "invalidPassword"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    And the response should contain an authentication error "Bad credentials"
    
  @negative @Booking11
  Scenario: Create token with empty username
    Given the username is ""
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking12
  Scenario: Create token with empty password
    Given the username is "admin"
    And the password is ""
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking13
  Scenario: Create token with both username and password empty
    Given the username is ""
    And the password is ""
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
        
    
# ================================================================================================
# NULL VALUES
# ================================================================================================    
    
  @negative @Booking14
  Scenario: Create token with null username
    Given the request body contains:
      """
      {
        "username": null,
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
    
  @negative @Booking15
  Scenario: Create token with null password
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": null
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking16
  Scenario: Create token with null username and null password
    Given the request body contains:
      """
      {
        "username": null,
        "password": null
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
# ==========================================================================================
# MISSING FIELDS
# ==========================================================================================

  @negative @Booking17
  Scenario: Create token without username field
    Given the request body contains:
      """
      {
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking18
  Scenario: Create token without password field
    Given the request body contains:
      """
      {
        "username": "admin"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token

  @negative @Booking19
  Scenario: Create token with empty JSON object
    Given the request body is:
      """
      {}
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking20
  Scenario: Create token without request body
    Given the request does not contain a request body
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token        
    
  # ============================================================
  # EXTRA / UNKNOWN FIELDS
  # ============================================================

  @negative @Booking21
  Scenario: Create token with an additional unknown field
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123",
        "role": "admin"
      }
      """
    When I send a POST request to the auth endpoint
    Then the API should process the request according to its contract
    And the authentication result should not be incorrectly affected by the unknown field
        	
  @negative @Booking22
  Scenario: Create token with multiple additional fields
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123",
        "role": "admin",
        "token": "abc",
        "id": 123
      }
      """
    When I send a POST request to the auth endpoint
    Then the API should process the request according to its contract
	
# ==================================================================================================
# DATA TYPE VALIDATION
# ==================================================================================================

  @negative @Booking23
  Scenario: Create token with numeric username
    Given the request body contains:
      """
      {
        "username": 12345,
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token	    
	    

  @negative @Booking24
  Scenario: Create token with numeric password
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": 12345
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking25
  Scenario: Create token with boolean username
    Given the request body contains:
      """
      {
        "username": true,
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking26
  Scenario: Create token with boolean password
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": true
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @negative @Booking27
  Scenario: Create token with array as username
    Given the request body contains:
      """
      {
        "username": [],
        "password": "password123"
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token        
        	    
@negative @Booking28
  Scenario: Create token with object as password
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": {}
      }
      """
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token	    
	    
  # ============================================================
  # HEADER VALIDATION
  # ============================================================
# Working on
  @negative @Booking29
  Scenario: Create token without Content-Type header
    Given the request does not contain the "Content-Type" header
    And the request body contains valid credentials
    When I send a POST request to the auth endpoint
    Then the API should process the request according to its Content-Type handling rules
    And it should not incorrectly return a malformed successful response	
    
  @negative @Booking30
  Scenario: Create token with unsupported Content-Type
    Given the request header "Content-Type" is "text/plain"
    And the request body contains valid credentials
    When I send a POST for booking request to the auth endpoint
    Then the request should not be processed as a valid JSON authentication request 
    

  @negative @Booking31
  Scenario: Create token with XML Content-Type
    Given the request header "Content-Type" is "application/xml"
    And the request body contains valid credentials
    When I send a POST request to the auth endpoint
    Then the request should not be processed as a valid JSON authentication request

  @negative @Booking32
  Scenario: Create token with malformed Content-Type
    Given the request header "Content-Type" is "invalid/type"
    And the request body contains valid credentials
    When I send a POST request to the auth endpoint
    Then the API should reject or fail to process the request correctly


 # ============================================================
 # HTTP METHOD VALIDATION
 # ============================================================

  @negative @Booking33
  Scenario: Create token using GET method
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123
      }
      """
    When I send a "GET" request to the auth booking endpoint
    Then the response should not be a successful token creation response   
	    
  @negative @Booking34
  Scenario: Create token using PUT method
    Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    When I send a "PUT" request to the auth booking endpoint
    Then the response should not be a successful token creation response
	    
  @negative @Booking35
  Scenario: Create token using PATCH method
     Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    When I send a "PATCH" request to the auth booking endpoint
    Then the response should not be a successful token creation response
	    
  @negative @Booking36
  Scenario: Create token using PATCH method
     Given the request body contains:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    When I send a "DELETE" request to the auth booking endpoint
    Then the response should not be a successful token creation response
    
  @negative @Booking37
  Scenario: Create token with malformed JSON
    Given the request malformed body is:
      """
      {
        "username": "admin",
        "password": "password123"
      """
    When I send a POST for booking request to the auth endpoint
    Then the API should reject the malformed JSON request
		    
 @negative @Booking38
  Scenario: Create token with plain text request body
     Given the request malformed body is:
      """
      username=admin&password=password123
      """
    When I send a POST for booking request to the auth endpoint
    Then the API should not process the request as valid JSON authentication    
	    
  @negative @Booking39
  Scenario: Create token with an empty request body
    Given the request body is empty
    When I send a POST request to the auth endpoint
    Then the response should not contain a valid authentication token


 # ============================================================
 # STRING BOUNDARY / SPECIAL CHARACTERS
 # ============================================================
	    
 @boundary @Booking40
  Scenario: Create token with username containing leading spaces
    Given the username is " admin"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token	    
	    
	    
  @boundary @Booking41
  Scenario: Create token with username containing trailing spaces
    Given the username is "admin "
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token	    
	    
  @boundary @Booking42
  Scenario: Create token with password containing leading spaces
    Given the username is "admin"
    And the password is " password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token
    
  @boundary @Booking43
  Scenario: Create token with password containing trailing spaces
    Given the username is "admin"
    And the password is "password123 "
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token    
    	    
@boundary @Booking44
  Scenario: Create token with username in different case
    Given the username is "ADMIN"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the API should authenticate according to its case-sensitivity rules	    
	    
  @boundary @Booking45
  Scenario: Create token with password in different case
    Given the username is "admin"
    And the password is "PASSWORD123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token

  @boundary @Booking46
  Scenario: Create token with special characters in username
    Given the username is "@dm!n"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token

  @boundary @Booking47
  Scenario: Create token with special characters in password
    Given the username is "admin"
    And the password is "@#$%^&*"
    When I send a POST request to the auth endpoint
    Then the response should not return a successful authentication token	    
	    
  @boundary @Booking48
  Scenario: Create token with extremely long username
    Given the username contains more than 1000 characters
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the API should handle the request without crashing
    And the response should not contain an invalid token

  @boundary @Booking49
  Scenario: Create token with extremely long password
    Given the username is "admin"
    And the password contains more than 1000 characters
    When I send a POST request to the auth endpoint
    Then the API should handle the request without crashing
    And the response should not contain an invalid token	    
	    
	    
 # ============================================================
 # SECURITY TESTS
 # ============================================================

  @security @Booking50
  Scenario: Create token with SQL injection in username
    Given the username is "' OR '1'='1"
    And the password is "password123"
    When I send a POST request to the auth endpoint
    Then the response should not return a valid authentication token
	    
 @security @Booking51
  Scenario: Create token with SQL injection in password
    Given the username is "admin"
    And the password is "' OR '1'='1"
    When I send a POST request to the auth endpoint
    Then the response should not return a valid authentication token

@security @Booking52
  Scenario: Create token with script injection in username
    Given the username is "<script>alert('xss')</script>"
    And the password is "password123"
    When I send a POST request to the auth endpoint
	Then the response should not return a valid authentication token

  @security @Booking53
  Scenario: Create token with script injection in password
    Given the username is "admin"
    And the password is "<script>alert('xss')</script>"
    When I send a POST request to the auth endpoint
    Then the API should safely process the input
    And the response should not contain executable script content	 
    
  @security @Booking54
  Scenario: Verify authentication failure does not expose the password
    Given invalid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response should not expose the submitted password
    
    
 @security @Booking55
  Scenario: Verify authentication failure does not expose sensitive credentials
    Given invalid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response should not expose the username and password unnecessarily   
    
 # ============================================================
 # RESPONSE CONTRACT VALIDATION
 # ============================================================

  @contract @Booking56
  Scenario: Verify token response field name
    Given valid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the response should contain exactly the expected token field name "token"  
    
  @contract @Booking57
  Scenario: Verify token is a string
    Given valid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the token should be of type string
    
  @contract @Booking58
  Scenario: Verify token is not null
    Given valid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the token should not be null

  @contract @Booking59
  Scenario: Verify token is not empty
    Given valid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the token should not be empty
    
  @contract @Booking60
  Scenario: Verify response body is valid JSON
    Given valid authentication credentials are provided
    When I send a POST request to the auth endpoint
    Then the response status code should be 200
    And the response body should be valid JSON           
    
    
# ============================================================
# IDEMPOTENCY / REPEATED REQUESTS
# ============================================================

 
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    