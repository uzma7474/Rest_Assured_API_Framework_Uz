package stepdefinitions;

import utils.TokenManager;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hook {
	
  
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
        		// Prevents memory leaks and cross-contamination between test threads
            TokenManager.clear();
            
            scenario.log("❌ SCENARIO FAILED: Inspect the response details above.");
            System.out.println("=================== Test Failed ===================== ");
            
        } else {
        		TokenManager.clear();
            scenario.log("✅ SCENARIO PASSED");
            System.out.println("=================== Test Passed ===================== ");
        }
    }

}
