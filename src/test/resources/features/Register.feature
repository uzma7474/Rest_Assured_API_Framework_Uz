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
  Scenario: Successfully register a user with valid email and password
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

