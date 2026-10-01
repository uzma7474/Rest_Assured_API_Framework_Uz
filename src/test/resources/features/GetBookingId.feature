Feature: Get Booking IDs API

  As an API consumer
  I want to retrieve booking IDs from the Booking API
  So that I can identify existing bookings using optional filters


  Background:
    Given the Restful Booker for GetBookingIds API is available
    And the booking endpoint is configured as "/booking"


# =============================================================================================
# POSITIVE TEST CASES - BASIC REQUEST
# =============================================================================================

  @positive @smoke @getBookingIds01
  Scenario: Get all booking IDs without any query parameters
    When the client sends a GET request to the booking endpoint
    Then the getBookingIds response status code should be 200
    And the getBookingIds response content type should be "application/json"
    And the response should contain a JSON array
    And each booking object should contain a "bookingid" field
    And each "bookingid" should be a number

 @positive @getBookingIds02
  Scenario: Get all booking IDs using an empty query string
    When the client sends a GET request to the booking endpoint with an empty query string
    Then the response status code should be 200
    And the response content type should be "application/json"
    And the response should contain a JSON array


# =================================================================================================
# FIRSTNAME FILTER
# =================================================================================================

  @positive @firstname @getBookingIds03
  Scenario: Get booking IDs by valid firstname
    Given a booking exists with a valid firstname
    When the client sends a GET request with the firstname filter
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response should contain empty list of booking Id

  @positive @firstname @getBookingIds04
  Scenario: Get booking IDs without request body
    When the user send Get request for getting booking ids
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response should contain list of booking Id

  @smoke @positive @getBookingIds05
  Scenario: Get all booking IDs without query parameters
    When the user send Get request for getting booking ids
    Then the response status code should be 200
    And the response body should contain booking IDs
    And each booking ID should be a valid integer

  @positive @getBookingIds06
  Scenario: Get booking IDs using firstname
    Given firstname is "sally"
    When the user sends a GET request with firstname
    Then the response status code should be 200
    And the response body should contain matching booking IDs


@positive @getBookingIds07
  Scenario: Get booking IDs using lastname
    Given lastname is "brown"
    When the user sends a GET request with lastname
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response contain list of booking Id should be "empty"

@positive @getBookingIds08
  Scenario: Get booking IDs using firstname and lastname
    Given firstname is "sally"
    And lastname is "brown"
    When the user sends a GET request with firstname and lastname
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response contain list of booking Id should be "empty"

  @positive @getBookingIds09
  Scenario: Get booking IDs using checkin date
    Given checkin date is "2014-03-13"
    When the user sends a GET request with checkin date
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response contain list of booking Id should be "non-empty"

  @positive @getBookingIds10
  Scenario: Get booking IDs using checkout date
    Given checkout date is "2014-05-21"
    When the user sends a GET request with checkout date
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response contain list of booking Id should be "empty" 

 @positive @getBookingIds11
  Scenario: Get booking IDs using checkin and checkout dates
    Given checkin date is "2014-03-13"
    And checkout date is "2014-05-21"
    When the user sends a GET request with checkin and checkout dates
    Then the response status code should be 200
    And the response contain list of booking Id should be "empty" 


 @positive @getBookingIds12
  Scenario: Get booking IDs using firstname and checkin date
    Given firstname is "sally"
    And checkin date is "2014-03-13"
    When the user sends a GET request with firstname and checkin date
    Then the response status code should be 200
    And the response contain list of booking Id should be "empty" 

 @positive @getBookingIds13
  Scenario: Get booking IDs using lastname and checkout date
    Given lastname is "brown"
    And checkout date is "2014-05-21"
    When the user sends a GET request with lastname and checkout date
    Then the response status code should be 200
    And the response contain list of booking Id should be "empty" 
    
  @positive @getBookingIds14
  Scenario: Get booking IDs using firstname lastname and date range
    Given firstname is "sally"
    And lastname is "brown"
    And checkin date is "2014-03-13"
    And checkout date is "2014-05-21"
    When the user sends a GET request with all booking filters
    Then the response status code should be 200
	And the response contain list of booking Id should be "empty"    
    
#################################################################################################
# Negative Test Cases
#################################################################################################

  @negative @getBookingIds15
  Scenario: Get booking IDs using a firstname that does not exist
    Given firstname is "nonexistentfirstname"
    When the user sends a GET request with firstname
    Then the response status code should be 200
    And the response contain list of booking Id should be "empty"    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    