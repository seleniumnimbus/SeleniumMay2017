package MakeMyTripAutomation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import DriverScript.DriverScript;

public class MakeMyTripAllFunctions extends DriverScript {

	//#############################################################################################
	/*	Function Name: launchMakeMytrip */
	/*	Description: Lunch MMT Application */
	//#############################################################################################
	public void launchMakeMytrip(String browserType) {
		//Get Value from Environment Sheet
		String applicationURL = environmentDataSheet.getValue("MakeMyTrip_URL");
		launchApplication(applicationURL, browserType);
		if (waitfor_ElementToBePresent(10, xPathDesc.getValue("MMT_FlightPage", "MMT_Logo"))) {
			test.log(LogStatus.PASS, "Make My Trip is launched");
		}
		else{
			test.log(LogStatus.FAIL, "Make My Trip is not launched");
		}
	}

	//#############################################################################################
	/*	Function Name: selectLocation */
	/*	Description: Select Location */
	//#############################################################################################
	public void selectLocation(String locationType, String location, String locationCode){
		if(locationType.equals("From")){
			//Enter value to from location
			EnterValueToTextBox(xPathDesc.getValue("MMT_FlightPage", "FromLocation"), location, "From Location text box");
			if (waitfor_ElementToBePresent(10, "//ul[contains(@class,'hp-widget__sfrom')]//span[contains(text(),'"+ locationCode +"')]/parent::p")) {
				clickElement("//ul[contains(@class,'hp-widget__sfrom')]//span[contains(text(),'"+ locationCode +"')]/parent::p", "From Location");
				test.log(LogStatus.PASS, "Select From Location as "+location);
			}
			else{
				Assert.assertTrue(false);
			}
		}
		else if(locationType.equals("To")){
			//Enter value to to location
			EnterValueToTextBox(xPathDesc.getValue("MMT_FlightPage", "ToLocation"), location, "To Location text box");
			if (waitfor_ElementToBePresent(10, "//ul[contains(@class,'hp-widget__sTo')]//span[contains(text(),'"+ locationCode +"')]/parent::p")) {
				clickElement("//ul[contains(@class,'hp-widget__sTo')]//span[contains(text(),'"+ locationCode +"')]/parent::p", "To Location");
				test.log(LogStatus.PASS, "Select To Location as "+location);
			}
			else{
				Assert.assertTrue(false);
			}
		}
	}

	//#############################################################################################
	/*	Function Name: selectLocation */
	/*	Description: Select Location */
	//#############################################################################################
	public void selectDateStartAndreturn(String date,String JourneyType) throws InterruptedException{
		//split date
		String[] arrDate = date.split("-");
		String xpathCal = null;
		//xpath for calendar
		if(JourneyType.equals("Depart")){
			waitfor_ElementToBePresent(10, xPathDesc.getValue("MMT_FlightPage", "DepartCalender"));
			clickElement(xPathDesc.getValue("MMT_FlightPage", "DepartCalender"), "Depart Calender");
			Thread.sleep(3000);
			xpathCal = "//div[@class='dateFilter hasDatepicker']";
		}
		else if(JourneyType.equals("Return")){
			waitfor_ElementToBePresent(10, xPathDesc.getValue("MMT_FlightPage", "ReturnCalender"));
			clickElement(xPathDesc.getValue("MMT_FlightPage", "ReturnCalender"), "Return Calender");
			Thread.sleep(3000);
			xpathCal = "//div[@class='dateFilterReturn hasDatepicker']";
		}

		List<WebElement> calender = driver.findElements(By.xpath(xpathCal+"//div[contains(@class,'datepicker-group')]"));
		//select date
		for(int i = 0;i<calender.size();i++){
			String getMonth = driver.findElement(By.xpath(xpathCal+"//div[contains(@class,'datepicker-group')]["+(i+1)+"]//span[@class='ui-datepicker-month']")).getText();
			String getYear = driver.findElement(By.xpath(xpathCal+"//div[contains(@class,'datepicker-group')]["+(i+1)+"]//span[@class='ui-datepicker-year']")).getText();
			/*System.out.println(getMonth);
			System.out.println(getYear);*/
			if((getMonth.equalsIgnoreCase(arrDate[1])) && (getYear.equals(arrDate[2]))){
				driver.findElement(By.xpath(xpathCal+"//div[contains(@class,'datepicker-group')]["+(i+1)+"]//tbody//td[@data-handler='selectDay']/a[text()='"+arrDate[0]+"']")).click();
				Thread.sleep(2000);
				return;
			}
		}
		//click on next button
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(xpathCal+"//span[text()='Next']/parent::a")));
		driver.findElement(By.xpath(xpathCal+"//span[text()='Next']/parent::a")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(xpathCal+"//span[text()='Next']/parent::a")).click();
		Thread.sleep(2000);
		selectDateStartAndreturn(date,JourneyType);
		return;
	}

	//#############################################################################################
	/*	Function Name: selectPassengers */
	/*	Description: Select Location */
	//#############################################################################################
	public void selectPassengers(){
		String adultsPassengers = (String) dataSheet.getValue("Adultd_Passengers");
		String childenPassengers = (String) dataSheet.getValue("Childen_Passengers");
		String infantsPassengers = (String) dataSheet.getValue("Infants_Passengers");
	}

	//#############################################################################################
	/*	Function Name: ClickSearchButton_SearchFlight */
	/*	Description: Select Location */
	//#############################################################################################
	public void ClickSearchButton_SearchFlight(){
		clickElement(xPathDesc.getValue("MMT_FlightPage", "SearchButton"), "Serachbutton");
		if(waitfor_ElementToBePresent(180, xPathDesc.getValue("MMT_FlightPage", "AirlinesList"))){
			test.log(LogStatus.PASS, "Click on Search Flight button and Flights are searched" );
		}
		else{
			test.log(LogStatus.FAIL, "Click on Search Flight button but Flights are not searched" );
		}
	}
	
	
}
