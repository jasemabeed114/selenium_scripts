
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Setup.Generic;
import Setup.GetDrivers;

public class POFPick extends Generic {

	public static String[] urlArray = new String[20];
	public static String profileXP;
	public static String tmp1;
	public static String tmp2;
	public static String tmp3;
	public final static String[] msgs = new String[5];
		
	// Returns the URL from a xpath Link
	public static String xPathtoURL(String xPath) {
		WebElement element = driver.findElement(By.xpath(xPath));
		String profileURL = element.getAttribute("href");
		return profileURL;
	}

	public static String getID(String url) {
		String[] url1 = url.split("=");
		String[] url2 = url1[1].split("&guid");
		String url3 = url2[0];
		return url3;
	}

	// *[@id="searchresults"]/div[4]/div/div[2]/div[2]/a
	public static void main(String[] args) throws InterruptedException {

		try {
			driver = GetDrivers.driver();
			driver.get("http://www.pof.com/start.aspx");
			// login to POF
			PofLogin();

				driver.get("http://www.pof.com/advancedsearch.aspx?" + "iama=m"
						+ "&sorting=1" + "&seekinga=f" + "&wantchildren="
						+ "&MinAge=21" + "&MaxAge=23" + "&smoke=1"
						+ "&country=1" + "&drugs=1" + "&City=91775"
						+ "&miles=20" + "&interests=" + "&state=11"
						+ "&viewtype=1" + "&height=" + "&drink="
						+ "&heightb=160" + "&haschildren="
						+ "&thnicitymult=4&thnicitymult=6&thnicitymult=10&"
						+ "body=1&body=3" + "&maritalstatus=1" + "&page="
						+ "1");

				// Assume there are 20 elements on page
				for (int i = 2; i < 22; i++) {

					// Make the xpath
					// //*[@id="searchresults"]/div[#]/div/div[2]/div[2]/a
					tmp1 = "//*[@id='searchresults']/div[";
					tmp2 = Integer.toString(i);
					tmp3 = "]/div/div[2]/div[2]/a";
					profileXP = tmp1 + tmp2 + tmp3;

					boolean isPresent = driver
							.findElements(By.xpath(profileXP)).size() > 0;
					if (isPresent == true) {
						// Convert the xpath to url & add it to the url array
						urlArray[i - 2] = xPathtoURL(profileXP);
					}
				}
				// for each element in the url array go to url and click the
				// "Add to Favorites" button
				for (int i = 0; i < 20; i++) {
					if (urlArray[i] != null) {
						driver.get(urlArray[i]);
						driver.findElement(By.linkText("Add to Favorites!"))
								.click();
						System.out.println("Added to favorites :)");
					}
				}
		} finally {
			driver.quit();
		}
	}
}
