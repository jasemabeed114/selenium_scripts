import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Setup.Generic;
import Setup.GetDrivers;

public class POF extends Generic {

	static String blacklistfile = "PATH_TO_BLACKLIST_FILE";
	static String messagefile = "PATH_TO_MESSAGE_FILE;

	public static String pickMsg() throws FileNotFoundException {
		String message = null;
		@SuppressWarnings("resource")
		Scanner file = new Scanner(new File(messagefile));
		while (file.hasNextLine()) {
			String str = file.nextLine();
			// if the page doesn't contain the message
			if (driver.getPageSource().contains(str) == false) {
				// return this message
				message = str;
				break;
			}
		}
		return message;
	}

	// gets profile id from url
	public static String getID(String url) {
		String[] url1 = url.split("=");
		String[] url2 = url1[1].split("&guid");
		String url3 = url2[0];
		return url3;
	}

	// checks if id is in blacklist
	public static boolean blacklist(String id) throws FileNotFoundException {
		boolean isin = false;
		@SuppressWarnings("resource")
		Scanner file = new Scanner(new File(blacklistfile));
		while (file.hasNextLine()) {
			String str = file.nextLine();
			if (str.equals(id)) {
				isin = true;
				break;
			}
		}
		return isin;
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		System.out.println(blacklistfile);
		try {

			// Go to favorites list
			driver = GetDrivers.driver();
			
			// login to POF
			PofLogin();

			boolean isPresent = driver.findElements(By.linkText("View MSGs"))
					.size() > 0;

			// Loop 60 times
			for (int i = 0; i < 60; i++) {

				// while the favorites list is not empty
				while (isPresent == true) {

					driver.get("http://www.pof.com/friends.aspx");
					// View the messages I have sent to first profile on list

//					// refresh page
//					driver.navigate().refresh();

					linkClick("View MSGs");
					// wait for message box
					nameWait("message");

					// Get the profile id
					String url3 = getID(driver.getCurrentUrl());

					// if the current profile is not blacklisted
					if (blacklist(url3) == false) {

						// find a message that I have not sent this profile
						String new_msg = pickMsg();

						// if I have already sent all messages to this profile,
						// then
						// add profile to blacklist
						if (new_msg == null) {
							WriteToFile(blacklistfile, url3, true);

						} else {

							// Access the textbox
							WebElement box = nameElement("message");

							// Type this message in the box
							box.sendKeys(new_msg);

							// Click submit button
							WebElement submit = driver.findElement(By
									.name("sendmessage"));
							submit.submit();
						}

						// add profile to blacklist
						WriteToFile(blacklistfile, url3, true);
					}

					// go back to favorites list
					driver.get("http://www.pof.com/friends.aspx");

					// Remove profile from list
					driver.get("http://www.pof.com/friends.aspx?Deletefriendid="
							+ url3);

					waitID("grey-box");

					Thread.sleep(3000);

					// see if there is another profile on list
					isPresent = driver.findElements(By.linkText("View MSGs"))
							.size() > 0;
				}
			}
		} finally {
			driver.findElement(By.linkText("Log Out")).click();
			driver.quit();
		}
	}
}
