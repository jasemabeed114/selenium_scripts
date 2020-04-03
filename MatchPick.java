
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Setup.Generic;
import Setup.GetDrivers;

public class MatchPick extends Generic {

	public static void login() throws InterruptedException {
		
		String old_handle = "ctl00_workarea_loginPageView_ctl00_login_ctl00_tbxHandle";
		String old_pw = "ctl00_workarea_loginPageView_ctl00_login_ctl00_tbxPassword";
		String old_submit = "ctl00_workarea_loginPageView_ctl00_login_ctl00_btnLogin";

		String new_handle = "email";
		String new_pw = "password";
		String new_submit = "//*[@id='ng-app']/div[1]/div/div/form/fieldset[2]/button";
		
		String username="YOUR_USERNAME";
		String password="YOUR_PASSWORD";
		
		Thread.sleep(3000);
		
		if (driver.findElements(By.id(old_handle)).size() > 0) {
			driver.findElement(By.id(old_handle)).sendKeys(username);
		} else {
			Thread.sleep(3000);
			driver.findElement(By.id(new_handle)).sendKeys(username);
		}
		if (driver.findElements(By.id(old_pw)).size() > 0) {
			Thread.sleep(3000);
			driver.findElement(By.id(old_pw)).sendKeys(password);
		} else {
			Thread.sleep(3000);
			driver.findElement(By.id(new_pw)).sendKeys(password);
		}
		if (driver.findElements(By.id(old_submit)).size() > 0) {
			Thread.sleep(3000);
			driver.findElement(By.id(old_submit)).click();
		} 
		else {
			if (driver.findElements(By.xpath(new_submit)).size() > 0) {
				Thread.sleep(3000);
				driver.findElement(By.xpath(new_submit)).click();
			}
		}

	}
	
	public static void gofilterSearch() throws InterruptedException{
		//go to search page
		driver.get("http://www.match.com/SearchReskin/?st=Q&CLR=true&%3flid=2&");
		
		//if there are profiles present on page
		boolean isPresent = driver.findElements(By.className("card"))
				.size() > 0;
		if (isPresent == true) {	
			//filter by saved search
			driver.findElement(By.id("saved-search-dd")).sendKeys("Type1");
			Thread.sleep(3000);
			//sort by newest users
			driver.findElement(By.id("sortby-dd")).sendKeys("Newest First");
			Thread.sleep(3000);
		}
	}

	public static void main(String[] args) throws InterruptedException {

		// TODO login to match.com
		try {
			driver = GetDrivers.driver();
			driver.get("http://www.match.com/login/login.aspx?lid=1");
			login();

			Thread.sleep(5000);
			gofilterSearch();
			
			// pick 20 profiles
			for (int i=0;i<=20;i++) {
				
				//save first profile to favs list
				WebElement view = driver
						.findElement(By.xpath("//*[@id='form-search-results']/div[3]/div[2]/div/div/a[2]"));
				view.click();

				//go to back search page
				gofilterSearch();
				
				// TODO Click on x to remove profile from search
				Thread.sleep(2000);
				driver.findElement(
						By.xpath("//*[@id='form-search-results']/div[3]/div[2]/div/a"))
						.click();
				Thread.sleep(2000);


			}
		}

		finally {
			driver.get("http://www.match.com/login/logout.aspx?lid=3");
			driver.quit();
		}
	}
}
