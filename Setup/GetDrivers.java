package Setup;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class GetDrivers {
	
	private final static String notepad = "C:\\Windows\\system32\\notepad.exe";
	private final static String driverPath="C:\\Selenium\\chromedriver.exe";
	
	public static WebDriver driver() {
		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver();		
		return driver;
	}

	public static void openTextFile(String filepath) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(notepad, filepath);
		pb.start();
	}

}