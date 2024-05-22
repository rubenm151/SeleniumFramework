package rahulshettyacademy.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import rahulshettyacademy.resources.ExtentReporterNG;

public class Listeners extends BaseTest implements ITestListener {
	ExtentTest test = null;
	ExtentReports extent = ExtentReporterNG.getReportObject();
	ThreadLocal<ExtentTest> extentThread = new ThreadLocal<ExtentTest>();

	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		extentThread.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		extentThread.get().log(Status.PASS, "All good");

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// Log the failure
		extentThread.get().fail(result.getThrowable());
		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String filePath = null;

		try {
			// Capture the screenshot
			filePath = getScreenshot(result.getMethod().getMethodName(), driver);
			// Add the screenshot to the report
			test.addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
		} catch (IOException e) {
			// Print the stack trace if an exception occurs while taking the screenshot
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, "All good");

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {

		extent.flush();

	}

}
