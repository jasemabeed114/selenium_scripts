import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Setup.GetDrivers;

public class Monster extends GetDrivers {

	public static WebDriver driver=null;
	private final static String userid = "YOUR_USERNAME;
	private final static String pwd = "YOUR_PASSWORD";
	private final static String URL = "http://my.monster.com/resume/createresume/?landedFrom=Header";
	private final static String resumePath = "PATH_TO_YOUR_RESUME";
    private final static String ZIP_NAME = "ZIP CITY, STATE";
    private final static String JOB_TITLE = "YOUR JOB TITLE";
    private final static String JOB_TITLE = "YOUR RESUME TITLE";
    private final static String SALARY_MIN = "YOUR_MIN_SALARY";
    private final static String SALARY_MAX = "YOUR_MAX_SALARY";
    
	//new File("Resume.docx").getAbsolutePath();
	
	public static void login() {
		driver.get(URL);
		idEdit("EmailAddress", userid);
		idEdit("Password", pwd);
		driver.findElement(By.className("formGS")).submit();
	}

	public static void idEdit(String id, String input) {
		WebElement element = driver.findElement(By.id(id));
		element.clear();
		element.sendKeys(input);
	}

	public static WebElement idElement(String id) {
		return driver.findElement(By.id(id));
	}

	public static void idClick(String id) {
		driver.findElement(By.id(id)).click();
	}

	public static void xpathClick(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}

	public static void linkClick(String linktxt) {
		driver.findElement(By.linkText(linktxt)).click();
	}

	public static boolean linkExist(String linktxt) {
		return driver.findElements(By.linkText(linktxt)).size() > 0;
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			driver=GetDrivers.driver();
			// go to create resume page
			login();
			if (linkExist("Skip this Offer") == true) {
				linkClick("Skip this Offer");
			}

			// Enter resume title
			idEdit("Title", RESUME_TITLE);

			// Enter job title
			idEdit("JobTitle", JOB_TITLE);

			// browse for file
			idElement("FileToUpload").sendKeys(resumePath);

			// Click Submit Button
			xpathClick("//*[@id='form0']/div[3]/fieldset[2]/input[1]");

			// Enter resume preferences
			idEdit("UserEnteredZipName", ZIP_NAME);
			idEdit("SalaryMin", SALARY_MIN);
			idEdit("SalaryMax", SALARY_MAX);
			idClick("WillingToRelocate1");
			idClick("WantsPermanent");
			idClick("WantsFullTime");

			// Submit preferences
			xpathClick("//*[@id='form0']/div/fieldset/a");
		} finally {	
			//logout
			driver.get("http://my.monster.com/Login/SignOut");
			}
			// Quit driver
			driver.quit();
		}
	}
