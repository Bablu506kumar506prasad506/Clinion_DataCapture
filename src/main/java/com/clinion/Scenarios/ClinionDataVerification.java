package com.clinion.Scenarios;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import static org.junit.Assert.*;

public class ClinionDataVerification {

	public static WebDriver driver;
	static PrintStream verificationErrors;

	public void DataVerification_Method() throws Exception {
		/*
		 * FileInputStream fi = new
		 * FileInputStream(System.getProperty("user.dir") +
		 * "/src/main/resources/DataFile.xls"); Workbook wb =
		 * Workbook.getWorkbook(fi); Sheet sh1 = wb.getSheet("Login"); String
		 * URL = sh1.getCell(4, 0).getContents();
		 */
		driver = new FirefoxDriver();
		System.out.println("!!!------Om Shri Ganeshaya Namah-------Welcome to My Code---------!!!");
		driver.manage().window().maximize();
		driver.get("http://haem-pro.clinion.com/Stage/Default.aspx");

		/*
		 * String username = sh1.getCell(4, 1).getContents(); String password =
		 * sh1.getCell(4, 2).getContents(); String datacapture = sh1.getCell(4,
		 * 3).getContents(); String subjectid = sh1.getCell(4, 4).getContents();
		 * String pagename = sh1.getCell(4, 5).getContents(); String expendicon
		 * = sh1.getCell(4, 6).getContents();
		 * 
		 * driver.findElement(By.id("txtUserName")).sendKeys(username);
		 * driver.findElement(By.id("txtPassword")).sendKeys(password);
		 * 
		 * driver.findElement(By.id("LoginButton")).click();
		 * driver.findElement(By.linkText(datacapture)).click();
		 * Thread.sleep(1000);
		 * driver.findElement(By.linkText(subjectid)).click();
		 * Thread.sleep(2500); driver.findElement(By.id(expendicon)).click();
		 * driver.findElement(By.linkText(pagename)).click();
		 */

		driver.findElement(By.id("txtUserName")).sendKeys("coinvestigator");
		driver.findElement(By.id("txtPassword")).sendKeys("Test@123");
		// -----Login----//
		driver.findElement(By.id("LoginButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("Data Capture")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("017")).click();
		Thread.sleep(2500);
		driver.findElement(By.id("imgdivScrrenslist1.00v")).click();
		driver.findElement(By.xpath("//tr[2]/td/div/table/tbody/tr[1]/td/a")).click();

		FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/DataFile.xls");
		Workbook wb = Workbook.getWorkbook(fi);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setSuppressWarnings(true);
		Sheet st = wb.getSheet("subjectdata");

		int numberOfRow = st.getRows();
		System.out.println("Num Of Rows in Excel: " + numberOfRow);
		for (int i = 0; i <= numberOfRow; i++) {

			List<String> PageControlId = new ArrayList<String>();
			List<WebElement> elements = driver.findElements(By.xpath("//*[@id='divforPrint']//*"));
			for (WebElement item : elements) {

				String controlType = item.getAttribute("type");
				if (controlType == null || controlType.trim() == "") {
					continue;
				}
				String controlId = item.getAttribute("id");

				if (controlType.equalsIgnoreCase("text") || controlType.equalsIgnoreCase("radio")
						|| controlType.equalsIgnoreCase("select-one") || controlType.equalsIgnoreCase("checkbox")) {
					// System.out.println(controlId);
					// System.out.println(controlType);
					if (controlType.equalsIgnoreCase("radio")) {
						WebElement parent = item.findElement(By.xpath("../../../.."));
						String controlIdAndType = parent.getAttribute("id") + "~" + controlType;
						boolean controlExists = false;
						for (String existingControlId : PageControlId) {
							if (existingControlId.equalsIgnoreCase(controlIdAndType)) {
								controlExists = true;
							}
						}
						if (!controlExists) {

							PageControlId.add(controlIdAndType);
						}
					} else if (controlType.equalsIgnoreCase("checkbox")) {
						WebElement parentcheckbox = item.findElement(By.xpath("../../../../.."));
						String controlIdAndTypecheck = parentcheckbox.getAttribute("id") + "~" + controlType;
						boolean controlExistscheck = false;
						for (String existingControlIdcheck : PageControlId) {

							if (existingControlIdcheck.equalsIgnoreCase(controlIdAndTypecheck)) {
								controlExistscheck = true;
							}
						}
						if (!controlExistscheck) {

							PageControlId.add(controlIdAndTypecheck);
						}
					} else {
						PageControlId.add(controlId + "~" + controlType);
					}
				}
			}

			int numOfCol = st.getColumns();
			int count = 0;
			System.out.println("Num Of Columns in Excel: " + numOfCol);
			for (int j = 0; j <= numOfCol; j++) {
				try {
					String controlData = st.getCell(j, i).getContents();
					String controlIdAndType = PageControlId.get(j);
					String controlId = controlIdAndType.split("~")[0];
					String controlType = controlIdAndType.split("~")[1];
					WebElement element = driver.findElement(By.id(controlId));

					// System.out.println("controlId: "+controlId);
					// System.out.println("controlType: "+controlType);
					System.out.println("ExcelCellData: " + controlData);

					switch (controlType) {
					case "text":
						try {
							try {
								System.out.println("controlData: " + element.getAttribute("value"));
								assertEquals(controlData, element.getAttribute("value"));
								System.out.println("Successfully done!!!");
							} catch (Error e) {
								System.out.println("Un-Successfully done Please Verify !!!");
								verificationErrors.append(e.toString());
							}

						} catch (Exception e) {
							e.getMessage();
						}
						break;
					case "radio":
						try {
							List<WebElement> RadioButtonList = element.findElements(By.xpath(".//*"));
							for (int radio = 0; radio < RadioButtonList.size(); radio++) {
								if (RadioButtonList.get(radio).getAttribute("type") != null
										&& RadioButtonList.get(radio).getAttribute("type").equalsIgnoreCase("radio")) {

									if (RadioButtonList.get(radio).isSelected() && RadioButtonList.get(radio)
											.getAttribute("value").equalsIgnoreCase(controlData)) {
										System.out.println(
												"controlData: " + RadioButtonList.get(radio).getAttribute("value"));
										System.out.println("Successfully done!!!");
									} else if (RadioButtonList.get(radio).isSelected() && !RadioButtonList.get(radio)
											.getAttribute("value").equalsIgnoreCase(controlData)) {
										System.out.println(
												"controlData: " + RadioButtonList.get(radio).getAttribute("value"));
										System.out.println("Un-Successfully done Please Verify !!!");
									}
								}
							}

						} catch (Exception e) {
							e.getMessage();
						}
						break;
					case "checkbox":
						try {
							List<WebElement> CheckBoxsList = driver.findElements(By.xpath(
									"//table[@id='" + controlId + "']/tbody/tr/td/span/input[@type='checkbox']"));
							List<String> list = Lists.newArrayList(Splitter.on(",").trimResults().split(controlData));
							for (int checkBox = 0; checkBox < CheckBoxsList.size(); checkBox++) {
								String xpathForInput = generateXPATH(CheckBoxsList.get(checkBox), "")
										.replaceAll("input", "label");
								WebElement labelElement = driver.findElement(By.xpath(xpathForInput));
								if (CheckBoxsList.get(checkBox).isSelected() && list.contains(labelElement.getText())) {
									System.out.println("controlData: " + labelElement.getText());
									System.out.println("Successfully done!!!");
								} else if (CheckBoxsList.get(checkBox).isSelected()
										&& !list.contains(labelElement.getText())) {
									System.out.println("controlData: " + labelElement.getText());
									System.out.println("Un-Successfully done Please Verify !!!");
								}
							}
						} catch (Exception e) {
							e.getMessage();
						}
						break;
					case "select-one":
						try {
							if (element.getAttribute("type").equalsIgnoreCase("select-one")) {
								Select se = new Select(driver.findElement(By.id(controlId)));
								WebElement option = se.getFirstSelectedOption();
								System.out.println("controlData: " + option.getText());
								if (controlData.equalsIgnoreCase(option.getText())) {
									System.out.println("Successfully done!!!");
								} else {
									System.out.println("Un-Successfully done Please Verify !!!");
								}

							} else {
								String checkOtherTextBox = element.getAttribute("style");
								if (!checkOtherTextBox.equalsIgnoreCase("display: none;")) {
									element.sendKeys(controlData);
								}
							}

						} catch (Exception e) {
							e.getStackTrace();
						}
						break;
					}

				} catch (Exception e) {
					e.getMessage();
				}
				count++;
			}

			Thread.sleep(3000);
			driver.findElement(By.id("btnSaveSubmit")).click();

		}
	}

	private static String generateXPATH(WebElement childElement, String current) {
		String childTag = childElement.getTagName();
		if (childTag.equals("html")) {
			return "/html[1]" + current;
		}
		WebElement parentElement = childElement.findElement(By.xpath(".."));
		List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
		int count = 0;
		for (int i = 0; i < childrenElements.size(); i++) {
			WebElement childrenElement = childrenElements.get(i);
			String childrenElementTag = childrenElement.getTagName();
			if (childTag.equals(childrenElementTag)) {
				count++;
			}
			if (childElement.equals(childrenElement)) {
				return generateXPATH(parentElement, "/" + childTag + "[" + count + "]" + current);
			}
		}
		return null;
	}
}
