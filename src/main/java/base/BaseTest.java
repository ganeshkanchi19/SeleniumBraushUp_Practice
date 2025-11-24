package base;

import utils.FileUtils;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
	@BeforeSuite
	public void cleanOldReports() {
		System.out.println(">>> BaseTest.cleanOldReports() – deleting old reports");
		FileUtils.cleanReportsFolder();
	}
}
