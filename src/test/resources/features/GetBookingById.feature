@GetBookingByID
Feature: Get a specific booking

  As an API consumer
  I want to retrieve a booking using its booking ID
  So that I can view the booking details
 

# ===============================================================================================
# POSITIVE TEST CASES
# ===============================================================================================  

  @positive @smoke @GetBookingById01
  Scenario: Get an existing booking using a valid booking ID
    Given the Get Booking By Id API is available
    And the booking ID 1 exists
    When I send a GET request to get booking by id "/booking/1"
    Then the response of get booking by id status code should be 200
    And the response of get booking by id api Content-Type should be "application/json"
    And the response should contain the booking details
    And the get booking by id api response should contain "firstname"
    And the get booking by id api response should contain "lastname"
    And the get booking by id api response should contain "totalprice"
    And the get booking by id api response should contain "depositpaid"
    And the get booking by id api response should contain "bookingdates"
    And the get booking by id api response should contain "additionalneeds"
    
    
  @positive @GetBookingById02
  Scenario: Get an existing booking with Accept application/json
    Given the booking ID 1 exists
    And the request header "Accept" is "application/json"
    When I send a GET request to get booking by id "/booking/1"
    Then the response of get booking by id status code should be 200
    And the response of get booking by id api Content-Type should be "application/json"
    And the response should contain valid JSON
    And the response should contain the booking details 
    
  @positive @GetBookingById03
  Scenario: Get an existing booking with Accept application/xml
    Given the booking ID 1 exists
    And the get booking by id api request header "Accept" is "application/xml"
    When I send a GET request to get booking by id "/booking/1" in XML format
    Then the response of get booking by id status code should be 200
    And the response of get booking by id api Content-Type should be "application/xml"
    And the response should contain the booking details in XML format
     
  @positive @GetBookingById04
  Scenario: Get booking without specifying Accept header
    Given the booking ID 1 exists
    When I send a GET request to get booking by id "/booking/1"
    Then the response of get booking by id status code should be 200
    And the response should be returned in the default format
    And the response should contain the booking details    
    
   @positive @GetBookingById05
  Scenario: Get a booking using another valid booking ID
    Given the booking ID 5 exists
    When I send a GET request to get booking by id "/booking/5"
    Then the response of get booking by id status code should be 200
    And the response should contain the booking details 
    
    
#================================================================================
# Response schema validation
#================================================================================
  @positive @contract @GetBookingById06
  Scenario: Validate the complete booking response schema
    Given the booking ID 5 exists
    When I send a GET request to get booking by id "/booking/5"
    Then the response of get booking by id status code should be 200
    And the response should contain the booking details
    And the get booking by id api response should contain "firstname"
    And the get booking by id api response should contain "lastname"
    And the get booking by id api response should contain "totalprice"
    And the get booking by id api response should contain "depositpaid"
    And the get booking by id api response should contain "bookingdates"
    And the get booking by id api response should contain "additionalneeds" 
    And the complete booking response schema should be valid   
    
    
   @positive @contract @GetBookingById07
  Scenario: Validate booking field data types
    Given the booking ID 5 exists
    When I send a GET request to get booking by id "/booking/5"
    Then the response of get booking by id status code should be 200
    And "firstname" should be a String
    And "lastname" should be a String
    And "totalprice" should be a Number
    And "depositpaid" should be a Boolean
    And "bookingdates" should be an Object
    And "bookingdates.checkin" should be a Date
    And "bookingdates.checkout" should be a Date
    And "additionalneeds" should be a String
    
    
  @positive @contract @GetBookingById08
  Scenario: Validate booking dates object
    Given the booking ID 5 exists
    When I send a GET request to get booking by id "/booking/5"
    Then the response of get booking by id status code should be 200
    And "bookingdates" should not be null
    And "bookingdates.checkin" should not be null
    And "bookingdates.checkout" should not be null
    
    @positive @contract @GetBookingById09
  Scenario: Validate booking response contains no unexpected top-level structure
    Given the booking ID 1 exists
    When I send a GET request to get booking by id "/booking/1"
    Then the response of get booking by id status code should be 200
    And the complete booking response schema should be valid 

#=========================================================================================
# Booking ID positive boundary cases
#=========================================================================================    
    
  @positive @boundary @GetBookingById10
  Scenario: Get booking using the minimum valid booking ID
    Given the booking ID 2 exists
    When I send a GET request to get booking by id "/booking/2"
    Then the response of get booking by id status code should be 200
    And the response should contain the booking details
    And the response should contain the booking details 
    
    
  @positive @boundary @GetBookingById11
  Scenario: Get booking using a multi-digit booking ID
    Given the booking ID 100 exists
    When I send a GET request to get booking by id "/booking/100"
    Then the response of get booking by id status code should be 200
    And the response should contain the booking details

	

