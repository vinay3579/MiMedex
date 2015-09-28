package com.pyramidconsulting.report;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;


import com.pyramidconsulting.Constants;
import com.pyramidconsulting.Launcher;
import com.pyramidconsulting.runner.Script;
import com.pyramidconsulting.util.PropertyUtil;
import com.pyramidconsulting.util.ScreenUtil;
import com.thoughtworks.selenium.Selenium;

public abstract class AbstractReporter implements Reporter {
	protected static Logger logger = Logger.getLogger(AbstractReporter.class);

	protected Selenium selenium;
	protected WebDriver driver;

	//private Date testStartTime;
	public static String filename;
	private static  String testCaseName;
	private  String environment;
	//	private final String hostName;
	private  String buildVersion;
	private  String browser;
	private  String applicationUrl;

	private Integer testStepPassCount = new Integer(0);
	public static Integer testStepFailCount = new Integer(0);
	private Integer testStepNo = new Integer(0);
	private static Date testStartTime = null;
	public  static Integer testIterationNumber = new Integer(0);
	public static String strReportPath="";
	public String strHTMLSummaryReportPath = null;
	
	//lines below added for properties access

	protected static  PropertyUtil properties = new PropertyUtil(Constants.PROPERTY_CONFIG_FILENAME);


	protected Boolean isFromQC = Boolean.FALSE;

	public AbstractReporter( String testCaseName,
			 String environment,
			 String buildVersion,  String browser,
			 String applicationUrl,  Selenium selenium,  WebDriver driver)  {				
		this.testCaseName = testCaseName;
		this.environment = environment;
		this.buildVersion = buildVersion;
		this.browser = browser;
		this.applicationUrl = applicationUrl;
		this.selenium = selenium;
		this.driver = driver;
		testStartTime = new Date();
		
	}
	

	@Override
	public void setIsFromQC( Boolean isFromQC) {
		this.isFromQC = isFromQC;
		
	}


	protected abstract byte[] getReportHeader() throws FileNotFoundException,
	IOException;

	protected abstract byte[] getReportContent() throws FileNotFoundException,
	IOException;
	protected abstract String getFileExtension();

	protected abstract void addStepStatus( String description, final String expectedResult,
			final String actualResult,  String stepStatus)
					throws FileNotFoundException, IOException ;

	
	
