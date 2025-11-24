package reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports createInstance() {
		if (extent != null) {
			return extent;
		}

		// String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String reportPath = System.getProperty("user.dir") + "/reports/extent-report.html";
		File f = new File(reportPath);
		f.getParentFile().mkdirs();

		System.out.println("Creating Extent Report at: " + reportPath);
		ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
		reporter.config().setDocumentTitle("Automation Report");
		reporter.config().setReportName("Selenium Test Report");

		extent = new ExtentReports();
		extent.attachReporter(reporter);
		return extent;
	}

}
