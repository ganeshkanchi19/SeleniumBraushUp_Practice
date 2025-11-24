package utils;

import java.io.File;

public class FileUtils {
	public static void cleanReportsFolder() {
		File folder = new File(System.getProperty("user.dir") + "/reports");

		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
	}
}
