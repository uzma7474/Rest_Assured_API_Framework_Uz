@CreateBooking
Feature: Create Booking API

  Background:
    Given the create booking API endpoint is "/booking"
    
 @positive @smoke @CreateBooking01
  Scenario: Create booking with all valid fields
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the create booking api response Content-Type should be "application/json"
    And the create booking api response should contain a numeric "bookingid"
    And the create booking api response should contain "booking" object
    And the create booking api booking firstname should match the request
    And the create booking api booking lastname should match the request
    And the create booking api booking totalprice should match the request
    And the create booking api booking depositpaid should match the request
    And the create booking api booking checkin should match the request
    And the create booking api booking checkout should match the request
    And the create booking api booking additionalneeds should match the request   
    
  @positive @CreateBooking02
  Scenario: Create booking without explicitly specifying Accept header
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is not specified
    And the create booking request contains valid booking details
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the create booking api response Content-Type should be "application/json"   
    
  @positive @CreateBooking03
  Scenario: Create booking with Accept application/json
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the create booking api response Content-Type should be "application/json"
    And the create booking api response should be returned in JSON format    
    
   @positive @CreateBooking04
  Scenario: Create booking with Accept application/xml
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the create booking api response should be returned in XML format
    
 @positive @CreateBooking05
  Scenario: Create booking with deposit paid as true
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    And the depositpaid value is "true"
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the response of create booking api depositpaid should be "true"   
    
    
  @positive @CreateBooking06
  Scenario: Create booking with deposit paid as false
   Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    And the depositpaid value is "false"
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the response of create booking api depositpaid should be "false"
    
 
  @positive @CreateBooking07
  Scenario: Create booking with additional needs
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking details
    And the additionalneeds is "Breakfast"
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the response additionalneeds should be "Breakfast"    
  
  
@positive @CreateBooking08
  Scenario: Create booking without additionalneeds
    Given the create booking request Content-Type is "application/json"
    And in create booking request the Accept header is "application/json"
    And the create booking request contains valid booking
    |firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
    | Priya	   | Patil    |   200      |   true      | 2026-08-01 | 2026-09-01 | Lunch           |
    When the user sends a POST request to create booking
    Then the create booking api response status code should be 200
    And the booking should be created successfully
    
 
  @positive @CreateBooking09
  Scenario: Create booking with different valid firstname
    Given the request contains a valid firstname "John" 
    And the request contains a valid lastname "Brown" 
    And the request contains a valid totalprice "111" 
    And the request contains a valid depositpaid "true" 
    And the request contains valid booking dates from "2026-09-10" to "2026-09-15" 
    And the request contains a valid additionalneeds "Breakfast"
    When the user sends a POST request to create booking with booking details
    Then the create booking api response status code should be 200
    And the create booking response firstname should be "John" 
    And the create booking response lastname should be "Brown" 
    And the create booking response totalprice should be "111" 
    And the create booking response depositpaid should be "true" 
    And the create booking response additionalneeds should be "Breakfast" 
    And the create booking response checkin should be "2026-09-10" 
    And the create booking response checkout should be "2026-09-15"   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     