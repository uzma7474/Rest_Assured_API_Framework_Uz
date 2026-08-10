
package listeners;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utils.ExtentManager;

/**
 * TestListener
 *
 * TestNG listener for REST Assured + Cucumber framework.
 *
 * Responsibilities: - Initialize ExtentReports - Create ExtentTest for every
 * test - Log test start - Log passed tests - Log failed tests - Log skipped
 * tests - Capture exceptions - Flush ExtentReport after execution
 */
public class TestListener implements ITestListener {

	private static ExtentReports extentReports;

	/*
	 * ThreadLocal is used because TestNG/Cucumber scenarios may execute in
	 * parallel.
	 *
	 * Each thread gets its own ExtentTest instance.
	 */
	private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	// =========================================================
	// SUITE START
	// =========================================================

	/**
	 * Called before the TestNG suite starts.
	 */
	@Override
	public void onStart(ITestContext context) {

		extentReports = ExtentManager.getInstance();

		System.out.println("================================================");

		System.out.println("Test Suite Started : " + context.getSuite().getName());

		System.out.println("Start Time : " + getCurrentTime());

		System.out.println("================================================");
	}

	// =========================================================
	// TEST START
	// =========================================================

	/**
	 * Called before each test starts.
	 */
	@Override
	public void onTestStart(ITestResult result) {

		String testName = getTestName(result);

		ExtentTest test = extentReports.createTest(testName);

		/*
		 * Add test metadata.
		 */
		test.assignCategory("REST Assured API");

		test.info("Test started at: " + getCurrentTime());

		test.info("Test Class: " + result.getTestClass().getName());

		/*
		 * Store ExtentTest for the current thread.
		 */
		extentTest.set(test);

		System.out.println("[START] " + testName);
	}

	// =========================================================
	// TEST PASS
	// =========================================================

	/**
	 * Called when a test passes.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {

		ExtentTest test = getExtentTest();

		if (test != null) {

			test.pass("Test passed successfully.");

			test.info("Execution time: " + getDuration(result) + " ms");

			test.info("Completed at: " + getCurrentTime());
		}

		System.out.println("[PASS] " + getTestName(result));

		removeExtentTest();
	}

	// =========================================================
	// TEST FAILURE
	// =========================================================

	/**
	 * Called when a test fails.
	 */
	@Override
	public void onTestFailure(ITestResult result) {

		ExtentTest test = getExtentTest();

		Throwable throwable = result.getThrowable();

		String errorMessage = getFailureMessage(result);

		if (test != null) {

			test.fail("Test failed.");

			test.fail("Error: " + errorMessage);

			test.info("Execution time: " + getDuration(result) + " ms");

			test.info("Failed at: " + getCurrentTime());

			/*
			 * Add complete exception details when available.
			 */
			if (throwable != null) {

				test.fail(throwable);
			}
		}

		System.out.println("[FAIL] " + getTestName(result));

		System.out.println("Error: " + errorMessage);

		removeExtentTest();
	}

	// =========================================================
	// TEST SKIPPED
	// =========================================================

	/**
	 * Called when a test is skipped.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {

		ExtentTest test = getExtentTest();

		if (test != null) {

			test.skip("Test was skipped.");

			test.info("Reason: " + getFailureMessage(result));
		}

		System.out.println("[SKIPPED] " + getTestName(result));

		removeExtentTest();
	}

	// =========================================================
	// RETRY
	// =========================================================

	/**
	 * Called when a test fails but is within the configured success percentage.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

		ExtentTest test = getExtentTest();

		if (test != null) {

			test.log(Status.WARNING, "Test failed but is within " + "success percentage.");
		}

		System.out.println("[RETRY] " + getTestName(result));
	}

	// =========================================================
	// SUITE FINISH
	// =========================================================

	/**
	 * Called after the TestNG suite finishes.
	 */
	@Override
	public void onFinish(ITestContext context) {

		if (extentReports != null) {

			extentReports.flush();
		}

		System.out.println("================================================");

		System.out.println("Test Suite Finished : " + context.getSuite().getName());

		System.out.println("Passed : " + context.getPassedTests().size());

		System.out.println("Failed : " + context.getFailedTests().size());

		System.out.println("Skipped : " + context.getSkippedTests().size());

		System.out.println("Report : " + new File("reports/ExtentReport.html").getAbsolutePath());

		System.out.println("End Time : " + getCurrentTime());

		System.out.println("================================================");
	}

	// =========================================================
	// HELPER METHODS
	// =========================================================

	/**
	 * Returns ExtentTest for current thread.
	 */
	public static ExtentTest getExtentTest() {

		return extentTest.get();
	}

	/**
	 * Removes ExtentTest from current thread.
	 *
	 * Important for parallel execution and preventing ThreadLocal memory leaks.
	 */
	private void removeExtentTest() {

		extentTest.remove();
	}

	/**
	 * Returns test name.
	 */
	private String getTestName(ITestResult result) {

		if (result == null || result.getMethod() == null) {

			return "Unknown Test";
		}

		return result.getMethod().getMethodName();
	}

	/**
	 * Returns execution duration.
	 */
	private long getDuration(ITestResult result) {

		return result.getEndMillis() - result.getStartMillis();
	}

	/**
	 * Returns current date/time.
	 */
	private String getCurrentTime() {

		return LocalDateTime.now().format(DATE_TIME_FORMATTER);
	}

	/**
	 * Returns failure message.
	 */
	private String getFailureMessage(ITestResult result) {

		if (result == null || result.getThrowable() == null) {

			return "No failure information available.";
		}

		Throwable throwable = result.getThrowable();

		if (throwable.getMessage() == null || throwable.getMessage().isBlank()) {

			return throwable.getClass().getSimpleName();
		}

		return throwable.getMessage();
	}
}
