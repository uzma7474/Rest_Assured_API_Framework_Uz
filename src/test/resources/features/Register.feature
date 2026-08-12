 Feature: User Registration API

	  As an API consumer
	  I want to register a new user
	  So that the user can access the EventHub application


  Background:
    Given the registration endpoint is "/api/auth/register"
     


# ============================================================================================== 
# POSITIVE SCENARIOS 
# ==============================================================================================

  @smoke @auth @register @positive @TC001 
  Scenario Outline: Successfully register a new user

    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |

    When I send a "<method_name>" request to register the user

    Then the registration response status code should be <status_code>

    And the registration response success should be "<success>"

	Examples:
		| email                  | password     | method_name    | status_code | success    |
		| pwd1@example.com       | pwd1@123     | POST           | 201         | true       |

 
  @positive @registration @TC002
  Scenario Outline: Successfully register a user with valid email and password
     Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the registration response success should be "<success>"
    And the response should contain a non-empty token
    And the response should contain a user object
    And the user email should be "<email>"
    And the user should have a id
 
	Examples:
		| email                | password     | method_name    | status_code | success    |
		| qa_2@example.com     | qa_2@123     | POST           | 201         | true       | 



  @positive @registration @TC003
  Scenario:  Successfully register a new user with valid credentials

     Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the registration response success should be "<success>"
    And the response should contain a non-empty token
    And the response should contain a user object
    And the user email should be "<email>"
    And the user ID should be generated
 
	Examples:
		| email                | password     | method_name    | status_code | success    |
		| qa_3@example.com     | qa_3@123     | POST           | 201         | true       | 


  @positive @registration @TC004
  Scenario: Verify unique user ID is generated after registration
     Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response user ID should not be null
    And the response user ID should be greater than 0
    
	Examples:
		| email                | password     | method_name    | status_code | success    |
		| qa_4@example.com     | qa_4@123     | POST           | 201         | true       | 
		
	
  @positive @registration @TC005
  Scenario Outline: Verify response email matches request email
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response user email should be "<email>"		

	Examples:
		| email                    | password         | method_name    | status_code | success    |
		| matching@example.com     | matching@123     | POST           | 201         | true       | 		
		
  @positive @registration @TC006
  Scenario Outline: Verify response email matches request email
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response Content-Type should be "<content_type>"		

	Examples:
		| email                | password      | method_name    | status_code | success    |content_type      |
		| cont@example.com     | cont@123      | POST           | 201         | true       | application/json |		
		
		
  @positive @registration  @TC007
  Scenario Outline: Successfully register users with different valid email formats
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the registration response success should be "<success>"
    And the response should contain a non-empty token
    And the response user email should be "<email>"

    Examples:
		| email             			  | password       | method_name    | status_code | success    |
		| user1@example.com  		  | csecret123    | POST           | 201         | true       |         
      	| firstname.lastname@test.com  | Secret@123     | POST           | 201        | true       |
     	| user123@example.org          | Test@12345     | POST           | 201        | true       |
      	| qa.automation@test.co.in     | Automation@123 | POST           | 201        | true       |
		

  @positive @registration @contract @TC008
  Scenario: Verify successful registration response schema
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response should match the registration success schema
    And the "success" field should be a boolean
    And the "token" field should be a string
    And the "user" field should be an object
    And the user id field should be a number
    And the user "<email>" field should be a string		
		
	Examples:
		| email               | password     | method_name    | status_code | success    |content_type      |
		| sch@example.com     | sch@123      | POST           | 201         | true       | application/json |	
		
		
  @positive @registration @security @TC009
  Scenario: Verify password is not returned in successful response
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response should not contain the "password" field
    And the response user object should not contain the "password" field		
	
	Examples:
		| email              | password     | method_name    | status_code | success    |content_type      |
		| sc@example.com     | schlk@123    | POST           | 201         | true       | application/json |	
		
		
  @positive @registration @e2e @TC0010 
  Scenario: Verify token returned after registration can be used for authenticated operations
    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And  the EventHub response should contain a valid JWT token	
		
	Examples:
		| email              | password   | method_name    | status_code | success    |content_type      |
		| to@example.com     | tok@123    | POST           | 201         | true       | application/json |	
				

