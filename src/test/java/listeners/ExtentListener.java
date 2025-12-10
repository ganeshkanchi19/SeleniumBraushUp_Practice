package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import reports.ExtentManager;

public class ExtentListener implements ITestListener {
	private static ExtentReports extent = ExtentManager.createInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Override
	public void onStart(ITestContext context) {
		System.out.println("ExtentListener.onStart() - TestNG context starting: " + context.getName());
	}

	@Override
	public void onTestStart(ITestResult result) {
		// example: attempt to read a 'browser' parameter from TestNG or from
		// DriverFactory
		String browser = System.getProperty("browser"); // or read from ITestContext if passed
		String testName = result.getMethod().getMethodName();
		if (browser != null && !browser.isEmpty()) {
			testName = testName + " [" + browser + "]";
		}
		ExtentTest t = extent.createTest(testName);
		test.set(t);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Test passed: " + result.getMethod().getMethodName());
		test.get().pass("Test passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Test failed: " + result.getMethod().getMethodName());
		test.get().fail("Test failed");
		test.get().fail(result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Test skipped: " + result.getMethod().getMethodName());
		test.get().skip("Test skipped");
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("ExtentListener.onFinish() - flushing reports");
		try {
			extent.flush(); // write timestamped file
		} catch (Throwable t) {
			System.err.println("ExtentListener: flush failed: " + t.getMessage());
		}
	}
}
