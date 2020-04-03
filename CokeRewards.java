package Personal;

import java.io.IOException;

import org.openqa.selenium.By;
import Setup.Generic;
import Setup.GetDrivers;

public class CokeRewards extends Generic {

	private final static String userid = "USER_ID";
	private final static String pwd = "PASSWORD";
	private final static String URL = "http://www.mycokerewards.com//home.do";
	private final static String filePath = "PATH_TO_CODES";

	// xpath of button that closes error window
	public final static String xErrPop = "//*[@id='codeErrorPop']/div[2]/div[1]/a/img";

	// xpath of button that closes popup survey window
	public final static String xmedPop = "//*[@id='mediumPopHandleBeta']/a/img";

	public static String myPoints;

	// Login to my account
	public static void login() {
		driver.get(URL);
		idEdit("emailAddress", userid);
		idEdit("passwordText", pwd);
		driver.findElement(By.id("loginSubmitBtn")).click();
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		try {
			// login
			driver = GetDrivers.driver();
			login();

			// wait for and get original number of points in account
			idWait("glPointsText");
			String oldPoints = (driver.findElement(By.id("glPointsText")))
					.getText();

			// close med popup box if exists
			if (driver.findElements(By.xpath(xmedPop)).size() > 0) {
				xpathClick(xmedPop);
			}

			// remove first line (containing points) of file
			RemoveFirstLine(filePath);

			// loop through each line in file and enter codes
			for (int i = 0; i <= CountLines(filePath); i++) {

				// if there is a code on the first line, save the code
				String code = readFirstLine(filePath);
				if (code != null) {
					// TODO get this to print in cmd
					System.out.println("code=" + code);
					// input code
					idEdit("rewardCode", code);

					// click enter code button
					idClick("enterCodeBtn");

				}

				// close error popup if exists
				if (driver.findElements(By.xpath(xErrPop)).size() > 0) {
					// close the popup window
					xpathClick(xErrPop);
				} else {
					String newPoints = (driver.findElement(By
							.id("glPointsText"))).getText();
					//if points have been added
					if (newPoints!=oldPoints) {
						// remove the code that has been entered
						RemoveFirstLine(filePath);
						myPoints=newPoints;
					}
				}
			}

			// print the current number of points at the top of the file
			AddFirstLine(filePath, myPoints + " points");

		} finally {
			if (driver.findElements(By.id("simplemodal-container")).size() > 0) {
				xpathClick("//*[@id='mediumPopHandleBeta']/a/img");
			}
			linkClick("Sign Out");
			driver.quit();
			GetDrivers.openTextFile(filePath);
		}
	}
}
