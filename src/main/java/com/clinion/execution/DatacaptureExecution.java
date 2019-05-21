package com.clinion.execution;

import java.io.FileInputStream;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.clinion.GlobalMethod.Globalmethods;
import com.clinion.Scenarios.ClinionDataVerification;
import com.clinion.Scenarios.CreateSubject;
import com.clinion.Scenarios.Datacapture;
import com.clinion.Scenarios.ExtractDataFromCRFtoExcel;

import jxl.Sheet;
import jxl.Workbook;

@Listeners(Listener_Demo.ListenerTest.class)

public class DatacaptureExecution {

	@BeforeMethod
	public void beforeMethod() throws Exception, Exception {

		FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
		Workbook wb = Workbook.getWorkbook(fi);
		Sheet r1 = wb.getSheet("Login");

		String URL = r1.getCell(1, 0).getContents();
		String firefoxBrowser = r1.getCell(1, 2).getContents();
		Globalmethods.LaunchBrowser(firefoxBrowser, URL);
	}

	/*@Test
	public void createsubjectM() throws Exception{
		CreateSubject CS = new CreateSubject();
		CS.CreateSubject_Methd();
	}*/
	
	@Test(priority = 0)
	public static void exctractData() throws Exception {
		ExtractDataFromCRFtoExcel extract = new ExtractDataFromCRFtoExcel();
		extract.ExtractSubject_Methd();
	}

	/*@Test(priority = 1)
	public static void datacapture() throws Exception {

		Datacapture DC = new Datacapture();
		DC.Datacapture_method();
	}*/
	
	/*@Test(priority = 2)
	public static void DataVerifiy() throws Exception {
		ClinionDataVerification CDV = new ClinionDataVerification();
		CDV.DataVerification_Method();
	}*/

	/*@AfterMethod
	public static void close() {
		Globalmethods.driver.close();
	}*/

}
