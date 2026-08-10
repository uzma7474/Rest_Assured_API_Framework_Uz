 Feature: User Registration API

	  As an API consumer
	  I want to register a new user
	  So that the user can access the EventHub application


  @smoke @auth @register @positive
  Scenario Outline: Successfully register a new user

    Given I have valid registration details
      | email   | password   | 
      | <email> | <password> |

    When I send a "<method_name>" request to register the user

    Then the registration response status code should be <status_code>

    And the registration response success should be <success>

	Examples:
		| email              | password | method_name    | status_code | success    |
		| sunnn@example.com  | sunnn@123| POST           | 201         | true               |

 