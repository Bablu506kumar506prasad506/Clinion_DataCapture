package com.clinion.Scenarios;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.clinion.GlobalMethod.Globalmethods;
import com.clinion.GlobalMethod.WaitMethod;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import jxl.Sheet;
import jxl.Workbook;

public class Datacapture extends CreateSubject {

	public Datacapture() {
		PageFactory.initElements(Globalmethods.driver, this);
	}

	WaitMethod GWait = new WaitMethod(Globalmethods.driver);
	Actions action = new Actions(Globalmethods.driver);

	
	
	public void Datacapture_method() throws Exception {
		
			// ---Click subject ID's--//
		WebElement ScreeningNumber = GWait.Wait_GetElementByXpath("//table/tbody/tr[1]/td[3]/a");
		Globalmethods.scrollToElement(ScreeningNumber);
		ScreeningNumber.click();
			// ----Click Expend icon----//
			Thread.sleep(3000);
			GWait.Wait_GetElementById("imgdivScrrenslist1.00v", 120).click();
			// ----Click Page------//
			Thread.sleep(1000);
			GWait.Wait_GetElementByXpath("//*[@id=\"divScrrenslist1.00v\"]/table/tbody/tr/td/a").click();
//			GWait.Wait_GetElementByLinkText("HEALTH ASSESSMENT QUESTIONNAIRE (HAQ-DI)").click();

			FileInputStream fi = new FileInputStream(System.getProperty("user.dir") + "/DataFile2.xls");
			HSSFWorkbook wb = new HSSFWorkbook(fi);
			HSSFSheet st = wb.getSheet("subjectdata");

			int numberOfRow = st.getLastRowNum();
			System.out.println("No of rows: "+numberOfRow);
			
			System.out.println("Row count: " + numberOfRow);
			for (int i = 0; i <= numberOfRow; i++) {
				
				Row row = st.getRow(i);
				List<String> PageControlId = new ArrayList<String>();
				List<WebElement> elements = Globalmethods.driver.findElements(By.xpath("//*[@id='divforPrint']//*"));
				for (WebElement item : elements) {

					String controlType = item.getAttribute("type");
					String controlSelect = item.getTagName();
					if (controlType == null || controlType.trim() == ""|| controlSelect == null || controlSelect.trim()== "") {
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

				int numOfCol = row.getLastCellNum();
				for (int j = 2; j <= numOfCol; j++) {
					try {
						int controlIndex = j - 2;
						String controlData = row.getCell(j).getStringCellValue();
						String controlIdAndType = PageControlId.get(controlIndex);
						String controlId = controlIdAndType.split("~")[0];
						String controlType = controlIdAndType.split("~")[1];
						WebElement element = GWait.Wait_GetElementById(controlId);
						System.out.println("Data "+controlData);
						switch (controlType) {
						case "text":
							try {
								if (element.getAttribute("class").equalsIgnoreCase("crfdatetextBox hasDatepick")) {
									String s = "document.getElementById('" + controlId + "').value = '" + controlData + "'";
									System.out.println("Excel Data " +controlData);
									//Generating an action to type a text in CAPS
									
									JavascriptExecutor jse = (JavascriptExecutor) Globalmethods.driver;
//									action.sendKeys(Keys.ENTER).build().perform();
									jse.executeScript(s);
									//#popup_cancel
									Globalmethods.QueriesPopup();
									
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
						case "radio":
							try {
								List<WebElement> RadioButtonList = element.findElements(By.xpath(".//*"));
								for (int radio = 0; radio < RadioButtonList.size(); radio++) {
									if (RadioButtonList.get(radio).getAttribute("type") != null
											&& RadioButtonList.get(radio).getAttribute("type").equalsIgnoreCase("radio")) {
										System.out.println(
												"Radio value: " + RadioButtonList.get(radio).getAttribute("value"));
										if (RadioButtonList.get(radio).getAttribute("value").trim()
												.equalsIgnoreCase(controlData)) {
											System.out.println("Excel Data " +controlData);
											RadioButtonList.get(radio).click();
											Globalmethods.QueriesPopup();
										}
									}
								}

							} catch (Exception e) {
								e.getMessage();
							}
							break;
						case "checkbox":
							try {
								List<WebElement> CheckBoxsList = element
										.findElements(By.xpath("//input[@type='checkbox']"));
								System.out.println(CheckBoxsList);
								List<String> list = Lists.newArrayList(Splitter.on(" , ").trimResults().split(controlData));
								System.out.println(list);
								for (int checkBox = 0; checkBox < CheckBoxsList.size(); checkBox++) {
									System.out.println(CheckBoxsList.size());
									System.out.println(CheckBoxsList.get(checkBox).getAttribute("type"));
									System.out.println(
											CheckBoxsList.get(checkBox).getAttribute("type").equalsIgnoreCase("checkbox"));
									if (CheckBoxsList.get(checkBox).getAttribute("type").equalsIgnoreCase("checkbox")) {
										System.out.println("Iam in");
										String xpathForInput = generateXPATH(CheckBoxsList.get(checkBox), "")
												.replaceAll("input", "label");
										System.out.println(xpathForInput);
										WebElement labelElement = GWait.Wait_GetElementByXpath(xpathForInput);
										System.out.println(labelElement);
										System.out.println(labelElement.getText());
										if (list.contains(labelElement.getText())) {
											System.out.println("Excel Data " +controlData);
											CheckBoxsList.get(checkBox).click();
											Globalmethods.QueriesPopup();
										}
									}
								}
							} catch (Exception e) {
								e.getMessage();
							}
							break;
						case "select-one":
							try {
								System.out.println("DropDown: "+element.getAttribute("type"));
								if (element.getAttribute("type").equalsIgnoreCase("select-one")) {
									Select dropdown = new Select(element);
									System.out.println("Dropdown data: "+controlData.toLowerCase());
									System.out.println("Excel Data " +controlData);
									dropdown.selectByValue(controlData.toLowerCase());
								} else {
									String checkOtherTextBox = element.getAttribute("style");
									if (!checkOtherTextBox.equalsIgnoreCase("display: none;")) {
										element.sendKeys(controlData);
										Globalmethods.QueriesPopup();
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

				}
				
				try {
					Globalmethods.QueriesPopup();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.sleep(1500);
				GWait.Wait_GetElementById("btnSaveSubmit").click();
				
//				Globalmethods.electronicSign();
				pageincres++;
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
