//package stepdefinitions;
//
//import context.ScenarioContext;
//import io.cucumber.java.en.Then;
//import org.testng.Assert;
//
//public class commonSteps {
//
//	private final ScenarioContext context;
//
//	public commonSteps(ScenarioContext context) {
//		this.context = context;
//	}
//
//	//@Then("the response status code should be {int}")
//	public void response_status_code_should_be(Integer expectedStatusCode) {
//
//		Assert.assertNotNull(context.getResponse(), "Response is null. API request may not have been executed.");
//
//		int actualStatusCode = context.getResponse().getStatusCode();
//
//		System.out.println("Expected Status Code : " + expectedStatusCode);
//		System.out.println("Actual Status Code   : " + actualStatusCode);
//		System.out.println("========== AUTH ME RESPONSE ==========");
//		System.out.println("Status Code : " + context.getResponse().getStatusCode());
//		System.out.println("Status Line : " + context.getResponse().getStatusLine());
//		System.out.println("Body       : " + context.getResponse().asPrettyString());
//		System.out.println("======================================");
//
//		Assert.assertEquals(actualStatusCode, expectedStatusCode.intValue(), "Unexpected response status code");
//
////		System.out.println("Status Code : "+context.getResponse().getStatusCode());
////		
////		
////		if(context.getResponse().statusCode() ==200 && context.getBaseErrorResponse()==null) {
////			Assert.assertNotNull(context.getResponse(), "Response is null. API request may not have been executed.");
////			Assert.assertEquals(context.getResponse().statusCode(), expectedStatusCode.intValue(), "Unexpected response status code");
////		}
////		else if(context.getBaseErrorResponse().isSuccess()== false && context.getResponse().statusCode() ==401) {
////			Assert.assertEquals(context.getBaseErrorResponse().getError(), "Unauthorized");
////
////		}
////		else if(context.getBaseErrorResponse().isSuccess()== false && context.getResponse().statusCode() ==200) {
////			Assert.assertEquals(context.getBaseErrorResponse().getError(), "Invalid or expired token");
////
////		}
//
//	}
//
//	@Then("the response content type should be {string}")
//	public void response_content_type_should_be(String expectedContentType) {
//
//		Assert.assertNotNull(context.getResponse(), "Response is null.");
//
//		Assert.assertTrue(context.getResponse().getContentType().contains(expectedContentType),
//				"Unexpected content type");
//	}
//
//	@Then("the response field success should be {string}")
//	public void response_success_should_be(String expectedSuccess) {
//
//		Assert.assertNotNull(context.getResponse(), "Response is null.");
//
//		boolean expected = Boolean.parseBoolean(expectedSuccess);
//
//		Assert.assertEquals(context.getResponse().jsonPath().getBoolean("success"), expected,
//				"Unexpected success value");
//	}
//}