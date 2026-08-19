Feature: Get Events API - Authentication

	Background: 
		Given the EventHub API base URL is configured 
		And the user has a valid authentication token 
		And the request Accept header is set to "application/json"