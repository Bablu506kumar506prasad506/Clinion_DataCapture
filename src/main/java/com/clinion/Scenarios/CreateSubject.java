package com.clinion.Scenarios;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.clinion.GlobalMethod.Globalmethods;
import com.clinion.GlobalMethod.WaitMethod;
import jxl.Sheet;
import jxl.Workbook;

public class CreateSubject {

	public CreateSubject() {
		PageFactory.initElements(Globalmethods.driver, this);
	}

	WaitMethod GWait = new WaitMethod(Globalmethods.driver);
	Actions action = new Actions(Globalmethods.driver);

	int i;
	int pageincres = 1;

	public void CreateSubject_Methd() throws Exception {
		
		Globalmethods.CoInvestigator_Login();

		for (int k = 0; k <= 100; k++) {
			
			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/DataFile.xls");
			Workbook wb = Workbook.getWorkbook(fi);
			Sheet r1 = wb.getSheet("CreateSubjectData");

			GWait.Wait_GetElementByLinkText("Data Capture").click();

			int numberOfRow = r1.getRows();
			System.out.println("Num Of Rows in Excel: " + numberOfRow);

			for (i = 0; i <= numberOfRow;) {
				List<String> PageControlId = new ArrayList<String>();// ctl00_ContentPlaceHolder1_PnlEDCTitle//ctl07_PnlEDCTitle
				List<WebElement> elements = Globalmethods.driver
						.findElements(By.xpath("//*[@id='ctl00_ContentPlaceHolder1_PnlEDCTitle']//*"));
				System.out.println(elements);
				for (WebElement item : elements) {

					String controlType = item.getAttribute("type");
					String controlSelect = item.getTagName();
					if (controlType == null || controlType.trim() == "" || controlSelect == null
							|| controlSelect.trim() == "") {
						continue;
					}
					String controlId = item.getAttribute("id");

					if (controlType.equalsIgnoreCase("text") || controlType.equalsIgnoreCase("radio")
							|| controlSelect.equalsIgnoreCase("select") || controlType.equalsIgnoreCase("checkbox")) {
						System.out.println(controlId);
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

				int numOfCol = r1.getColumns();
				for (int j = 0; j <= numOfCol; j++) {

					try {
						String controlData = r1.getCell(j, i).getContents();
						System.out.println(controlData);
						String controlIdAndType = PageControlId.get(j);
						String controlId = controlIdAndType.split("~")[0];
						String controlType = controlIdAndType.split("~")[1];

						WebElement element = GWait.Wait_GetElementById(controlId);
						switch (controlType) {
						case "text":
							try {
								// System.out.println(controlData);
								if (element.getAttribute("class").equalsIgnoreCase("textboxbdr hasDatepick")) {
									String s = "document.getElementById('" + controlId + "').value = '" + controlData
											+ "'";
									JavascriptExecutor jse = (JavascriptExecutor) Globalmethods.driver;
									jse.executeScript(s);
								} else {
									String checkOtherTextBox = element.getAttribute("style");
									if (!checkOtherTextBox.equalsIgnoreCase("display: none;")) {
										element.sendKeys(controlData);
									}

								}
							} catch (Exception e) {
								e.getMessage();
							}
							break;
						case "select-one":
							try {
								System.out.println("DropDown: " + element.getAttribute("type"));
								if (element.getAttribute("type").equalsIgnoreCase("select-one")) {
									Select dropdown = new Select(element);
									System.out.println("Dropdown data: " + controlData);
									dropdown.selectByVisibleText(controlData);
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
						e.getStackTrace();
					}
				}
				// ctl00_ContentPlaceHolder1_btnEDCSubmit//ctl07_btnEDCSubmit
				GWait.Wait_GetElementById("ctl00_ContentPlaceHolder1_btnEDCSubmit").click();
				Datacapture dc = new Datacapture();
				dc.Datacapture_method();
				i++;
			}

		}
	}

}
