import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import Setup.Generic;
import Setup.GetDrivers;

public class Indeed extends Generic {

	private final static String userid = "USER_ID";
	private final static String pwd = "PASSWORD";
	private final static String URL = "https://secure.indeed.com//account//login?service=my&hl=en_US&co=US&continue=http%3A%2F%2Fwww.indeed.com%2F&xxfb=1";
	private final static String summaryPath = "C:\\Users\\eichinose\\Automation\\iSummaryt.txt";
	private final static String experience1Path = "PATH_TO_EXPERIENCE1_FILE";
	private final static String experience2Path = "PATH_TO_EXPERIENCE2_FILE";

	static int lineNumber = 0;

	public static void login() {
		driver.get(URL);
		idEdit("signin_email", userid);
		idEdit("signin_password", pwd);
		classClick("input_submit");
	}

	public static String getContent(String path) throws IOException {

		String content = null;
		// This will reference one line at a time
		String line = "";

		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader(path);

		@SuppressWarnings("resource")
		Scanner fileIn = new Scanner(new FileReader(path));
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		// while the next line is not blank
		while ((line = bufferedReader.readLine()) != null) {
			// append line to status string
			if (lineNumber < 1) {
				content += line; 
			} else {
				content += "\n" + line;
			}
			// Move to next line
			lineNumber = +1;
			if ((fileIn.nextLine()).isEmpty())
				break;
		}

		// Always close files.
		bufferedReader.close();
		return content;
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		try {

			driver = GetDrivers.driver();

			// login to account
			login();

			// go to resume page
			driver.get("http://www.indeed.com/promo/resume");

			// click edit button
			idClick("edit_rez_button");

			// ***********************************************************************************************//
			// click edit summary
			xpathClick("//*[@id='basic_info_row']/div[1]/span");

			// edit summary
			String summary = getContent(summaryPath);
			idEdit("summary", summary.substring(4));

			// save change
			classClick("save");

			// ***********************************************************************************************//
			// click edit we
			xpathClick("//*[starts-with(@id, 'workExperience-')]/div[1]/span[1]");

			// edit experience
			String experience1 = getContent(experience1Path);
			xpathEdit("//*[starts-with(@id,'we-description-')]",
					experience1.substring(4));

			// save change
			xpathClick("//*[starts-with(@id,'workExperience-')]/div/form/div[2]/div[6]/input");

			// ***********************************************************************************************//
			// click edit we
			xpathClick("// *[@id='workExperience-EeNAaNmMPluFsr9oLXgqwg']/div[1]/span[1]");

			// edit experience
			String experience2 = getContent(experience2Path);
			idEdit("we-description-EeNAaNmMPluFsr9oLXgqwg",
					experience2.substring(4));

			// save change
			xpathClick("//*[@id='workExperience-EeNAaNmMPluFsr9oLXgqwg']/div/form/div[2]/div[6]/input");

		} finally {
			// Quit driver
			driver.quit();
		}
	}
}