	@Override
	public void ReportStep(String description,String expectedResult, String actualResult,  String stepStatus)throws FileNotFoundException, IOException {
	 try{
		 // if report path is given in config.xls, make that as path for reports folder, else default value set in framework config file is taken.
		String reportsPath = "";
		String reportPathinMap =Script.dicConfigValues.get("reportsPath");
		if(!(reportPathinMap.equals("") || reportPathinMap.equals(null)))
        	reportsPath =  reportPathinMap;
		else
			reportsPath = properties.getProperty("report.location");
		String screenCaptureAres = properties.getProperty("report.screenshot.area");
		String screenshot = Script.dicConfigValues.get("ScreenShot").toLowerCase();
		String seleniumVariant = Script.dicConfigValues.get("SeleniumVariant").toLowerCase();
		
		incTestStepNo();
		
		// when step passes
		if (stepStatus.equals("Pass")) 
		{
			incTestStepPassCount();
			if(screenshot.equals("yes"))
			{
				actualResult = capturescreen(screenCaptureAres, seleniumVariant, reportsPath, actualResult);
			}
		}
		// when step fails
		else
		{
			incTestStepFailCount();
			actualResult = capturescreen(screenCaptureAres, seleniumVariant, reportsPath, actualResult);
		}
		addStepStatus(description, expectedResult, actualResult, stepStatus);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	
	
	//////////////////////////////
  public String capturescreen(String screenCaptureAres, String seleniumVariant, String reportsPath, String actualResult )
  {
	  try{
//		incTestStepFailCount();
		boolean isScreenCaptured = true;
		File screenshotFileName = new File(generateScreenshotFullFileName());
		String imageType = properties.getProperty("report.screenshot.fileformat");
		String reportingType = Script.dicConfigValues.get("ReportingFormat");
		 ////// for capturing full screen screenshots
		if(StringUtils.equals(screenCaptureAres, "FULL_PAGE") || StringUtils.isBlank(screenCaptureAres)) 
		{
			File scrFile =null;
			
			//// for webdriver
			if (seleniumVariant.contains("webdriver"))
			{
				if(Launcher.executionMethod.equals("local"))
				{
				    /////// for taking screenshots only in case of safari**************
					if(browser.toLowerCase().contains("safari"))
					{
						Robot objRobot = new Robot();
						BufferedImage objBufferedimage = objRobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
						try 
						{
						    // copy buffered image
							scrFile = new File("tempImage." +imageType);
						    ImageIO.write(objBufferedimage, imageType, scrFile);
						} 
						catch (IOException e) 
						{
						   logger.error("Unable to write buffered image to tempImage file."); 
						}
					}
					///////////////////////////////////////////////***********************
					
					// for capturing screen on all other browsers
					else
					{
						scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					}
				}
				// for execution on hub
				else if(Launcher.executionMethod.equals("hub"))
				{
					scrFile = ((TakesScreenshot)Launcher.augmentedDriver).getScreenshotAs(OutputType.FILE);
				}
				
				FileUtils.copyFile(scrFile, screenshotFileName);  // copy tempImage file to proper folder in reports
				// code for deleting tempImage
			    File deletingTempImg = new File("tempImage." +imageType);
			    deletingTempImg.delete();
			}
			//// for selenium RC
			else
			{
				scrFile = new File(screenshotFileName.getAbsolutePath());
				try
				{
				selenium.captureEntirePageScreenshot(scrFile.getAbsolutePath(), "");
				}
				catch(Exception ee)
				{
					logger.error("Selenium does not support Entire Page Screenshot for this browser. Therefore taking current-view screenshot.");
					selenium.captureScreenshot(scrFile.getAbsolutePath());
				}
			}
			logger.debug("shreenshot of full page captured");
			//for copying files in last run
			File scrscreenshotFile=new File(screenshotFileName.getAbsolutePath());
			File lastrunfile=new File(reportsPath +"/Last Run/"+ scrscreenshotFile.getName());    //{}
			FileUtils.copyFile(scrscreenshotFile, lastrunfile);
		} 
		
		///////// for taking current view screen shots
		else if(StringUtils.equals(screenCaptureAres, "CURRENT_VIEW")) 
		{
			File scrFile =null;
			
			//// for webdriver
			if (seleniumVariant.contains("webdriver"))
			{
				if(Launcher.executionMethod.toLowerCase().equals("local"))
				{
					ScreenUtil.captureScreen(screenshotFileName, imageType);
				}
				else if(Launcher.executionMethod.toLowerCase().equals("hub"))
				{
					scrFile = ((TakesScreenshot)Launcher.augmentedDriver).getScreenshotAs(OutputType.FILE);
				}
			}
			// for RC
			else
			{
				scrFile = new File(screenshotFileName.getAbsolutePath());
				selenium.captureScreenshot(scrFile.getAbsolutePath());
			}
			
			//for copying files in last run
			File scrscreenshotFile=new File(generateScreenshotFullFileName());
			File lastrunfile=new File(reportsPath + "/Last Run/"+ scrscreenshotFile.getName());    //{}
			FileUtils.copyFile(scrscreenshotFile, lastrunfile);
			logger.debug("shreenshot of current view captured");
		} 
		else
		{
			logger.warn("the value of report.screenshot.area in framework.config file is not valid. screenshot not captured.");
			isScreenCaptured = false;
		}
		
		if(isScreenCaptured) 
		{
			if(isFromQC) {
				actualResult +=" Refer screenshot "+generateScreenshotFileName()+".";
			} else {
				if(reportingType.toLowerCase().equals("html"))
					if (Script.isNotePadFileError == true)
					{
						actualResult +="";
					}
					else
					{
					 actualResult +=" Refer screenshot click <a href="+generateScreenshotFileName()+"> here</a> ";
					}
					String jpgFile=screenshotFileName.getAbsolutePath();
					jpgFile=jpgFile.replace("\\.\\", "\\");
					strReportPath=strReportPath+jpgFile+",";				
			}
			logger.info("Screen captured and saved:"+screenshotFileName.getAbsolutePath());
		}
	  }
	  catch(Exception e)
	  {
		  System.out.println(e.getMessage());
	  }
	  return actualResult;
  }

	//////////////////////////////

	@Override
	public void saveReport() throws FileNotFoundException, IOException {
		if(("HTM".equals(Script.dicConfigValues.get("ReportingFormat"))) || ("HTML".equals(Script.dicConfigValues.get("ReportingFormat"))))
		{
		try
		{
		// String fileName = generateReportFileName();
		
			if(Launcher.chkIteration == true)
				saveReport(filename);
			else
			{
				filename = generateReportFileName();
				saveReport(filename);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			
		}
		}
		else
		{
			saveReport(filename);
		}
	}

	///@ HTML Summary Report by kamlesh  
	@Override
	public String HTMLSummaryReportPath()
	{
		
		return strHTMLSummaryReportPath;
	}
	
	
	@Override
	public void saveReport( String fileName) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		try{
			
		if("HTML".equals(Script.dicConfigValues.get("ReportingFormat")))
		{
//		if (file.exists()) {
//			logger.info(file.getAbsolutePath()
//					+ " already exist. replacing the file.");
//		}
		
		
		byte[] fileContentHeader = getReportHeader();
		if(Launcher.chkIteration != true)
			FileUtils.writeByteArrayToFile(file, fileContentHeader);
		
		
		byte[] fileContent = getReportContent();
		FileUtils.writeByteArrayToFile(file, fileContent, true);
		//String totalduration = HtmlReporter.strExceutionDuration;
		Script.dicConfigValues.put("totalduration", HtmlReporter.strExceutionDuration);
		String htmlFile=file.getAbsolutePath();
		htmlFile=htmlFile.replace("\\.\\", "\\");
		
		//@ added code for HTML Summary Report by kamlesh  
		strHTMLSummaryReportPath = htmlFile;
		////////////////////////////////////////////
		
		
		strReportPath =  strReportPath + htmlFile;		
		
		
		
		logger.info("report saved:" + file.getAbsolutePath());		
		}
		
		String reportsPath = "";  //{}
		if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
        	reportsPath =  Script.dicConfigValues.get("reportsPath");
		else
			reportsPath = properties.getProperty("report.location");
		//Copying file to Last Run folder
		File srcFile=new File(fileName);
		File destFile=new File(reportsPath + "/Last Run/"+ srcFile.getName());
		FileUtils.copyFile(file, destFile);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}

	@Override
	public Boolean isTestPassed() {
		Boolean isPAssed = Boolean.TRUE;
		if(testStepFailCount > 0) {
			isPAssed = Boolean.FALSE;
		}
		return isPAssed;
	}

	@Override
	public String getReportLocation() {
        if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
			return Script.dicConfigValues.get("reportsPath");
		else
		    return properties.getProperty("report.location");
	}

	public  String generateReportFileName() {
		return String.format("%s.%s",generateFileNameWithLocation(),getFileExtension());
	}

	protected String generateScreenshotFileName() {
		return String.format("%s_%d.%s",generateFileName(),testStepNo,properties.getProperty("report.screenshot.fileformat"));
	}

	protected String generateScreenshotFullFileName() {
		 String screenshotFileName = String.format("%s_%d.%s",generateFileNameWithLocation(),testStepNo,properties.getProperty("report.screenshot.fileformat"));
		logger.debug("Scheenshot filename:"+screenshotFileName);
		return screenshotFileName;
	}

	public static String generateFileName() {
		
		return String.format("%s_%s",getTestCaseName(),DateFormatUtils.format(testStartTime, properties.getProperty("report.dateFormat")));
	}
	
	public static String generateFileNameWithLocation() {
        String reportsPath = "";
        if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
        	reportsPath =  Script.dicConfigValues.get("reportsPath");
		else
			reportsPath = properties.getProperty("report.location");
		String fileName = String.format("%s/%s",
		reportsPath+ "/" + Launcher.testSetName + "/",
		generateFileName().split("_")[0] + "//" + generateFileName()
		); //}}
		return fileName;
		}
	public static String getTestCaseName() {
		return testCaseName;
	}

	public String getEnvironment() {
		return environment;
	}

    public String getProjectName() {
		return Script.dicConfigValues.get("ProjectName"); //{}
	}
	public String getBuildVersion() {
		return buildVersion;
	}

	public String getBrowser() {
		return browser;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	protected void incTestStepPassCount() {
		try
		{
		testStepPassCount++;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public Integer getTestStepPassCount() {
		return testStepPassCount;
	}

	protected void incTestStepFailCount() {
		try{
		testStepFailCount++;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public Integer getTestStepFailCount() {
		return testStepFailCount;
	}

	public Integer getTestIterationCount() {
		return testIterationNumber;
	}

	public Date getTestExecutionTime() {
		return testStartTime;
	}

	public Integer getTestStepNo() {
		return testStepNo;
	}

	protected void incTestStepNo() {
		try{
		testStepNo++;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
