package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import reports.ExtentManager;

public class ExtentListener implements ITestListener {
	// create instance when the class is loaded
	private static ExtentReports extent = ExtentManager.createInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Override
	public void onStart(ITestContext context) {
		System.out.println(">>> ExtentListener.onStart() - TestNG context starting: " + context.getName());
	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println(">>> Test started: " + result.getMethod().getMethodName());
		ExtentTest t = extent.createTest(result.getMethod().getMethodName());
		test.set(t);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println(">>> Test passed: " + result.getMethod().getMethodName());
		test.get().pass("Test passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println(">>> Test failed: " + result.getMethod().getMethodName());
		test.get().fail("Test failed");
		test.get().fail(result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println(">>> Test skipped: " + result.getMethod().getMethodName());
		test.get().skip("Test skipped");
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println(">>> ExtentListener.onFinish() - flushing reports");
		extent.flush();
	}
}
