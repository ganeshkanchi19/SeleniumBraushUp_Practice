package reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * Simple ExtentManager compatible with older ExtentListener usage:
 * - creates (or reuses) a single ExtentReports instance
 * - generates report at ./reports/extent-report.html
 */
public class ExtentManager {

    private static ExtentReports extent;

    // Stable path used by Jenkins / CI / bookmarks
    private static final String REPORT_PATH = System.getProperty("user.dir") + File.separator + "reports"
            + File.separator + "extent-report.html";

    public static synchronized ExtentReports createInstance() {
        if (extent != null) {
            return extent;
        }

        try {
            // Ensure reports folder exists
            File reportFile = new File(REPORT_PATH);
            File parent = reportFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            System.out.println("Creating Extent Report at: " + reportFile.getAbsolutePath());
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFile);
            spark.config().setDocumentTitle("Automation Report");
            spark.config().setReportName("Selenium Test Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        } catch (Exception e) {
            System.err.println("ExtentManager.createInstance(): failed to create reporter: " + e.getMessage());
            e.printStackTrace();
            // still create a fallback minimal ExtentReports to avoid NPEs
            extent = new ExtentReports();
        }

        return extent;
    }
}
