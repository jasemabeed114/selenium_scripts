package Personal;
import java.io.IOException;
import java.sql.Timestamp;

import org.openqa.selenium.By;

import Setup.Generic;
import Setup.GetDrivers;

public class TMobile extends Generic {
	
	private final static String google_userid = "USER_ID";
	private final static String google_pwd = "PASSWORD";
	private final static String google_voice_url = "https://www.google.com/voice";
	private final static String phone_number = "PHONE_NUMBER";
	private final static String pwd = "GV_PASSWORD";
	private final static String URL = "https://my.t-mobile.com/Login/?rc=&dest=https%3a%2f%2fmy.t-mobile.com%3a443%2fdefault.aspx";
	private final static String filePath = "FILE_TO_STORE_MINUTES_DATA";
	
	// Login to my account
	public static void login() {
		driver.get(URL);
		idEdit("msisdn", phone_number);
		idEdit("password", pwd);
		idClick("primary_button");
	}
	
	// Go to Google Voice and send text to self
	public static void sendsms(String message, String phone_number) throws InterruptedException{
		driver.get(google_voice_url);
		GoogleLogin(google_userid, google_pwd);
		
		if (driver.getPageSource().contains(message) == false) {

			// click on text button
			driver.findElement(
					By.xpath("//*[@id='gc-sidebar-jfk-container']/div/div[1]/div[2]"))
					.click();

			// enter number in to field
			idEdit("gc-quicksms-number", phone_number);
			idEdit("gc-quicksms-text2", message);
			Thread.sleep(3000);
			
			driver.findElement(
					By.xpath("//*[@id='gc-quicksms-send2']/div/div/div/div[2]"))
					.click();
			
			Thread.sleep(3000);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		try {

			// login
			driver = GetDrivers.driver();
			login();

			// Wait for current usage info to appear		
			idWait("myAccountUseBody");		
			//minutes usage value
			classWait("datausage-mod-desc");	
			//data usage value
			classWait("home-txt20");
			//account status value
			idWait("pp_paymentstatus");
			//next payment amount value
			idWait("pp_nextChargeAmountpaid");
			//next charge date value
			idWait("pp_nextplandate");
			
			// print minutes used and minutes left
			java.util.Date date = new java.util.Date();
			Timestamp x = new Timestamp(date.getTime());

			String timestamp = (x + "\n");
			String usage = "\n"
					+ driver.findElement(By.id("family-usage")).getText()+ "\n";
			String account = "\n"
					+ driver.findElement(By.id("myaccount-module")).getText();
			WriteToFile(filePath, timestamp + usage + account, false);
		
			sendsms(usage, phone_number);

		} finally {
			driver.get("https://my.t-mobile.com/logout.aspx");
			driver.close();
			driver.quit();
			GetDrivers.openTextFile(filePath);

			
		}
	}
}
