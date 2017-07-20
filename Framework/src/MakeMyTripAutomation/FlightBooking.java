package MakeMyTripAutomation;



import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

public class FlightBooking extends MakeMyTripAllFunctions {

	@Test
	public void BookFlight_OneWay() throws InterruptedException{
		//Set method name to MethodName variable
		methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		//initialize method mane to reporter and test data sheet
		test = extent.startTest(methodName);
		dataSheet.storeSpecificRowDataToHashMap(methodName);

		//launch application
		String browserType = (String) dataSheet.getValue("BrowserType");
		launchMakeMytrip(browserType);

		//Select From Location
		String fromLocation = (String) dataSheet.getValue("FromLocation");
		String fromlocationCode = (String) dataSheet.getValue("FromLocationCode");
		selectLocation("From", fromLocation, fromlocationCode);

		//Select To Location
		String toLocation = (String) dataSheet.getValue("ToLocation");
		String tolocationCode = (String) dataSheet.getValue("ToLocationCode");
		selectLocation("To", toLocation, tolocationCode);

		//Select Depart Date
		selectDateStartAndreturn((String) dataSheet.getValue("DepartDate"),"Depart");
		test.log(LogStatus.PASS, "Select Depert Date as "+ (String) dataSheet.getValue("DepartDate"));

		//Search Flight
		ClickSearchButton_SearchFlight();
	}

	@Test
	public void BookFlight_RoundTrip() throws InterruptedException{

		//Set method name to MethodName variable
		methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		//initialize method mane to reporter and test data sheet
		test = extent.startTest(methodName);
		dataSheet.storeSpecificRowDataToHashMap(methodName);

		//launch application
		String browserType = (String) dataSheet.getValue("BrowserType");
		launchMakeMytrip(browserType);

		//Select From Location
		String fromLocation = (String) dataSheet.getValue("FromLocation");
		String fromlocationCode = (String) dataSheet.getValue("FromLocationCode");
		selectLocation("From", fromLocation, fromlocationCode);

		//Select To Location
		String toLocation = (String) dataSheet.getValue("ToLocation");
		String tolocationCode = (String) dataSheet.getValue("ToLocationCode");
		selectLocation("To", toLocation, tolocationCode);

		//Select Depart Date
		selectDateStartAndreturn((String) dataSheet.getValue("DepartDate"),"Depart");
		test.log(LogStatus.PASS, "Select Depert Date as "+ (String) dataSheet.getValue("DepartDate"));

		//Select Depart Date
		selectDateStartAndreturn((String) dataSheet.getValue("ReturnDate"),"Return");
		test.log(LogStatus.PASS, "Select Return Date as "+ (String) dataSheet.getValue("ReturnDate"));

		//Search Flight
		ClickSearchButton_SearchFlight();
	}


}
