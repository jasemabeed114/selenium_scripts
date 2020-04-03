package Personal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Setup.Generic;
import Setup.GetDrivers;

public class Facebook extends Generic {

	public final static BufferedReader br = new BufferedReader(
			new InputStreamReader(System.in));
	private static int lineNumber = 0;
	public static String status = "";
	private final static String userid = "USER_ID";
	private final static String pwd = "PASSWORD";
	private final static String URL = "https://www.facebook.com//login";
	private final static String filePath = "FILE_WITH_STATUSES";
	public static String getStatus() throws IOException {

		// This will reference one line at a time
		String line = "";

		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader(filePath);

		@SuppressWarnings("resource")
		Scanner fileIn = new Scanner(new FileReader(filePath));
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		// while the next line is not blank
		while ((line = bufferedReader.readLine()) != null) {
			// append line to status string
			status += line;
			// Move to next line
			lineNumber = +1;
			if ((fileIn.nextLine()).isEmpty())
				break;
		}

		// Always close files.
		bufferedReader.close();
		return status;
	}

	public static void removeStatus() throws IOException {

		FileReader fileReader = new FileReader(filePath);
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		for (int i = 0; i <= lineNumber; i++) {
			RemoveFirstLine(filePath);
		}
		// Always close files.
		bufferedReader.close();
	}

	public static void idEdit(String id, String input) {
		idWait(id);
		WebElement element = driver.findElement(By.id(id));
		element.clear();
		element.sendKeys(input);
	}

	public static void nameEdit(String name, String input) {
		nameClick(name);
		WebElement element = driver.findElement(By.name(name));
		element.clear();
		element.sendKeys(input);
	}

	public static boolean txtExist(String txt) {
		return driver.getPageSource().contains(txt);
	}

	// Login to my account
	public static void login() {
		idEdit("email", userid);
		idEdit("pass", pwd);
		idClick("u_0_1");
	}

	public static void main(String[] args) throws NullPointerException,
			IOException {

		status = getStatus();
		driver = GetDrivers.driver();
		driver.get(URL);

		try {
			if (status.isEmpty() == false && status != null) {
				// login to facebook.com

				login();

				// click on status box
				idClick("u_0_1f");

				// Get timestamp
				java.util.Date date = new java.util.Date();
				Timestamp x = new Timestamp(date.getTime());
				
				// input status in status box
				nameEdit("xhpc_message_text", status + "\n-Posted on " + x);

				// click post button
				xpathClick("//*[@id='u_0_1f']/div/div[4]/div/ul/li[3]/button");
				
				if (txtExist(status) == true) {
					System.out.println("Status has been posted.");
					removeStatus();
				} else {
					System.out.println("Error!");
				}
			}
			else{
				System.out.println("No status to post!");
			}
		} finally {
			driver.quit();
		}
	}
}
