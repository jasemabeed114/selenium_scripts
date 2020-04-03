
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import Setup.Generic;
import Setup.GetDrivers;

public class Ebay extends Generic {

	private final static String userid = "YOUR_USERID";
	private final static String pwd = "YOUR_PASSWORD";
	private final static long msec = 1750;
	private final static String baseURL = "http://my.ebay.com";

	// Login to my ebay account
	public static void login() {
		driver.get("https://signin.ebay.com");
		waitID("userid");
		driver.findElement(By.id("userid")).sendKeys(userid);
		waitID("pass");
		WebElement password_field = driver.findElement(By.id("pass"));
		password_field.sendKeys(pwd);
		password_field.sendKeys(Keys.ENTER);

		}
//		driver.findElement(By.id("sgnBt")).click();

	public static void main(String[] args) throws InterruptedException {
		try {
			driver = GetDrivers.driver();
			// login to ebay.com
			login();
			Thread.sleep(msec);
			while(classExist("gh-ug-guest")==true){
				login();
			}
			// go to unsold items page
			driver.get(baseURL
					+ "SubmitAction.ChangeRelist=x&View=UnsoldNext&Column=Unknown&CurrentPage=MyeBayNextUnsold&RelistFilter=NotRelisted&ssPageName==STRK:MEUSX:ITEMSNOTRELISTED");
			// filter unsold period
			driver.get(baseURL
					+ "NewPeriod=Custom&SubmitAction.ChangePeriod=x&tagId=0&View=UnsoldNext&UnsoldNext.Filter=All&Column=Unknown&CurrentPage=MyeBayNextUnsold&ssPageName==STRK:MEUSX:CUSTPER");

			Thread.sleep(msec);

			// check if there are unsold items present
			boolean isPresent = driver.findElements(By.name("LineID")).size() > 0;

			// while there are items to be relisted
			while (isPresent == true) {

				// click on relist checkbox for first item on list
				WebElement relist = driver.findElement(By.name("LineID"));
				relist.click();

				// click on relist button for first item on list
				WebElement submitRelist = driver.findElement(By
						.name("SubmitAction.BulkRelist"));
				submitRelist.click();
				Thread.sleep(msec);
				
				Thread.sleep(msec);
				if (linkExist("Continue listing without using our catalog")==true){
					linkClick("Continue listing without using our catalog");
				}
				
				Thread.sleep(msec);
				
				if (driver.findElements(By.id("aidZ1_btnFrm")).size() > 0 == true) {
					driver.findElement(By.id("aidZ1_btnFrm")).click();
				}
				
				// wait for relist popup windowitem page submit button
				waitClass("btnMain");
				if (driver.findElements(By.id("totalInsertionFeesParam")).size() > 0 == true
						) {
					WebElement total = driver.findElement(By
							.id("totalInsertionFeesParam"));
					Double x = Double.parseDouble(total.getAttribute("value"));
					if (x > 0.00) {
						break;
					}

				}
				
				// relist the item
				WebElement submit = driver.findElement(By.className("btn"));
				String tmp=driver.findElement(By.xpath("//*[@id='layer_fee_summary_sec']/div/table/tbody/tr[4]/td[2]/b")).getText();
				if ( tmp.equals("$0.00")) {
					submit.click();
				}

				// Wait until success page is shown
				waitLink("Relist another item");
				System.out.println("Item relisted");
				// go to unsold items page
				driver.get(baseURL
						+ "CurrentPage=MyeBayNextUnsold&ssPageName=STRK:ME:LNLK:MEUSX");

				// go to unsold items page
				driver.get(baseURL
						+ "SubmitAction.ChangeRelist=x&View=UnsoldNext&Column=Unknown&CurrentPage=MyeBayNextUnsold&RelistFilter=NotRelisted&ssPageName==STRK:MEUSX:ITEMSNOTRELISTED");
				// filter unsold period
				driver.get(baseURL
						+ "NewPeriod=Custom&SubmitAction.ChangePeriod=x&tagId=0&View=UnsoldNext&UnsoldNext.Filter=All&Column=Unknown&CurrentPage=MyeBayNextUnsold&ssPageName==STRK:MEUSX:CUSTPER");

				Thread.sleep(msec);

				// check if there are unsold items present
				isPresent = driver.findElements(By.name("LineID")).size() > 0;

			}
		} finally {
			driver.get("https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&lgout=1");
			driver.quit();
		}
	}
}
