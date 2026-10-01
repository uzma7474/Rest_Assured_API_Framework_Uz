package listeners;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepStarted;


public class CucumberListener implements EventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Register handlers for specific lifecycle events
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::onTestStepStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        System.out.println("Scenario Started: " + event.getTestCase().getName());
    }

    private void onTestStepStarted(TestStepStarted event) {
        // This fires before every Given, When, Then step
        System.out.println("Step Started...");
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        System.out.println("Scenario Finished Status: " + event.getResult().getStatus());
    }
}
