import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import Setup.Generic;
import Setup.GetDrivers;

public class OpenLabel extends Generic {
    // Manual requirements:
    // - You put emails from job recruiters into a labeled folder called "AutoReply"
    // - You have a template file detailing your job criteria that you want
    // Automation:
    // - This script will reply to each email in the labeled folder with the job criteria from the template, then delete the email.
    
	private final static String userid = "YOUR_USERNAME";
	private final static String pwd = "YOUR_PASSWORD";
	private final static String baseURL = "https://mail.google.com/";
	private final static String templatePath = "JOB_CRITERIA_TEMPLATE";

	// Login to my gmail account
	public static void login() {
		driver = GetDrivers.driver();
		driver.get(baseURL);
		idEdit("Email", userid);
		idEdit("Passwd", pwd);
		idClick("signIn");
	}

	public static void classEnter(String classname) {
		waitClass(classname);
		driver.findElement(By.className(classname)).sendKeys(Keys.ENTER);
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		try{
		login();
		// go to autoreply folder
		driver.get("https://mail.google.com/mail/u/0/#label/AutoReply");
		driver.navigate().refresh();
		Thread.sleep(3000);
		while (driver
				.findElements(By.xpath("//*[starts-with(@id, ':')]/td[5]"))
				.size() > 0) {
			// go to first message
			xpathClick("//*[starts-with(@id, ':')]/td[5]");

			String tmp = "/html/body/div[5]/div[2]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div/div/div[2]/table/tr/td/div[2]/div[2]/div/div[3]/div/div/div/div/div/div[2]/div/div/div/table/tbody/tr/td[2]/div/div";

			// reply to message
			xpathClick("/html/body/div[5]/div[2]/div/div[2]/div/div[2]/div/div/div/div[2]/div/div/div/div/div/div[2]/table/tr/td/div[2]/div[2]/div/div[3]/div/div/div/div/div/div[2]/div/div/div/table/tbody/tr/td[2]/div/div");

			// get remail auto-reply
			String email = readFile(templatePath);
			xpathElement("//div[contains(@class, 'editable')]").sendKeys(
					email + Keys.TAB + Keys.ENTER + "y");
			Thread.sleep(3000);
	
			if (driver.findElements(By.xpath(tmp)).size() > 0) {
				xpathElement("//div[contains(@class, 'amn')]").sendKeys("y");
			}

		}
		}
		finally { 
			driver.get("https://mail.google.com/mail/u/0/?logout&hl=en&hlor");
			driver.quit();
		}
	}
}