#===========================================================================================
# Negative — non-existing booking
#===========================================================================================

# Fail
  @positive @boundary @GetBookingById12
  Scenario: Get booking using a large existing booking ID
    Given the booking ID 1000 exists
    When I send a GET request to get booking by id "/booking/1000"
    Then the response of get booking by id status code should be 404
    And the response should not contain the booking detail but have error "Not Found"
    
    
#======================================================================================
# Negative — non-existing booking
#=======================================================================================

  @negative @smoke @GetBookingById13
  Scenario: Get booking using a non-existing booking ID
    Given the booking ID 99999999 exists
     When I send a GET request to get booking by id "/booking/99999999"
    Then the response status code should be 404
    And the response should not contain the booking detail but have error "Not Found"
    
    
  @negative @GetBookingById14
  Scenario: Get booking using another non-existing booking ID
    Given the booking ID 999999999 exists
     When I send a GET request to get booking by id "/booking/999999999"
    Then the response status code should be 404   
    And the response should not contain the booking detail but have error "Not Found"   
    
  @negative @GetBookingById15
  Scenario: Get booking using booking ID zero
    Given the booking ID 0 exists
     When I send a GET request to get booking by id "/booking/0"
    Then the response status code should be 404   
    And the response should not contain the booking detail but have error "Not Found"      
    
  @negative @GetBookingById16
  Scenario: Get booking using a negative booking
    Given booking ID -1 does not exists
    When I send a GET request to get booking by id "/booking/-1"
    Then the response status code should be 404   
    And the response should not contain the booking detail but have error "Not Found"      
    
#=======================================================================================
# Missing booking ID
#=======================================================================================

  @negative @validation @GetBookingById17
  Scenario: Get booking without providing booking ID
    Given booking ID -0 does not exists
    When I send a GET request to get booking by id "/booking/0"
    Then the response status code should be 404
    And the response should not contain the booking detail but have error "Not Found" 
        
  @negative @GetBookingById18
  Scenario: Get booking with an empty booking ID
    When I send a GET request to get booking by id "/booking/"      
    Then the response status code should be 200
    And the response contain bookingids 
    
#=======================================================================================
# Invalid booking ID formats
#=======================================================================================

  @negative @GetBookingById19
  Scenario: Get booking using alphabetic booking ID
    When I send a GET booking by id request to "/booking/abc"
    Then the response should not contain booking details
    And the response status code should be 404
    
    
    @negative @GetBookingById20
  Scenario: Get booking using alphanumeric booking ID
    When I send a GET booking by id request to "/booking/abc123"
    Then the response should not contain booking details
    And the response status code should be 404

  @negative @GetBookingById21
  Scenario: Get booking using special character as booking ID
    When I send a GET booking by id request to "/booking/@"
    Then the response should not contain booking details
    And the response status code should be 404

  @negative @GetBookingById22
  Scenario: Get booking using whitespace as booking ID
    When I send a GET booking by id request to "/booking/%20"
    Then the response should not contain booking details
    And the response status code should be 404
    

  @negative @GetBookingById23
  Scenario: Get booking using decimal booking ID
    When I send a GET booking by id request to "/booking/1.5"
    Then the response should not contain booking details
    And the response status code should be 418

  @negative @GetBookingById24
  Scenario: Get booking using boolean booking ID
    When I send a GET booking by id request to "/booking/true"
    Then the response should not contain booking details
    And the response status code should be 404

  @negative @GetBookingById25
  Scenario: Get booking using null-like booking ID
    When I send a GET booking by id request to "/booking/null"
    Then the response should not contain booking details
    And the response status code should be 404

#=================================================================================================
# Accept header positive scenarios
#=================================================================================================  

  @positive @content-negotiation @GetBookingById26
  Scenario: Request booking in JSON format
    Given the booking ID 7 exists
	And the request header "Accept" is "application/json"
    When I send a GET request to get booking by id "/booking/7"
    Then the response of get booking by id status code should be 200
    And the response of get booking by id api Content-Type should be "application/json"
    And the response should contain valid JSON

  @positive @content-negotiation @GetBookingById27
  Scenario: Request booking in XML format
    Given the booking ID 1 exists
    And the request header "Accept" is "application/xml"
    When I send a GET request to get booking by id "/booking/1"
    Then the response status code should be 200
    And the response Content-Type should contain "application/xml"
    And the response should be valid XML




  
        
        