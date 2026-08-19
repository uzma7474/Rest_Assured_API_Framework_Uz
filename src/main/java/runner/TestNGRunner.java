package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@Listeners(listeners.TestListener.class)
@CucumberOptions(features = "src/test/resources/features", 
	glue = { "stepdefinitions", "hooks" }, 
	plugin = { 
				"pretty",
				"html:target/cucumber-reports/cucumber.html", 
				"json:target/cucumber-reports/cucumber.json",
				"junit:target/cucumber-reports/cucumber.xml" 
	}, 
	tags = "@AuthMe21", 
	publish = false, 
	dryRun = false, 
	monochrome = true)
public class TestNGRunner extends AbstractTestNGCucumberTests {
}