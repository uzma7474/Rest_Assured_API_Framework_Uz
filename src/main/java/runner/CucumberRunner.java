package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.DataProvider;



@CucumberOptions(features = "src/test/resources/features", 
	glue = { "stepdefinitions" }, 
	plugin = { 
				"pretty",
				"html:target/cucumber-reports/cucumber.html", 
				"json:target/cucumber-reports/cucumber.json",
				"listeners.CucumberListener",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
	}, 
	tags = "@GetBookingByID", 
	publish = false, 
	dryRun = false, 
	monochrome = true)
public class CucumberRunner extends AbstractTestNGCucumberTests {
	
	 // Enables parallel execution of scenarios if needed
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}