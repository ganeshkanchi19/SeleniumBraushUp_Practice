package reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * ExtentManager: creates a timestamped Extent report and keeps track of the
 * generated path. Also provides helper to copy the generated report to a
 * stable file path (extent-report.html).
 */
public class ExtentManager {
    private static ExtentReports extent;
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // The path of the most recently generated (timestamped) report
    private static String generatedReportPath = null;

    // Stable path that external tools / bookmarks can open:
    // this file will be overwritten each run with the latest generated report
    private static final String stableReportPath = System.getProperty("user.dir") + "/reports/extent-report.html";

    public static synchronized ExtentReports createInstance() {
        if (extent != null) {
            return extent;
        }

        String ts = LocalDateTime.now().format(TF);
        String reportPath = System.getProperty("user.dir") + "/reports/extent-report_" + ts + ".html";

        // ensure folder exists
        File f = new File(reportPath);
        f.getParentFile().mkdirs();

        // store generated path for later retrieval/copy
        generatedReportPath = f.getAbsolutePath();

        System.out.println("Creating Extent Report at: " + generatedReportPath);

        ExtentSparkReporter reporter = new ExtentSparkReporter(generatedReportPath);
        reporter.config().setDocumentTitle("Automation Report");
        reporter.config().setReportName("Selenium Test Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        return extent;
    }

    /**
     * Return the generated (timestamped) report path for logging/verification.
     */
    public static String getReportFilePath() {
        return generatedReportPath;
    }

    /**
     * Copy the most-recent generated report to the stable file path
     * (overwrites existing stable file). Call this after extent.flush().
     */
    public static void copyToStableFile() {
        if (generatedReportPath == null) {
            // nothing to copy; caller should call createInstance() first
            System.err.println("ExtentManager.copyToStableFile(): no generated report found to copy.");
            return;
        }

        File src = new File(generatedReportPath);
        File dst = new File(stableReportPath);

        try {
            // ensure parent folder exists
            if (dst.getParentFile() != null) {
                dst.getParentFile().mkdirs();
            }

            // copy (replace existing)
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied report to stable path: " + dst.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to copy report to stable path: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
