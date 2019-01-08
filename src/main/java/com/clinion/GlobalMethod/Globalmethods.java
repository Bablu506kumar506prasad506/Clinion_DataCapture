package com.clinion.GlobalMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

import jxl.Sheet;
import jxl.Workbook;

public class Globalmethods {
	
	public static WebDriver driver;

	public static void LaunchBrowser(String browserName, String Url) {
		
		if (browserName.equals("firefox")) {
			System.setProperty("webdriver.firefox.driver",
					System.getProperty("user.dir") + "/src/main/resources/win/geckodriver.exe");
			driver = new FirefoxDriver();
		} else if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "/src/main/resources/win/chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browserName.equals("ie")) {
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "/src/main/resources/win/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}

		driver.manage().window().maximize();
		driver.get(Url);
	}

	//-------Data Manager login-------//
	public static void Datamanager_Login() throws Exception {

		FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
		Workbook wb = Workbook.getWorkbook(fi);
		Sheet r1 = wb.getSheet("LoginData");

		String UserName_Data = r1.getCell(1, 1).getContents();
		String Password_Data = r1.getCell(2, 1).getContents();

		driver.findElement(By.id("txtUserName")).sendKeys(UserName_Data);
		WebElement sas = driver.findElement(By.id("txtPassword"));
		sas.sendKeys(Password_Data);
		driver.findElement(By.xpath("//input[@type='submit']")).click();

	}
	
	//-------Investigator login-------//
		public static void Investigator_Login() throws Exception {

			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
			Workbook wb = Workbook.getWorkbook(fi);
			Sheet r1 = wb.getSheet("LoginData");

			String UserName_Data = r1.getCell(1, 2).getContents();
			String Password_Data = r1.getCell(2, 2).getContents();

			driver.findElement(By.id("txtUserName")).sendKeys(UserName_Data);
			WebElement sas = driver.findElement(By.id("txtPassword"));
			sas.sendKeys(Password_Data);
			driver.findElement(By.xpath("//input[@type='submit']")).click();

		}
		
		public static void CoInvestigator_Login() throws Exception {

			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
			Workbook wb = Workbook.getWorkbook(fi);
			Sheet r1 = wb.getSheet("LoginData");

			String UserName_Data = r1.getCell(1, 3).getContents();
			String Password_Data = r1.getCell(2, 3).getContents();

			driver.findElement(By.id("txtUserName")).sendKeys(UserName_Data);
			WebElement sas = driver.findElement(By.id("txtPassword"));
			sas.sendKeys(Password_Data);
			driver.findElement(By.xpath("//input[@type='submit']")).click();

		}
		
		public static void Datamanager_Login_Client1() throws Exception {

			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
			Workbook wb = Workbook.getWorkbook(fi);
			Sheet r1 = wb.getSheet("LoginData");

			String UserName_Data = r1.getCell(1, 3).getContents();
			String Password_Data = r1.getCell(2, 3).getContents();

			driver.findElement(By.id("txtUserName")).sendKeys("ritika");
			WebElement sas = driver.findElement(By.id("txtPassword"));
			sas.sendKeys("Test@1234");
			driver.findElement(By.xpath("//input[@type='submit']")).click();

		}
		
		public static void FP_Login_Client1() throws Exception {

			driver.findElement(By.id("txtUserName")).sendKeys("investigator");
			WebElement sas = driver.findElement(By.id("txtPassword"));
			sas.sendKeys("Test@123");
			driver.findElement(By.xpath("//input[@type='submit']")).click();

		}
		
		public static void SP_Login_Client1() throws Exception {

			driver.findElement(By.id("txtUserName")).sendKeys("girish");
			WebElement sas = driver.findElement(By.id("txtPassword"));
			sas.sendKeys("Test@123");
			driver.findElement(By.xpath("//input[@type='submit']")).click();

		}
		
		//------Electronic signature-----//
		public static void electronicSign() throws Exception {
			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
			Workbook wb = Workbook.getWorkbook(fi);
			Sheet r1 = wb.getSheet("LoginData");
			
			String Password_Data = r1.getCell(2, 2).getContents();
			
			driver.findElement(By.id("txtPwd")).sendKeys(Password_Data);
			driver.findElement(By.id("btnSaveSign")).click();
			
		}
		
		public static void alertaccept() throws Exception {

			Alert al = driver.switchTo().alert();
			String msgalert = al.getText();
			al.accept();

		}
		public static void isAlertPresent() throws Exception {

			try {
				driver.switchTo().alert();
				System.out.println(" Alert Present");
				alertaccept();
			} catch (NoAlertPresentException e) {
				System.out.println("No Alert Present");
			}
		}
		public static void QueriesPopup() {
			try {
				if (driver.findElement(By.cssSelector("#popup_ok")).isDisplayed()) {
					
						driver.findElement(By.cssSelector("#popup_ok")).click();
					
					
				}else {
					System.out.println("No Alert Present");
				}
				
			} catch (NoAlertPresentException e) {
				System.out.println("No Alert Present");
			}
		}
		
		public static void scrollToElement(WebElement element) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
		}

}
