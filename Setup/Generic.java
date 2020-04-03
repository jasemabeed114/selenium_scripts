package Setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Generic {
	
	public static WebDriver driver = null;

	public static void waitClass(String classname) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.className(classname)));
	}

	public static boolean classPresent(String classname, int delay, int retry)
			throws InterruptedException {
		boolean isPresent = false;
		for (int i = 0; i < retry; i++) {
			Thread.sleep(delay);
			isPresent = driver.findElements(By.className(classname)).size() > 0;
			if (isPresent == true) {
				break;
			}
		}
		return isPresent;
	}

	public static void clickLink(String link) {
		waitLink(link);
		WebElement element = driver.findElement(By.linkText(link));
		element.click();
	}

	public static void idWait(String id) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}

	public static void classWait(String className) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.className(className)));
	}

	public static void nameWait(String name) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
	}

	public static void nameEdit(String name, String input) {
		nameWait(name);
		WebElement element = driver.findElement(By.name(name));
		element.clear();
		element.sendKeys("");
		element.sendKeys(input);
	}

	public static void idEdit(String id, String input) {
		idWait(id);
		WebElement element = driver.findElement(By.id(id));
		element.clear();
		element.sendKeys("");
		element.sendKeys(input);
	}

	public static void xpathEdit(String xpath, String input) {
		xpathWait(xpath);
		WebElement element = driver.findElement(By.xpath(xpath));
		element.clear();
		element.sendKeys(input);
	}

	public static void idClick(String id) {
		idWait(id);
		driver.findElement(By.id(id)).click();
	}

	public static void waitLink(String link) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.linkText(link)));
	}

	public static void xpathWait(String xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(xpath)));
	}

	public static void linkClick(String link) {
		waitLink(link);
		WebElement element = driver.findElement(By.linkText(link));
		element.click();
	}

	public static WebElement partiallinkClick(String link) {
		waitLink(link);
		WebElement element = driver.findElement(By.linkText(link));
		element.sendKeys(Keys.ESCAPE);
		return element;
	}
	
	public static WebElement nameElement(String name){
		return driver.findElement(By.name(name));
	}
	
	public static void nameClick(String name) {
		WebElement element = driver.findElement(By.name(name));
		element.click();
	}

	public static void waitID(String id) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}
	public static boolean classExist(String classname){
		if (driver.findElements(By.className(classname)).size() > 0) {
			return true;}
		else{
			return false;
			}
		}
	
	public static boolean linkExist(String linktext){
		if (driver.findElements(By.partialLinkText(linktext)).size() > 0) {
			return true;}
		else{
			return false;
			}
		}
	public static void classClick(String classname) {
		classWait(classname);
		WebElement element = driver.findElement(By.className(classname));
		element.click();
	}

	public static void xpathClick(String xpath) {
		xpathWait(xpath);
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
	}

	public static WebElement xpathElement(String xpath) {
		xpathWait(xpath);
		WebElement element = driver.findElement(By.xpath(xpath));
		return element;
	}

	public static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public static String readFirstLine(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		try {
			line = br.readLine();
		} finally {
			br.close();
		}
		return line;
	}

	public static void WriteToFile(String file, String line, boolean append)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));

		String[] words = line.split("\n");
		for (String word : words) {
			writer.write(word);
			writer.newLine();
		}

		writer.close();
	}

	public static int CountLines(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		int lines = 0;
		while (reader.readLine() != null)
			lines++;
		reader.close();
		return lines;
	}

	public static void RemoveFirstLine(String filePath) throws IOException {
		File f = new File(filePath);
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		long writePos = raf.getFilePointer();
		raf.readLine();
		long readPos = raf.getFilePointer();

		byte[] buf = new byte[1024];
		int n;
		while (-1 != (n = raf.read(buf))) {
			raf.seek(writePos);
			raf.write(buf, 0, n);
			readPos += n;
			writePos += n;
			raf.seek(readPos);
		}

		raf.setLength(writePos);
		raf.close();
	}

	public static void AddFirstLine(String filePath, String line)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(new File(filePath), "rw");
		f.seek(0); // to the beginning
		f.write(line.getBytes());
		f.close();
	}
	
	// Login to my google account
	public static void GoogleLogin(String userid, String pwd ) {
			idEdit("Email", userid);
			idClick("next");
			idEdit("Passwd", pwd);
			idClick("signIn");
			}

	// login to pof
	public static void PofLogin() throws InterruptedException {
			driver.get("http://www.pof.com/friends.aspx");
			Thread.sleep(3000);
			driver.findElement(By.name("username")).sendKeys(pof_username);
			driver.findElement(By.name("password")).sendKeys(pof_password);
			WebElement login = driver.findElement(By
					.id("logincontrol_submitbutton"));
			login.click();
		}
}
