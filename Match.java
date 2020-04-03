import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;

import Setup.Generic;
import Setup.GetDrivers;

public class Match extends Generic {
	static int delay = 3000;
	static int retry = 3;
	static String messagefile = "PATH_TO_MESSAGES_FILE";
	static String blacklistfile = "PATH_TO_BLACKLIST_FILE";
	static String username = "YOUR_USERNAME";
	static String password = "YOUR_PASSWORD";

	public static void login() throws InterruptedException {

		String new_handle = "email";
		String new_pw = "password";
		String new_submit = "//*[@id='ng-app']/div[1]/div/div/form/fieldset[2]/button";

		waitID(new_handle);
		driver.findElement(By.id(new_handle)).sendKeys(username);

		waitID(new_pw);
		driver.findElement(By.id(new_pw)).sendKeys(password);

		xpathWait(new_submit);
		driver.findElement(By.xpath(new_submit)).click();

	}

	public static WebElement accessTextbox(String id) {
		waitID(id);
		WebElement element = driver.findElement(By.id(id));

		element.sendKeys(Keys.ESCAPE);
		return element;
	}

	public static String pickMsg() throws FileNotFoundException,
			InterruptedException {
		String message = null;
		@SuppressWarnings("resource")
		Scanner file = new Scanner(new File(messagefile));
		while (file.hasNextLine()) {
			String str = file.nextLine();
			if (classPresent("historyBody", delay, retry) == false) {
				message = str;
				break;
			} else {
				String tmp = driver.findElement(By.className("historyBody"))
						.getText();
				boolean x = tmp.contains(str);
				if (x == false) {
					message = str;
					break;
				}
			}

		}
		return message;
	}

	public static void main(String[] args) throws InterruptedException,
			FileNotFoundException {

		try {
			driver = GetDrivers.driver();
			driver.get("http://www.match.com/login/login.aspx?lid=1");
			login();
			Thread.sleep(delay);
			driver.get("http://www.match.com/interests/fave/1/");
			
			
			waitID("tab-cnt-fave");
			
			Thread.sleep(delay);
			driver.findElement(By.id("sb")).sendKeys("username");
			Thread.sleep(delay);

			
			boolean isPresent = true;
			// while the favorites list is not empty
			while (isPresent == true) {

				Thread.sleep(delay);
				isPresent = driver.findElements(By.className("card")).size() > 0;

				// Message first profile on list
				WebElement view = driver
						.findElement(By.className("card-email"));
				
				//if button is enabled
				String seeking = driver.findElement(By.className("seeking")).getText();
				String hidden = "Hidden Profile"; 
				
				boolean isdisabled = seeking.equals(hidden);
				
				if (isdisabled == false){
					view.click();
					// Get the first message that I have not yet sent this user
					String new_msg = pickMsg();

					// if there is a message I have not sent this user,
					if (new_msg != null) {
						Thread.sleep(delay);

						// Find the subject line
						WebElement subject = driver
								.findElement(By
										.xpath("//*[@id='ng-app']/div/div[2]/div/div/div[1]/div/form/div[2]/input"));

						// Type this subject into subject line
						subject.clear();
						subject.sendKeys("Please read my message!!!!!");

						// Find the textbox
						WebElement box = driver
								.findElement(By
										.xpath("//*[@id='ng-app']/div/div[2]/div/div/div[1]/div/form/div[2]/textarea"));

						// Type this message in the box
						box.clear();
						box.sendKeys(new_msg);

						// Find submit button
						WebElement submit = driver
								.findElement(By
										.xpath("//*[@id='ng-app']/div/div[2]/div/div/div[1]/div/form/div[3]/span[2]/button[2]"));

						// click submit button
						submit.click();
						
						//wait for confirmation
						xpathWait("//*[@id='ng-app']/div/div[2]/div/div/div[1]/div/div[4]/div[1]/h5");
						
						//go back to favs page
						driver.get("http://www.match.com/interests/fave/1/");
						
						//wait for favorites page
						waitID("bodyContent");
						
						//sort favorites by username
						driver.findElement(By.id("sb")).sendKeys("username");
					}
				}

				//  Click on x to remove profile from list
				Thread.sleep(delay);
				driver.findElement(
						By.xpath("//*[@id='tab-cnt-fave']/div[1]/div[2]/div/a"))
						.click();
				Thread.sleep(delay);
				driver.findElement(
						By.xpath("//*[@id='deleteConfirmation']/div/a[1]"))
						.click();
				Thread.sleep(delay);

				// see if there is another profile on list
				isPresent = driver.findElements(By.className("card")).size() > 0;
			}
		}

		finally {
			driver.get("http://www.match.com/login/logout.aspx?lid=3");
			driver.quit();
		}
	}
}
