package com.clinion.Scenarios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.clinion.GlobalMethod.Globalmethods;
import com.clinion.GlobalMethod.WaitMethod;

import jxl.Sheet;
import jxl.Workbook;

public class ExtractDataFromCRFtoExcel {

	public ExtractDataFromCRFtoExcel() {
		PageFactory.initElements(Globalmethods.driver, this);
	}

	WaitMethod GWait = new WaitMethod(Globalmethods.driver);
	Actions action = new Actions(Globalmethods.driver);

	public void ExtractSubject_Methd() throws Exception {
		Globalmethods.Datamanager_Login_Client1();
//		Globalmethods.FP_Login_Client1();
		

		GWait.Wait_GetElementByLinkText("Data Capture").click();
//		GWait.Wait_GetElementByXpath("//tr/td[7]/table/tbody/tr/td[1]/a", 120).click();
		Thread.sleep(1000);
		GWait.Wait_GetElementByLinkText("1701002S008").click();
		Thread.sleep(4000);
		GWait.Wait_GetElementById("imgdivScrrenslist5.00v",120).click();
		Thread.sleep(5000);
		
		GWait.Wait_GetElementByXpath("//*[@id=\"divScrrenslist5.00v\"]/table/tbody/tr/td/a").click();
//		GWait.Wait_GetElementById("//*[@id='tvVisitst27']").click();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("subjectdata");
		
		int rowCount = 0;
		for (int i = 0; i <= rowCount; i++) {
			
			//start - reading the control id's
			List<String> PageControlId = new ArrayList<String>();
			List<WebElement> elements = Globalmethods.driver.findElements(By.xpath("//*[@id='divforPrint']//*"));
			for (WebElement item : elements) {

				String controlType = item.getAttribute("type");
				if (controlType == null || controlType.trim() == "") {
					continue;
				}

				String controlId = item.getAttribute("id");

				if (controlType.equalsIgnoreCase("text") || controlType.equalsIgnoreCase("radio")
						|| controlType.equalsIgnoreCase("select-one") || controlType.equalsIgnoreCase("checkbox")
						|| controlType.equalsIgnoreCase("submit")) {

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
			//end - reading the control id's
			if (PageControlId.size() > 0) {

				XSSFRow row;
				row = spreadsheet.createRow(rowCount);
				//start - reading data from control and inserting in to excel cells
				for (int i1 = 2; i1 < PageControlId.size(); i1++) {
					int controlIndex = i1-2;
					Cell cell = row.createCell(i1);
					String controlIdAndType = PageControlId.get(controlIndex);
					String controlId = controlIdAndType.split("~")[0];
					String controlType = controlIdAndType.split("~")[1];

					WebElement element = GWait.Wait_GetElementById(controlId,120);

					switch (controlType) {
					case "text":
						cell.setCellValue((String) element.getAttribute("value"));
						break;
					case "radio":
						try {
							List<WebElement> RadioButtonList = element.findElements(By.xpath(".//*"));
							for (int radio = 0; radio < RadioButtonList.size(); radio++) {
								if (RadioButtonList.get(radio).getAttribute("type") != null
										&& RadioButtonList.get(radio).getAttribute("type").equalsIgnoreCase("radio")) {
									if (RadioButtonList.get(radio).isSelected()) {
										cell.setCellValue((String) RadioButtonList.get(radio).getAttribute("value"));
									}
								}
							}

						} catch (Exception e) {
							e.getMessage();
						}
						break;
					case "checkbox":
						try {
							List<WebElement> CheckBoxsList = Globalmethods.driver.findElements(By.xpath(
									"//table[@id='" + controlId + "']/tbody/tr/td/span/input[@type='checkbox']"));
							String checkedValues = "";
							for (int checkBox = 0; checkBox < CheckBoxsList.size(); checkBox++) {
								String xpathForInput = generateXPATH(CheckBoxsList.get(checkBox), "")
										.replaceAll("input", "label");
								WebElement labelElement = GWait.Wait_GetElementByXpath(xpathForInput);
								if (CheckBoxsList.get(checkBox).isSelected()) {
									checkedValues += labelElement.getText() + " , ";
								}
							}
							cell.setCellValue((String) checkedValues);
						} catch (Exception e) {
							e.getMessage();
						}
						break;
					case "select-one":
						try {
							if (element.getAttribute("type").equalsIgnoreCase("select-one")) {
								Select se = new Select(GWait.Wait_GetElementById(controlId,120));
								WebElement option = se.getFirstSelectedOption();
								cell.setCellValue((String) option.getText());

							} else {
								String checkOtherTextBox = element.getAttribute("style");
								if (!checkOtherTextBox.equalsIgnoreCase("display: none;")) {
									// element.sendKeys(controlData);
								}
							}

						} catch (Exception e) {
							e.getStackTrace();
						}
						break;
					}
				}
				//end - reading data from control and inserting in to excel cells
				
				//Clicking the submit and next button
				
				FileOutputStream out = new FileOutputStream(new File("TimerDataFile2.xls"));
				workbook.write(out);
				out.close();
				System.out.println("TimerDataFile2.xls written successfully");
//				GWait.Wait_GetElementById("btnNext", 120).click();
				
				GWait.Wait_GetElementById("btnSaveSubmit",120).click();
				Globalmethods.isAlertPresent();
				Thread.sleep(3000);
			}
			
//			incrementing the row
			rowCount++;
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
