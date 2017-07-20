package DriverScript;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



public class DriverScript {

	public ExtentReports extent;
	public ExtentTest test;
	public WebDriver driver;
	public Logger logger;
	public TestDataReadWrite dataSheet;
	public EnvironmentVariableReader environmentDataSheet;
	public XPathDescription xPathDesc;
	public String methodName;
	public WebDriverWait wait;

	//#############################################################################################
	/*	Function Name: launchApplication */
	/*	Description: Launch browser */
	//#############################################################################################
	public void launchApplication(String applicationURL, String browserType){
		//for Chrome browser
		if(browserType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Lib\\chromedriver.exe");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("profile.default_content_setting_values.notifications", 2);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			driver = new ChromeDriver(options);
		}
		//Launch Browser
		driver.get(applicationURL);
		logger.debug("Launch Application. URL "+ applicationURL);
		driver.manage().window().maximize();
		logger.debug("Maximize the window");
	}

	//#############################################################################################
	/*	Function Name: waitfor_ElementToBePresent */
	/*	Description: Wait for the object to be present in the application */
	//#############################################################################################
	public boolean waitfor_ElementToBePresent(int seconds,String xpathExpression){
		wait = new WebDriverWait(driver, seconds);
		try{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathExpression)));
			logger.debug("Xpath "+xpathExpression+ " is present");
			return true;
		}
		catch(Throwable t){
			logger.debug("Xpath "+xpathExpression+ " is not present");
			return false;
		}
	}

	//#############################################################################################
	/*	Function Name: clickElement */
	/*	Description: Click Element */
	//#############################################################################################
	public boolean clickElement(String xpathExpression,String objectName){
		wait = new WebDriverWait(driver, 10);
		try{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression)));
			driver.findElement(By.xpath(xpathExpression)).click();
			logger.debug("Object "+ objectName+ " is clicked");
			return true;
		}
		catch(Throwable t){
			logger.debug("Object "+ objectName+ " is not present and clickable");
			return false;
		}
	}

	//#############################################################################################
	/*	Function Name: EnterValueToTextBox */
	/*	Description: Enter value to text box */
	//#############################################################################################
	public boolean EnterValueToTextBox(String xpathExpression,String value,String objectName){
		wait = new WebDriverWait(driver, 10);
		try{
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathExpression)));
			driver.findElement(By.xpath(xpathExpression)).sendKeys(value);
			logger.debug("Value "+ value + " is entered to object "+ objectName);
			return true;
		}
		catch(Throwable t){
			logger.debug("Object "+ objectName+ " is not present and clickable");
			return false;
		}
	}

	//#############################################################################################
	/*	Function Name: isPresent */
	/*	Description: Presence of Object */
	//#############################################################################################
	public boolean isPresent(String xpathExpression){
		return(driver.findElement(By.xpath(xpathExpression)).isDisplayed());
	}


	//#############################################################################################
	/* Framework Related Code */
	//#############################################################################################
	@BeforeTest
	public void config() throws IOException
	{
		//Create the Date and Time format for reports
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDateTime = df.format(today);
		reportDateTime = reportDateTime.replace("/", "_").replace(":", "_").replace(" ", "_");
		//Create Report File Path
		extent = new ExtentReports(System.getProperty("user.dir") +"/Result/HTML_Reports/AutomationReports_"+reportDateTime+".html", false);
		//Add some System information to html report
		extent.addSystemInfo("User Name", "Automation Tester")
		.addSystemInfo("Environment", "QA")
		.addSystemInfo("Application Name", "SnapDeal")
		.loadConfig(new File(System.getProperty("user.dir")+"\\extendReportConfiguration.xml"));
		//for logger
		logger = Logger.getLogger("SeleniumLog");
		//Create object for data sheet
		dataSheet = new TestDataReadWrite();
		//Create object for environment sheet
		environmentDataSheet = new EnvironmentVariableReader();
		//Create object for XPath Description
		xPathDesc = new XPathDescription();
	}

	@AfterMethod
	public void generateReports(ITestResult result) throws IOException{
		//if test cases fail
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenShotPath = captureScreenshot();
			test.log(LogStatus.FAIL, result.getThrowable());
			test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(screenShotPath));
		}
		//for Skipped test cases
		else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
		}
		//end test report
		extent.endTest(test);
		driver.quit();
	}

	@AfterTest
	public void tearDown(){
		extent.flush();
		extent.close();
		//driver.quit();
	}

	//Take Screen shot
	public String captureScreenshot() throws IOException
	{
		//Create the Date and Time format for reports
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDateTime = df.format(today);
		reportDateTime = reportDateTime.replace("/", "_").replace(":", "_").replace(" ", "_");
		//take screen shot
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir") +"\\Result\\ScreenShot\\Screenshot_"+reportDateTime+".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);        
		return dest;
	}
}
