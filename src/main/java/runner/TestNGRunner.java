package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners(listeners.TestListener.class)
@CucumberOptions(
		features = "src/test/resources/features", 
		glue = { "stepdefinitions" }, 
		plugin = { 
					"pretty",
					"html:target/cucumber-reports/cucumber.html", 
					"json:target/cucumber-reports/cucumber.json",
					
		}, 
		tags = "@CreateBooking09", 
		publish = false, 
		dryRun = false, 
		monochrome = true   )
public class TestNGRunner extends AbstractTestNGCucumberTests {
	
	 // Enables parallel execution of scenarios if needed
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
    
    
    
}