#####################################################################################################
# Negative Test Cases
#####################################################################################################

  # ============================================================
  # NEGATIVE - EMAIL VALIDATION
  # ============================================================

  @negative @email @TC0011
  Scenario: Register user with invalid email format
    Given the registration request body contains:
      | email   | password   | 
      | <email> | <password> |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response success field should be false
    And the response error should be "Validation failed"
    And the email validation message should be "A valid email is required"		
		
	Examples:
		| email           | password   | method_name    | status_code | success    |content_type      |
		| student@examp   | secret123  | POST           | 400         | true       | application/json |	
						
	
 @negative @email @TC0012
  Scenario: Register user without email domain
    Given the registration request body contains:
      | email    | student@ |
      | password | secret123 |
    When I send a "<method_name>" request to register the user
    Then the registration response status code should be <status_code>
    And the response success field should be false	
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"    	
		
	Examples:
		| email           | password   | method_name    | status_code | success    |content_type      |
		| student@examp   | secret123  | POST           | 400         | true       | application/json |	
			
		
  @negative @email @TC0013
  Scenario: Register user without @ symbol in email
    Given the registration request body contains:
      | email    | studentexample.com |
      | password | secret123          |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"	
		
  @negative @email @TC0014
  Scenario: Register user with empty email
    Given the registration request body contains:
      | email    |           |
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the email validation message should be "A valid email is required"
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"
		
  @negative @email @TC0015
  Scenario: Register user with null email
    Given the registration request body contains:
      | email    | null      |
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false	
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"

		
  @negative @email @TC0016
  Scenario: Register user with email containing spaces
    Given the registration request body contains:
      | email    | student user@example.com |
      | password | secret123                |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false	
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"
    
  @negative @email @TC0017
  Scenario: Register user with email containing only spaces
    Given the registration request body contains:
      | email    |           |
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false	
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required" 
    
   @negative @email @TC0018
  Scenario: Register user with email missing username
    Given the registration request body contains:
      | email    | @example.com |
      | password | secret123    |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required" 
    
  @negative @email @TC0019
  Scenario: Register user with email missing top level domain
    Given the registration request body contains:
      | email    | student@example |
      | password | secret123       |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"

    
  # ============================================================
  # NEGATIVE - PASSWORD VALIDATION
  # ============================================================

  @negative @password @TC0020
  Scenario: Register user with password shorter than minimum length
    Given the registration request body contains:
      | email    | shortpassword@example.com |
      | password | abc                       |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
    
  @negative @password @TC0021
  Scenario: Register user with empty password
    Given the registration request body contains:
      | email    | emptypassword@example.com |
      | password |                           |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
    
    
  @negative @password @TC0022
  Scenario: Register user with null password
    Given the registration request body contains:
      | email    | nullpassword@example.com |
      | password | null                     |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
    
  @negative @password @TC0023
  Scenario: Register user with password containing only spaces
    Given the registration request body contains:
      | email    | spacepassword@example.com |
      | password |                           |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false  
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
     

  @negative @password @TC0024
  Scenario: Register user with one character password
    Given the registration request body contains:
      | email    | onechar@example.com |
      | password | a                   |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false   
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
         
    
    
  @negative @password @TC0025
  Scenario: Register user with password at invalid minimum boundary
    Given the registration request body contains:
      | email    | boundary@example.com |
      | password | abc                   |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false   
    And the response error message should be "Validation failed"
    And the response details message should be "Password must be at least 6 characters"    
     
    
  # ============================================================
  # NEGATIVE - REQUIRED FIELD VALIDATION
  # ============================================================

  @negative @required @TC0026
  Scenario: Register user without email field
    Given the registration request body contains:
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false   
    And the response error message should be "Validation failed"
    And the response details message should be "A valid email is required"
     
   
  @negative @required @TC0027
  Scenario: Register user without password field
    Given the registration request body contains:
      | email | missingpassword@example.com |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false
    And the response error message should be "Validation failed"
    And the response details message should be Password must be at least 6 characters 
    
  @negative @password @TC0028
  Scenario: Register user with one character password
    Given the registration request body contains:
      | email    |    |
      | password |    |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false 
    And the response error message should be "Validation failed"
    And Password must be at least 6 characters      


  @negative @password @TC0029
  Scenario: Register user with one character password
    Given the registration request body contains:
      | email    |  null  |
      | password |  null  |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false      
    
    
  # ============================================================
  # NEGATIVE - DUPLICATE USER
  # ============================================================

  @negative @duplicate @TC0030
  Scenario: Register an already registered user
    Given a user already exists with email "qa_1@example.com"
    And the registration request body contains:
      | email    | qa_1@example.com |
      | password | qa_1@123           |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false     
    And the response should indicate that the email is already registered    
    
    
  @negative @duplicate @TC0031
  Scenario: Register the same user twice
    Given the registration request body contains:
      | email    | dup@example.com |
      | password | secc123             |
    When I send a "POST" request to register the user
    Then the registration response status code should be 201
    When I send the same registration request again
    Then the response status code should be 400
    And the response success field should be false    
    
  # ============================================================
  # NEGATIVE - DATA TYPE VALIDATION
  # ============================================================

  @negative @datatype @TC0033
  Scenario: Register user with numeric email
    Given the registration request body contains:
      | email    | 123456789 |
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false    
    
    
 @negative @datatype @TC0034
  Scenario: Register user with numeric password
    Given the registration request body contains:
      | email    | numericpwd@example.com |
      | password | 987654321                   |
    When I send a "POST" request to register the user
    Then the registration response status code should be 201
    And the response success field should be true    
    
  @negative @datatype @TC0035
  Scenario: Register user with boolean email
    Given the registration request body contains:
      | email    | true      |
      | password | secret123 |
    When I send a "POST" request to register the user
    Then the registration response status code should be 400
    And the response success field should be false     
 
 
 

    
 
    

   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
           
    
    
    
    
    
    
    
    
    
    
    
    
    
       
      
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
       
    
    
    
    
    
    
    
    
    
    
    
    	
		
		
				    