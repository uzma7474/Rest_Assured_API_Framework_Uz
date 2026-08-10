package utils;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * ExtentManager
 *
 * Centralized ExtentReports manager.
 */
public final class ExtentManager {

	private static ExtentReports extent;

	private ExtentManager() {
		// Prevent object creation
	}

	/**
	 * Returns singleton ExtentReports instance.
	 *
	 * @return ExtentReports
	 */
	public static synchronized ExtentReports getInstance() {

		if (extent == null) {

			/*
			 * Create reports directory if it does not exist.
			 */
			new File("reports").mkdirs();

			ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");

			reporter.config().setReportName("EventHub API Automation Report");

			reporter.config().setDocumentTitle("EventHub API Test Report");

			extent = new ExtentReports();

			extent.attachReporter(reporter);

			/*
			 * System information shown in report.
			 */
			extent.setSystemInfo("Application", "EventHub API");

			extent.setSystemInfo("Framework", "REST Assured + Cucumber");

			extent.setSystemInfo("Execution", "TestNG");

			extent.setSystemInfo("Environment", System.getProperty("env", "qa"));
		}

		return extent;
	}
}
