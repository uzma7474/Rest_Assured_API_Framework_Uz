package stepdefinitions.hook;

import utils.TokenManager;

import io.cucumber.java.After;


public class Hook {
	
    @After
    public void tearDown() {
        // Prevents memory leaks and cross-contamination between test threads
        TokenManager.clear();
    }

}
