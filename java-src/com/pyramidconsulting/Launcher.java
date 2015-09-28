package com.pyramidconsulting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pyramidconsulting.Action.Browser;
import com.pyramidconsulting.common.ArgumentKey;
import com.pyramidconsulting.reader.ExcelReader;
import com.pyramidconsulting.reader.ExcelReaderImpl;
import com.pyramidconsulting.report.AbstractReporter;
import com.pyramidconsulting.report.ExcelReporter;
import com.pyramidconsulting.report.HtmlReporter;
import com.pyramidconsulting.report.Reporter;
import com.pyramidconsulting.runner.ExternalScriptRunner;
import com.pyramidconsulting.runner.Script;
import com.pyramidconsulting.runner.ScriptRunner;
import com.pyramidconsulting.util.JVMUtil;
import com.pyramidconsulting.util.PropertyUtil;
import com.pyramidconsulting.util.QCManager;
import com.pyramidconsulting.util.ZephyrUtil;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class Launcher {

	/**
	 * Data-member Description
	 *  	environment				To contain Environment's value, get from command line argument.
	 *  	hostName				Indicates the host name.
	 *  	applicationUrl			Indicates the application URL.
	 *  	buildVersion			Indicates the build version.
	 *  	browser					Indicates the browser type.
	 *  	reportType				Indicates the report type.
	 *  	appName					To contain application name, get from command line argument.
	 *  	options					To contains the command line argument options.
	 *  	cmd						To get the command line arguments.
	 *  	APPLICATION_NAME_PH		Indicates the application name place holder.
	 *  	ENVIRONMENT_PH			Indicates the Environment place holder.
	 *  	args					To store the command line arguments.
	 *  	properties				To get the values from properties file.
	 */


	/** To produce the logger informations. */
	///@ Added Code For Summary Report//////////////////////////////////////////////
	File file;
	FileWriter oReportSummaryWriter;
	String SummaryPath = null;
	
	public int iSummaryStepCount;
	public int iSummaryPassStepCount = 0;
	public int iSummaryFailStepCount = 0;
	public int iPassTotalCount = 0;
	public int iFailTotalCount = 0;
	public int iNoRunTotalCount = 0;
	public Integer iExceutionTime = new Integer(0);
	public static Integer iteExceutionTime = new Integer(0);
	public static String strStatusHTMLResult = null; 
	public static int intcount = 0;
    //public int iExceutionTime;	
    public static String strfullVersion = null;
	
    ///@ Added Code For Summary Report//////////////////////////////////////////////
    
	private static final Logger logger = Logger.getLogger(Launcher.class);

	private static String environment ;
	//private  String hostName;
	private static  String applicationUrl;
	private static String buildVersion ;
	private static String browser;
	private static  String reportType;
	private static String appName;
	private static Options options = null;
	private static CommandLine cmd = null;
	private static String linkValue = "";

	public static WebDriver driver;
	public static Selenium selenium;
	public static Selenium selenium1;
	/** true if running test from QC, otherwise false */
	private Boolean isFromQC;
	private String testName;
	public static String testSetName;
	private String scriptFile;
	private String testRunName;
	public static String executionMethod;
	public static String HubIP;
	public static WebDriver augmentedDriver;
	static String execId="";
	public static String strReportsPath="";


	private String APPLICATION_NAME_PH = "__APPLICATION_NAME__";
	private String ENVIRONMENT_PH = "__ENVIRONMENT__";
	

	private String[] args = null;

	public static boolean chkIteration=false;
	public  static PropertyUtil properties = new PropertyUtil(Constants.PROPERTY_CONFIG_FILENAME);
	
	
	
	//public static PropertyUtil appConfig = new PropertyUtil(Constants.AppConstants.APP_CONFIG_FILENAME);

	/**
	 * This method returns the valid command line options
	 * @return
	 * 			: [Options]  the valid command line options
	 */
	
    void browserSetting(){
    Runtime runtime = Runtime.getRuntime();
    try
    {
    Process p1 = runtime.exec("cmd /c .\\BrowserSettings\\BrowserSettings.bat");
//    Process p1 = runtime.exec("cmd /c .\\Utilities\\BrowserSettings\\BrowserSettings.bat");
    InputStream is = p1.getInputStream();
    int i = 0;
    while( (i = is.read() ) != -1)
    {
    System.out.print((char)i);
    }

    }
    catch(IOException ioException)
    {
    System.out.println(ioException.getMessage() );
    }
    }

	
	private Options getOptions(){
		options = new Options();
		options.addOption(ArgumentKey.env.name(), true,
				"Environment of the application");
		options.addOption(ArgumentKey.appUrl.name(), true,
				"URL of the application");
		options.addOption(ArgumentKey.hostName.name(), true,
				"Host Name for the application");
		options.addOption(ArgumentKey.browser.name(), true,
				"Browser on which the application is running");
		options.addOption(ArgumentKey.buildVer.name(), true,
				"The Build vresion of the application");
		options.addOption(ArgumentKey.appName.name(), true,
				"Name of the application");
		options.addOption(ArgumentKey.reportType.name(), true,
				"Type of the Report");
		options.addOption(ArgumentKey.tcdfName.name(), true,
				"File Name of the TestCase");
		options.addOption(ArgumentKey.tcName.name(), true,
				" Name of the TestCase");
		options.addOption(ArgumentKey.tsName.name(), true,
				" Name of the TestSet");
		options.addOption(ArgumentKey.tsfName.name(), true,
				"File Name of the TestSuit");
		options.addOption(ArgumentKey.scriptFile.name(), true,
				"Location of Script file");
		options.addOption(ArgumentKey.runName.name(), true,
				"Name of run");
		options.addOption(ArgumentKey.isFromQC.name(), true,
				"Is this test running from QC");

		return options;
	}

	private QCManager getQcManager() {
		QCManager qcManager = null;
		final String qcUrl = Script.dicConfigValues.get("QCURL");		
		final String domainName = Script.dicConfigValues.get("QCDomainName");
		final String projectName = Script.dicConfigValues.get("QCProjectName");
		final String userName = Script.dicConfigValues.get("QCUserName");
		final String password = Script.dicConfigValues.get("QCPassword");
		qcManager = new QCManager(qcUrl, domainName, projectName, userName, password);
		return qcManager;				
	}

		private static Selenium getSelenium() {		
		browser = browser.toUpperCase();
		if ("FIREFOX".equals(browser) || "FF".equals(browser)) {
			selenium = new DefaultSelenium("localhost", 4444, "*firefox", Script.dicConfigValues.get("strApplicationURL"));
		} else if ("INTERNET_EXPLORER".equals(browser) || "IE".equals(browser)) {
			selenium = new DefaultSelenium("localhost", 4444, "*iexplore", Script.dicConfigValues.get("strApplicationURL"));
		} else if("CHROME".equals(browser)) {
			selenium = new DefaultSelenium("localhost", 4444, "*googlechrome", Script.dicConfigValues.get("strApplicationURL"));
		} else if("SAFARI".equals(browser)) {
			selenium = new DefaultSelenium("localhost", 4444, "*safari", Script.dicConfigValues.get("strApplicationURL"));
		} else  {
			logger.error("Unsupported browser:"+browser);
			throw new RuntimeException("Unsupported browser:"+browser);
		}
		
		return selenium;
	}

	
	
	public static void startSeleniumServer(String serverJarFileName)
	{
		//*Note: serverJarFileName passed should not contain any special characters or spaces, otherwise server will not be started using cmd.
		String[] cmd = new String [3];
		String current = "";
		try 
		{
			current = new java.io.File( "." ).getCanonicalPath();
		} 
		catch (IOException e1) 
		{
			logger.error("Problem in fetching selenium server standalone jar");
		}
		String serverJarFile ="";
		
		File files = new File("./jar");
		File[] allFiles =files.listFiles();
		
		if(!serverJarFileName.equals(""))
		{
		for(File strFile:allFiles)
		{
			if(strFile.getName().equals(serverJarFileName))
			{
				serverJarFile=serverJarFileName;
				break;
			}
		 }
		}
		else
		{
			String[] brokenFileName;
			String fileversion;
			String fileversion2;
			String[] version;
			String[] version2;
			logger.info("Jar File mentioned in datasheet not found, trying to execute the latest server jar file");
			for(File strFile:allFiles)
			{
				if(strFile.getName().contains("selenium-server-standalone-"))
				{
					serverJarFileName = strFile.getName();
					brokenFileName = strFile.getName().split("-");
					fileversion = brokenFileName[3];
					version = fileversion.split("\\.");
					for(File strFiless: allFiles)
					{
						if(strFiless.getName().contains("selenium-server-standalone-"))
						{
							brokenFileName = strFiless.getName().split("-");
							fileversion2 = brokenFileName[3];
							version2 = fileversion2.split("\\.");
							if(Integer.parseInt(version2[2]) > Integer.parseInt(version[2]) || (Integer.parseInt(version2[1]) > Integer.parseInt(version[1])) || (Integer.parseInt(version2[0]) > Integer.parseInt(version[0])))
							{
								if(Integer.parseInt(version2[1]) > Integer.parseInt(version[1]) || (Integer.parseInt(version2[0]) > Integer.parseInt(version[0])))
								{
									if(Integer.parseInt(version2[0]) > Integer.parseInt(version[0]))
									{
										serverJarFileName = strFiless.getName();
										serverJarFile=strFiless.getName();
									}
								}
							}
							
						}
					}
					
				}
			 }
		serverJarFile = serverJarFileName;	
		}
		try 
		{
			logger.info("Using selenium server Jar file :" + serverJarFile);
			Runtime.getRuntime().exec("java -jar "+ current +"\\jar\\" + serverJarFile);
		} 
		catch (Exception e) 
		{
			logger.error("Problem in starting Selenium Server");
		}
	}
	
	
	/**
	 * This is the main method of the Launcher class to start execution
	 * @param args
	 * 				: Refers to the command line arguments
	 * @throws Exception
	 * 				: This method may throw the Exception type exception
	 */
	
	
	public static void main(final String[] args) throws Exception 
	{
		String dataSheetFileName;
		String testSuiteFileName;
		String testCaseName;
		String iterationsRange;
//		int TestCaseId;
		String TestCaseId;
		String TestName;
		String sheetn;
		killProcess("rundll32.exe");
		killProcess("EXCEL.EXE");
		killProcess("chromedriver.exe");
				
		//String HubIP;
		putKeyValuesFromSheetToMap(".\\Datasheets\\Config.xls","Config", 1, "TestParameters", 0, 1, Script.dicConfigValues );
		String ExecutionVariant=Script.dicConfigValues.get("ExecutionVariant");
		String jiraRunonlyNotPassed=Script.dicConfigValues.get("JiraRunonlyNotPassedTests");		
		String jiraProjectName = Script.dicConfigValues.get("JiraProjectName");
		String jiraCycleName = Script.dicConfigValues.get("JiraCycleName");
        String jiraVersion = Script.dicConfigValues.get("JiraFixVersion");
		String jiraLogDefect = Script.dicConfigValues.get("JiraLogDefect");
		String jiraDefectAssignee = Script.dicConfigValues.get("JiraDefectAssignee");
		        
        //Call ZephyrUtill and get all the test cases in JIRA for a specific project/cycle name, if JIRA status is requested for in Config
        
        if(ExecutionVariant.equalsIgnoreCase("jira")||ExecutionVariant.equalsIgnoreCase("TestSuite/Results in JIRA")||jiraLogDefect.equalsIgnoreCase("YES"))
        {                                                                                                                              

                        try 
                        {
                        	
                        				ZephyrUtil.setCredentials(Script.dicConfigValues.get("JiraUser")+":"+Script.dicConfigValues.get("JiraPassword"));
                        	
                                        String jiraProjectNameEncoded = URLEncoder.encode(jiraProjectName, "UTF-8");
                                        
                                        String jiraCycleNameEncoded = URLEncoder.encode(jiraCycleName, "UTF-8");
                                        
                                        String jiraVersionEncoded = URLEncoder.encode(jiraVersion, "UTF-8");
                                        
                                        //Code added by Aarti      
                                        // If it is not necessary to run only the non-passed test cases from JIRA
                                        if(!jiraRunonlyNotPassed.equalsIgnoreCase("Yes"))
                                        {                                        	
	                                        String zephyrQuery = "project=\""+jiraProjectNameEncoded+"\"%20AND%20fixVersion=\""+jiraVersion+"\"%20AND%20cycleName%20in(\""+jiraCycleNameEncoded+"\")&maxRecords=100000&expand=executions&offset=0";                                        
	                                                                                
	                                        ZephyrUtil.zqlExecuteSearch(zephyrQuery, "executions");
	                                        
	                                        logger.info("Jira integration requested in config file and ALL the test case names under a specific Project/Cycle/Fix Version are fetched successfully.");	                                                                    
	                                        
                                        }
                                        // Running only non-passed test cases from JIRA
                                        else
                                        {
                                        	String zephyrQuery = "project=\""+jiraProjectNameEncoded+"\"%20AND%20fixVersion=\""+jiraVersion+"\"%20AND%20cycleName%20in(\""+jiraCycleNameEncoded+"\")%20AND%20executionStatus%20not%20in(\"PASS\")&maxRecords=100000&expand=executions&offset=0";                                        
                                            
	                                        ZephyrUtil.zqlExecuteSearch(zephyrQuery, "executions");
	                                        
	                                        logger.info("Jira integration requested in config file and All the non-passed test case names under a specific Project/Cycle/Fix Version are fetched successfully.");
                                        	
                                        }
                                        
                                        // Update all eligible test cases in the cycle to UNEXECUTED
                                        // Uncomment the code below if there is ever a use case to retain old results for the scenario where Buggy scripts need to have previous results
                                       // boolean blnUpdateUnex = Script.dicConfigValues.get("AccessTestSuite").equalsIgnoreCase("YES")&&jiraIntegration.equalsIgnoreCase("No");
                                        // If running from Test Suite, and we do not update results on JIRA, do not make scripts Unexecuted
                                        //if(!blnUpdateUnex)
                                        //{
	                                        boolean blnStatus = ZephyrUtil.updateAllTestExecutions("UNEXECUTED");
	                                        
	                                        if(blnStatus)
	                                        	logger.info("All test executions to be run in the cycle ("+jiraCycleName+") are updated to 'UNEXECUTED' status at the start of execution.");
	                            			else
	                            				logger.error("All test executions to be run in the cycle ("+jiraCycleName+") are not updated to 'UNEXECUTED' status at the start of execution.");
                                        //}
                                        //else
                                        	//logger.info("The test executions to be run in cycle ("+jiraCycleName+") are not updated to 'UNEXECUTED' status, since we would not be updating the results at the end of eexecution.");
                        }
                        catch (UnsupportedEncodingException ignored) 
                        {
                            // Can be safely ignored because UTF-8 is always supported
                        }
        }
        else
        {
                        logger.info("Jira integration not requested in config file.");
        }


		dataSheetFileName =Script.dicConfigValues.get("MasterTestDataPath");
		if(dataSheetFileName.equals(null) || dataSheetFileName.equals(""))
			dataSheetFileName=properties.getProperty("testdata.location")+properties.getProperty("template.testData.fileName")+Script.dicConfigValues.get("strEnvironment") + ".xls";
		
		if(!Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
			startSeleniumServer(Script.dicConfigValues.get("ServerName"));
		
		final Launcher launcher = new Launcher();
		launcher.browserSetting();
		launcher.init(args);
		driver=null;
		selenium=null;
		selenium1 =null;
		if(args.length > 1)
		{
			try{
			
			// getting arguments from launcher.bat
			/* 
			 TestcaseID = args[0]
			 rowidtestcasename (test case name in testsuite) = args[1]
			 iterationsRange = args[2]
			 sheetn (testsetname ; name of sheet in testsuite) = args[3]
			 datasheetfilename= args[4]
			 testsuitefilename = args[5]
			 HubIP = args[6]
			 */
			
			
 			logger.info(args.length + "\n");
			logger.info("str args[]" + args[0]);
//			TestCaseId = (int)Float.valueOf(args[0]).floatValue(); 
			TestCaseId = args[0]; 
			String rowIDtestCaseName = args[1];
 			testCaseName = rowIDtestCaseName;
			iterationsRange = args[2];
			sheetn =args[3].substring(0,args[3].indexOf("$"));
			testSetName = sheetn;
			//dataSheetFileName = args[4];
			testSuiteFileName=args[4];
			HubIP = args[5];
			executionMethod = "hub";
			
			
			logger.info(TestCaseId +"**"+ testCaseName +"**"+ iterationsRange);
			
			POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(properties.getProperty("testsuite.location")+testSuiteFileName+".xls")); 
			HSSFWorkbook workbook = new HSSFWorkbook(fileSystem); 
			
			final String orFileName = launcher.getORFileName();
			final ExcelReader td = new ExcelReaderImpl(dataSheetFileName);
			final ExcelReader or = new ExcelReaderImpl(orFileName);
			
			if(launcher.scriptFile!=null) 
			{
				logger.info("Running test script:"+launcher.scriptFile);
				//***************
				if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
				{
					driver = launcher.getWebDriver();	
				}
				else
				{
					try
					{
					selenium=getSelenium();
					selenium.start();
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
				launcher.runScript(new File(launcher.scriptFile), selenium, driver, td,or);
				
			} 
			
			else
			{
			File f11 = new File("./java-Testscripts");
			File[] f21 =f11.listFiles();
			for(File strFile1:f21)
			{
				 File[] f3 =strFile1.listFiles();
				 for(File strFile11:f3)
				 {
					 if(strFile11.getName().equals(rowIDtestCaseName))
					 strFile11.delete();
				 }
			 }
			
			String iteratefrom = iterationsRange.split("-")[0];   // split iterations by "_" for "from" to "to" values of iterations
			String iterateto = iterationsRange.split("-")[1];     //
			
			// Convert into int values  
			int iterateFrom=(int)Float.valueOf(iteratefrom).floatValue();  
			int iterateTo=(int)Float.valueOf(iterateto).floatValue();      
			 
			String rowIdModified1 = testCaseName.replaceAll(" : ", "");
			String rowIdModified2 = rowIdModified1.replaceAll(" - ", "");
			String rowIdModified = rowIdModified2.replaceAll(" ", "");
			testCaseName = TestCaseId + "_" + rowIdModified;   
			
			putKeyValuesFromSheetToMap(dataSheetFileName,"CommonValues", 0, "ITEM", 0, 1, Script.dicCommonValue);
			
			
			if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
			{
				driver = launcher.getWebDriver();	
			}
			else
			{
				try
				{
				//selenium = new DefaultSelenium("localhost", 4444, Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
					selenium = getSelenium();
				selenium.start();
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
			
			Reporter reporter = launcher.getReporter(testCaseName, selenium, driver);
			
			// deleting files from LAST RUN Reports folder
			String reportsLocation = "";  //{}
			if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
				reportsLocation = Script.dicConfigValues.get("reportsPath");
			else
			reportsLocation = properties.getProperty("report.location");
		
			//@ Added code for summary report
			launcher.SummaryPath = reportsLocation;
			
			File f1 = new File(reportsLocation);
			if(!f1.isDirectory()) //{}
			(new File(reportsLocation)).mkdirs();
			if(!f1.isDirectory())
			(new File(reportsLocation)).mkdir();
			(new File(reportsLocation + "\\Last Run")).mkdir();

			String testsetfolder = reportsLocation+ "/" + Launcher.testSetName + "/" + testCaseName.split("_")[0];
			File testSetFolder = new File(testsetfolder);
			if(!testSetFolder.isDirectory())
			{
			(new File(testsetfolder)).mkdirs();
			if(!testSetFolder.isDirectory())
			(new File(testsetfolder)).mkdir();
			}

			
			//File f1 = new File(properties.getProperty("report.location"));
			 File[] f2 =f1.listFiles();
			 for(File strFile:f2)
			 {

				 if(strFile.getName().equals("Last Run"))
				 {
					 File[] f3 =strFile.listFiles();
					 for(File strFile1:f3)
					 {
						 strFile1.delete();
					 }
					 break;
				 }
			 }
			 
			 //loop for multiple iterations
			int howmanytimesiterated=0;
			for(int i=iterateFrom;i<=iterateTo;i++)
			{
				AbstractReporter.testIterationNumber =i;
				
				Script.dicTestData.clear();      // clears the test specific data from
                getTestDataFromDataSheet(sheetn,rowIDtestCaseName ,dataSheetFileName, i); 
				if(howmanytimesiterated>=1)
					Launcher.chkIteration=true;
				howmanytimesiterated++;
				logger.info("Running testcase:" + testCaseName);
				reporter.setIsFromQC(launcher.isFromQC);
				String dataSheetName = sheetn;
				ExcelReporter excelreporter=new ExcelReporter(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl, selenium, driver);
				if("XLS".equals(reportType) || ("EXCEL".equals(reportType)))
				{													
					excelreporter.Enterdatainexcel(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl);							
				}
				
				 ScriptRunner scriptRunner = new ExternalScriptRunner(
						selenium, driver, or, td, reporter,testCaseName);

				 String testScriptFile =  properties.getProperty("script.file.location") + "/" +testSetName + "//" + testCaseName;
				 HSSFSheet sheet = workbook.getSheet(testSetName);
			 
				//script starts running
				scriptRunner.runScript(testScriptFile);
				
				boolean isPassed1=reporter.isTestPassed();
		
				
				int n = findRowNumber(sheet, rowIDtestCaseName);
				
				if (reporter != null) 
				{
						reporter.saveReport();									
					//***************************************
					//***************************************
					boolean isPassed=reporter.isTestPassed();
					
					//@ Added Script.isNotePadFileError flag to make script fail [Compile error or Script not present in dedicated folder]
					////////////////Code added by kamlesh for Summary Report/////////////////
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////											
					if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
					{
						if (intcount == 0)
						{
							launcher.PrepSummaryReport(launcher.SummaryPath);
							launcher.NewSummaryHeader();	
							
						}

						
						String strResult = null;
						
						if(isPassed)
						{
							strResult = "PASS";
						}
						else
						{
							strResult = "FAIL";
						}
						

							
						//launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
						launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
						if (iterateTo == 1)
						{
							launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
						}
						else
						{
							//@ Code for Iteration
							//launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
						   String[] iteDate=Script.dicConfigValues.get("totalduration").split(":");  
							  
							int itehour=Integer.parseInt(iteDate[0]);  
							int iteminute=Integer.parseInt(iteDate[1]);  
							int itesecond=Integer.parseInt(iteDate[2]);  
							int iteTemp = itesecond + (60 * iteminute) + (3600 * itehour);
							iteExceutionTime = iteExceutionTime + iteTemp;
							
							if (strResult == "FAIL")
							{
								strStatusHTMLResult = strResult;
							}
							
							if (howmanytimesiterated == iterateTo)
							{
								if(strStatusHTMLResult == null)
									strStatusHTMLResult = strResult;
								launcher.HTMLSummaryReport(testCaseName, strStatusHTMLResult, new SimpleDateFormat("HH:mm:ss"){{setTimeZone(TimeZone.getTimeZone("UTC"));}}.format(new Date(iteExceutionTime * 1000)).toString(), "file:///"+reporter.HTMLSummaryReportPath());
							}
						}
						
					}
               ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					
					
					//@ Added Script.isNotePadFileError flag to make script fail [Compile error or Script not present in dedicated folder]
					if(Script.isNotePadFileError == true)
						isPassed = false;
					String linkValueActual="";
					String execStatus="";
					if(isPassed)
					{		
						execStatus="Pass";
						HSSFRow row =sheet.getRow((short)n);							
						HSSFCell cell = row.getCell(6);							
						if (cell == null)    
						    cell = row.createCell(6);							
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						HSSFCellStyle titleStyle = workbook.createCellStyle();
						titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);
						titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cell.setCellStyle(titleStyle);
						
						cell.setCellValue(new HSSFRichTextString("PASS"));							
						FileOutputStream fout = new FileOutputStream(properties.getProperty("testsuite.location")+testSuiteFileName+".xls");						
						workbook.write(fout);
						fout.flush();
						fout.close();
						
						HSSFCell cell2 = row.createCell(7);							
						if (cell2 == null)    
						    cell2 = row.createCell(7);
						cell2.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						//File file=new File(properties.getProperty("report.location") + "/" +testSetName + "//" + AbstractReporter.generateFileName()  + "." + reportType);
						//String linkValue=file.getAbsolutePath();
						File file=new File(AbstractReporter.generateFileNameWithLocation()  + "." + reportType);
						if(linkValue == "")
						{
						linkValue = file.getAbsolutePath();
						linkValueActual = linkValue.replace("\\.\\", "\\");
						String actualValue="Report Link";
						cell2.setCellFormula("HYPERLINK(\"" + linkValueActual+ "\",\"" + actualValue + "\")");
						FileOutputStream fout2 = new FileOutputStream(properties.getProperty("testsuite.location")+testSuiteFileName+ ".xls");						
						workbook.write(fout2);
						fout2.flush();
						fout2.close();
						}
						AbstractReporter.testStepFailCount=0;
						
					}
					else
					{							
						execStatus="Fail";
						HSSFRow row =sheet.getRow((short)n);													
						HSSFCell cell = row.getCell(6);							
						if (cell == null) 
						    cell = row.createCell(6);							
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						HSSFCellStyle titleStyle = workbook.createCellStyle();
						titleStyle.setFillForegroundColor(HSSFColor.RED.index);
						titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cell.setCellStyle(titleStyle);
						
						cell.setCellValue(new HSSFRichTextString("FAIL"));							
						FileOutputStream fout = new FileOutputStream(properties.getProperty("testsuite.location")+testSuiteFileName+".xls");						
						workbook.write(fout);
						fout.flush();
						fout.close();
					
						HSSFCell cell2 = row.createCell(7);						
						if (cell2 == null)    
						    cell2 = row.createCell(7);
						cell2.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						//File file=new File(properties.getProperty("report.location") + "/" +testSetName + "//" + AbstractReporter.generateFileNameWithLocation()  + "." + reportType);
						File file=new File(AbstractReporter.generateFileNameWithLocation() + "." + reportType);  //{}
						//String linkValue=file.getAbsolutePath();
						if(linkValue == "")
						{
						linkValue = file.getAbsolutePath();
						linkValueActual = linkValue.replace("\\.\\", "\\");
						String actualValue="Report Link";
						cell2.setCellFormula("HYPERLINK(\"" + linkValueActual+ "\",\"" + actualValue + "\")");
						FileOutputStream fout2 = new FileOutputStream(properties.getProperty("testsuite.location")+testSuiteFileName+".xls");						
						workbook.write(fout2);
						fout2.flush();
						fout2.close();
						}
						ExcelReporter.a.clear();
						AbstractReporter.testStepFailCount=0;
					}
					
					//@ Added code by Kamlesh
					linkValue = "";
					
					//***************** closing all the "WebDriver" browser windows ********** ///////////////
					if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
					{
						Set<String> windowHandler = new HashSet<String>(5);
						windowHandler = driver.getWindowHandles();
						for(String browserWindowHandle : windowHandler)
							driver.switchTo().window(browserWindowHandle).close();
					}
					else
					{
						launcher.shutDown(selenium);
						selenium.stop();
						if(!(selenium1 == null))
						{
							launcher.shutDown(selenium1);
							selenium1.stop();
						}
					}
			}
			
			if((i>=iterateFrom)&&(i<iterateTo))
			{
				
				if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
				{
					driver = launcher.getWebDriver();	
				}
				else
				{
					try
					{
					//selenium = new DefaultSelenium("localhost", 4444,Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
					selenium = getSelenium();
					selenium.start();
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
				
				reporter = launcher.getReporter(testCaseName, selenium, driver); 
			    logger.info("Script end");
			}
		}
		Launcher.chkIteration=false;
		AbstractReporter.filename=null;
			}
		}
		catch(Exception ee)
		{
			logger.error(ee.getMessage());
		}
		finally 
		{
			if(!Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
			{
				try{
				launcher.shutDown(selenium);		// in case of exception , the last code to be executed is this, which will close the browser window
				}
				catch(Exception e)
				{
					logger.info("No other selenium RC window Opened");
				}
				if(!(selenium1 == null))
				{
					launcher.shutDown(selenium1);
					selenium1.stop();
				}
			}
			else
			{
				// in case of exception , the last code to be executed is this, which will close the webdriver browser window
				try{
				driver.close();
				}
				catch(Exception e)
				{
					logger.info("No other Driver window opened.");
				}
			}
				killProcess("chromedriver.exe");
				logger.info("Script end");
				//@ Summary Report :- added by kamlesh
				if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
				{
				 launcher.EndSummaryTestReport();
				}				
				
			}
		}
		
		 
		else
		{
			if(!ExecutionVariant.equalsIgnoreCase("jira"))
			{
					HubIP = null;
					executionMethod = "local";
					
				
				dataSheetFileName =Script.dicConfigValues.get("MasterTestDataPath");
				if(dataSheetFileName.equals(null) || dataSheetFileName.equals(""))
					dataSheetFileName=properties.getProperty("testdata.location")+properties.getProperty("template.testData.fileName")+Script.dicConfigValues.get("strEnvironment") + ".xls";
				final String orFileName = launcher.getORFileName();
				logger.debug("datasheet filename:" + dataSheetFileName);
				final ExcelReader td = new ExcelReaderImpl(dataSheetFileName);
				final ExcelReader or = new ExcelReaderImpl(orFileName);
				testSuiteFileName = launcher.getTestSuiteFileName();
				ExcelReader testSuite = new ExcelReaderImpl(testSuiteFileName);

				// the value of this column will uniquely identify each row
				testSuite.setRowIdentifierColName("TestCaseName");
				POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(testSuiteFileName)); 
				HSSFWorkbook workbook = new HSSFWorkbook(fileSystem); 
				int strSheetCount = workbook.getNumberOfSheets();
				
				System.out.println("total number of sheets are : "+strSheetCount);
				 //////Put values from configValues of dataSheet in dicCommonValue map ////
			    //getCommonValuesFromDataSheet(dataSheetFileName);
				putKeyValuesFromSheetToMap(dataSheetFileName,"CommonValues", 0, "ITEM", 0, 1, Script.dicCommonValue);
				
				try  	
				{
					for(int count=0;count<strSheetCount;count++)
					{
						int n=0;
						testSuite.setDefaultSheetName(count);
						System.out.println("count loop "+ count);		
					 	HSSFSheet sheet = workbook.getSheetAt(count);
					  	sheetn=sheet.getSheetName();					  
					  	System.out.println(sheetn);
					  	testSetName = sheetn;
					  	workbook.setActiveSheet(count);
					  	System.out.println("Active sheet num: "+ workbook.getActiveSheetIndex());
					  	testSuite.setRowIdentifierColName("TestCaseName");
					  	String rowIds[] = testSuite.getAllRowIdentifierValues();
			
						if(launcher.scriptFile!=null) 
						{
							logger.info("Running test script:"+launcher.scriptFile);
							//***************
							if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
							{
								driver = launcher.getWebDriver();	
							}
							else
							{
								try
								{
								selenium=getSelenium();
								selenium.start();
								}
								catch(Exception e)
								{
									System.out.println(e.getMessage());
								}
							}
							launcher.runScript(new File(launcher.scriptFile), selenium, driver, td,or);
						} 
						else 
						{
							logger.info("running testsuite:" + testSuiteFileName);
							for (String rowId : rowIds)
							{				
								if(rowId!="")     // rowIds[] contains extra values which have no data i.e where testCaseName is also "" are added in this array
								{
								n=n+1;
								String runValue = testSuite.getValue(rowId, "Run");
								if (StringUtils.equalsIgnoreCase(runValue, "Y")) 
								{
									File f11 = new File("./java-Testscripts");
									File[] f21 =f11.listFiles();
									for(File strFile1:f21)
									{
										 File[] f3 =strFile1.listFiles();
										 for(File strFile11:f3)
										 {
											 if(strFile11.getName().equals(rowId))
											 strFile11.delete();
										 }
									 }
									
										
									// get iteration from test suite xls file
									iterationsRange=testSuite.getValue(rowId, "Iteration");   // eg. 1-2
									String iteratefrom = iterationsRange.split("-")[0];   // split iterations by "_" for "from" to "to" values of iterations
									String iterateto = iterationsRange.split("-")[1];     //
									
									// Convert into int values  
									int iterateFrom=(int)Float.valueOf(iteratefrom).floatValue();  
									int iterateTo=(int)Float.valueOf(iterateto).floatValue();      
									 
									//append TestCaseId as a prefix of the testCaseName
//									TestCaseId=(int)Float.valueOf(testSuite.getValue(rowId, "TestCaseId")).floatValue(); 
									TestCaseId=testSuite.getValue(rowId, "TestCaseId");
									String rowIdModified1 = rowId.replaceAll(" : ", "");
									String rowIdModified2 = rowIdModified1.replaceAll(" - ", "");
									String rowIdModified = rowIdModified2.replaceAll(" ", "");
									testCaseName = TestCaseId + "_" + rowIdModified;   
									//driver object
									//selenium = new WebDriverBackedSelenium(driver, "");
									if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
									{
										driver = launcher.getWebDriver();	
									}
									else
									{
										try
										{
										//selenium = new DefaultSelenium("localhost", 4444, Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
											selenium = getSelenium();
										selenium.start();
										}
										catch(Exception e)
										{
											System.out.println(e.getMessage());
										}
									}
									
									
									Reporter reporter = launcher.getReporter(testCaseName, selenium, driver);
									
									
									// deleting files from LAST RUN Reports folder
									String reportsLocation = "";  //{}
									if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
										reportsLocation = Script.dicConfigValues.get("reportsPath");
									else
									reportsLocation = properties.getProperty("report.location");
								

									//@ Added Code for Summary Report
									launcher.SummaryPath = reportsLocation;
									
									
									File f1 = new File(reportsLocation);
									if(!f1.isDirectory()) //{}
									(new File(reportsLocation)).mkdirs();
									if(!f1.isDirectory())
									(new File(reportsLocation)).mkdir();
									(new File(reportsLocation + "\\Last Run")).mkdir();
			
									String testsetfolder = reportsLocation+ "/" + Launcher.testSetName + "/" + testCaseName.split("_")[0];
									File testSetFolder = new File(testsetfolder);
									if(!testSetFolder.isDirectory())
									{
									(new File(testsetfolder)).mkdirs();
									if(!testSetFolder.isDirectory())
									(new File(testsetfolder)).mkdir();
									}
			
									
									//File f1 = new File(properties.getProperty("report.location"));
									 File[] f2 =f1.listFiles();
									 for(File strFile:f2)
									 {
				
										 if(strFile.getName().equals("Last Run"))
										 {
											 File[] f3 =strFile.listFiles();
											 for(File strFile1:f3)
											 {
												 strFile1.delete();
											 }
											 break;
										 }
									 }
									 
									 //loop for multiple iterations
									int howmanytimesiterated=0;
									for(int i=iterateFrom;i<=iterateTo;i++)
									{
										AbstractReporter.testIterationNumber =i;
										Script.dicTestData.clear();      // clears the test specific data from
					                    getTestDataFromDataSheet(sheetn,rowId ,dataSheetFileName , i); 
										if(howmanytimesiterated>=1)
											Launcher.chkIteration=true;
										howmanytimesiterated++;
										logger.info("Running testcase:" + testCaseName);
										reporter.setIsFromQC(launcher.isFromQC);
										String dataSheetName = sheetn;
										
											 
										// if folder name is provided in testsuite then use this value as sheet name
										if (StringUtils.isNotEmpty(dataSheetName)) 
										{
											td.setDefaultSheetName(dataSheetName);
										} 
										else 
										{ // otherwise take first sheet as a default sheet for data
											td.setDefaultSheetName(0);
										}
										ExcelReporter excelreporter=new ExcelReporter(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl, selenium, driver);
										if("XLS".equals(reportType) || ("EXCEL".equals(reportType)))
										{													
											excelreporter.Enterdatainexcel(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl);							
										}
										
										 ScriptRunner scriptRunner = new ExternalScriptRunner(
												selenium, driver, or, td, reporter,testCaseName);
					
										 String testScriptFile =  properties.getProperty("script.file.location") + "/" +testSetName + "//" + testCaseName;
										
										 // @ Deleting the pre-compiled class files from bin folder to avoid not necessary debugging while updation of code.
									 		final String packageName = JVMUtil.generatePackageName();
									 		final File root = new File(
													properties.getProperty("script.java.class.location"),
													packageName.replace(".", "/"));
											FileUtils.cleanDirectory(root);
										
										//script starts running
										scriptRunner.runScript(testScriptFile);
										boolean isPassed1=reporter.isTestPassed();
										 
										
										if (reporter != null) 
										{
												reporter.saveReport();									
											//***************************************
											//***************************************
											boolean isPassed=reporter.isTestPassed();
											
											
											//@ Added Script.isNotePadFileError flag to make script fail [Compile error or Script not present in dedicated folder]
											////////////////Code added by kamlesh for Summary Report/////////////////
											//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////											
											if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
											{
												if (intcount == 0)
												{
													launcher.PrepSummaryReport(launcher.SummaryPath);
													launcher.NewSummaryHeader();	
													
												}
	
												
												String strResult = null;
												
												if(isPassed)
												{
													strResult = "PASS";
												}
												else
												{
													strResult = "FAIL";
												}
												

													
												//launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
												launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
												if (iterateTo == 1)
												{
													launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
												}
												else
												{
													//@ Code for Iteration
													//launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
												   String[] iteDate=Script.dicConfigValues.get("totalduration").split(":");  
													  
													int itehour=Integer.parseInt(iteDate[0]);  
													int iteminute=Integer.parseInt(iteDate[1]);  
													int itesecond=Integer.parseInt(iteDate[2]);  
													int iteTemp = itesecond + (60 * iteminute) + (3600 * itehour);
													iteExceutionTime = iteExceutionTime + iteTemp;
													
													if (strResult == "FAIL")
													{
														strStatusHTMLResult = strResult;
													}
													
													if (howmanytimesiterated == iterateTo)
													{
														if(strStatusHTMLResult == null)
															strStatusHTMLResult = strResult;
														launcher.HTMLSummaryReport(testCaseName, strStatusHTMLResult, new SimpleDateFormat("HH:mm:ss"){{setTimeZone(TimeZone.getTimeZone("UTC"));}}.format(new Date(iteExceutionTime * 1000)).toString(), "file:///"+reporter.HTMLSummaryReportPath());
													}
												}
												
											}
                                       ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
											
											if(Script.isNotePadFileError == true)
												isPassed = false;
											
											String linkValueActual = "";
											String execStatus = "";
											if(isPassed)
											{								
												execStatus = "PASS";
												HSSFRow row =sheet.getRow((short)n);							
												HSSFCell cell = row.getCell(6);							
												if (cell == null)    
												    cell = row.createCell(6);							
												cell.setCellType(HSSFCell.CELL_TYPE_STRING);
												HSSFCellStyle titleStyle = workbook.createCellStyle();
												titleStyle.setFillForegroundColor(HSSFColor.GREEN.index);
												titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
												cell.setCellStyle(titleStyle);
												
												cell.setCellValue(new HSSFRichTextString(execStatus));							
												FileOutputStream fout = new FileOutputStream(testSuiteFileName);						
												workbook.write(fout);
												fout.flush();
												fout.close();
												
												HSSFCell cell2 = row.createCell(7);							
												if (cell2 == null)    
												    cell2 = row.createCell(7);
												cell2.setCellType(HSSFCell.CELL_TYPE_FORMULA);
												//File file=new File(properties.getProperty("report.location") + "/" +testSetName + "//" + AbstractReporter.generateFileName()  + "." + reportType);
												//String linkValue=file.getAbsolutePath();
												File file=new File(AbstractReporter.generateFileNameWithLocation()  + "." + reportType);
												if(linkValue == "")
												{
												linkValue = file.getAbsolutePath();
												linkValueActual = linkValue.replace("\\.\\", "\\");
												String actualValue="Report Link";
												cell2.setCellFormula("HYPERLINK(\"" + linkValueActual+ "\",\"" + actualValue + "\")");
												FileOutputStream fout2 = new FileOutputStream(testSuiteFileName);						
												workbook.write(fout2);
												fout2.flush();
												fout2.close();
												}
												AbstractReporter.testStepFailCount=0;
												
											}
											else
											{
												execStatus = "FAIL";
												HSSFRow row =sheet.getRow((short)n);													
												HSSFCell cell = row.getCell(6);							
												if (cell == null) 
												    cell = row.createCell(6);							
												cell.setCellType(HSSFCell.CELL_TYPE_STRING);
												HSSFCellStyle titleStyle = workbook.createCellStyle();
												titleStyle.setFillForegroundColor(HSSFColor.RED.index);
												titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
												cell.setCellStyle(titleStyle);
												
												cell.setCellValue(new HSSFRichTextString(execStatus));							
												FileOutputStream fout = new FileOutputStream(testSuiteFileName);						
												workbook.write(fout);
												fout.flush();
												fout.close();
											
												HSSFCell cell2 = row.createCell(7);						
												if (cell2 == null)    
												    cell2 = row.createCell(7);
												cell2.setCellType(HSSFCell.CELL_TYPE_FORMULA);
												//File file=new File(properties.getProperty("report.location") + "/" +testSetName + "//" + AbstractReporter.generateFileNameWithLocation()  + "." + reportType);
												File file=new File(AbstractReporter.generateFileNameWithLocation() + "." + reportType);  //{}
												//String linkValue=file.getAbsolutePath();
												if(linkValue == "")
												{
												linkValue = file.getAbsolutePath();
												linkValueActual = linkValue.replace("\\.\\", "\\");
												String actualValue="Report Link";
												cell2.setCellFormula("HYPERLINK(\"" + linkValueActual+ "\",\"" + actualValue + "\")");
												FileOutputStream fout2 = new FileOutputStream(testSuiteFileName);						
												workbook.write(fout2);
												fout2.flush();
												fout2.close();
												}
												ExcelReporter.a.clear();
												AbstractReporter.testStepFailCount=0;
												//@ added code by Aarti
												if(jiraLogDefect.equalsIgnoreCase("Yes"))
												{
													// Get the Execution ID/ Test Summary of the failed test script																			
													execId = ZephyrUtil.getExecutionMap().get(jiraCycleName+"~"+TestCaseId);
													String strTestSummary = ZephyrUtil.getTestSummary(execId);
													// Create a bug in JIRA with the for the failed test script													
													String strBugKey = ZephyrUtil.addIssue(jiraProjectName,"The functionality ("+strTestSummary+") failed.","The functionality ("+strTestSummary+") failed during automation script replay. Please refer to attachments.",jiraDefectAssignee);													
													strReportsPath=AbstractReporter.strReportPath;
													// Add reports and screenshots to the raised bug in JIRA
													ZephyrUtil.addAttachmenttoBug(strReportsPath,strBugKey);
													// Link the bug raised to the failed script in JIRA
													ZephyrUtil.linkDefect(execId,strBugKey);
													logger.info("Issue '"+strBugKey+"' has been raised for the failed test script '"+TestCaseId+"' in Cycle '"+jiraCycleName+"' and Project '"+jiraProjectName+"'. The issue has been linked to the failed test script in JIRA.");
												}
												else
												{
													logger.info("Jira Integration for raising Bugs is not requested.");
												}
											}
											
											//@ added code by Kamlesh
											linkValue = "";
											
											//Call ZephyrUtill updateExecutionStatus and addAtachment method
											if(ExecutionVariant.equalsIgnoreCase("JIRA")||ExecutionVariant.equalsIgnoreCase("TestSuite/Results in JIRA"))
											{
												strReportsPath=AbstractReporter.strReportPath;
												// Get the Execution ID based on the Cycle Name and Test Case Id and Fix Version																			
												execId = ZephyrUtil.getExecutionMap().get(jiraCycleName+"~"+TestCaseId);
												
												//ZephyrUtil.setCredentials(Script.dicConfigValues.get("JiraUser")+":"+Script.dicConfigValues.get("JiraPassword"));
												
												ZephyrUtil.updateTestExecution(execId, execStatus, "");
												
												ZephyrUtil.addAttachment(strReportsPath, execId);
												
												AbstractReporter.strReportPath = "";
												
												logger.info("Jira integration requested in config file and status of execution updated in Jira.");

											}
											else
											{
												logger.info("Jira integration not requested in config file.");
											}
											
											//***************** closing all the "WebDriver" browser windows ********** ///////////////
											if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
											{
												Set<String> windowHandler = new HashSet<String>(5);
												windowHandler = driver.getWindowHandles();
												for(String browserWindowHandle : windowHandler)
													driver.switchTo().window(browserWindowHandle).close();
											}
											else
											{
												launcher.shutDown(selenium);
												selenium.stop();
												if(!(selenium1 == null))
												{
													launcher.shutDown(selenium1);
													selenium1.stop();
												}
											}
									}
									
									if((i>=iterateFrom)&&(i<iterateTo))
									{
										
										if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
										{
											driver = launcher.getWebDriver();	
										}
										else
										{
											try
											{
											//selenium = new DefaultSelenium("localhost", 4444,Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
												selenium = getSelenium();
											selenium.start();
											}
											catch(Exception e)
											{
												System.out.println(e.getMessage());
											}
										}
										
										reporter = launcher.getReporter(testCaseName, selenium, driver); 
									    logger.info("Script end");
									}
								}
								Launcher.chkIteration=false;
								AbstractReporter.filename=null;
							}
							}
						}
					}
				}
				}
			catch (final Throwable t) 
			{
					logger.error("Error occurred while processing.",t);
			} 
			finally 
			{
				if(!Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
				{
					try{
					launcher.shutDown(selenium);		// in case of exception , the last code to be executed is this, which will close the browser window
					}
					catch(Exception e)
					{
						logger.info("No other selenium RC window Opened");
					}
					if(!(selenium1 == null))
					{
						launcher.shutDown(selenium1);
						selenium1.stop();
					}
				}
				else
				{
					// in case of exception , the last code to be executed is this, which will close the webdriver browser window
					try{
					driver.close();
					}
					catch(Exception e)
					{
						logger.info("No other Driver window opened.");
					}
				}
				killProcess("chromedriver.exe");
				logger.info("Script end");
				//@ Summary Report :- added by kamlesh
				if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
				{
				 launcher.EndSummaryTestReport();
				}
				
			}
		}
		// Executing directly from Jira			
		else
		{
			HubIP = null;
			executionMethod = "local";
			
		
		dataSheetFileName =Script.dicConfigValues.get("MasterTestDataPath");
		if(dataSheetFileName.equals(null) || dataSheetFileName.equals(""))
			dataSheetFileName=properties.getProperty("testdata.location")+properties.getProperty("template.testData.fileName")+Script.dicConfigValues.get("strEnvironment") + ".xls";
		final String orFileName = launcher.getORFileName();
		logger.debug("datasheet filename:" + dataSheetFileName);
		final ExcelReader td = new ExcelReaderImpl(dataSheetFileName);
		final ExcelReader or = new ExcelReaderImpl(orFileName);

		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(dataSheetFileName)); // @Added by Ashish
		HSSFWorkbook workbook = new HSSFWorkbook(fileSystem); 
		int strSheetCount = workbook.getNumberOfSheets();
		
		System.out.println("total number of sheets are : "+strSheetCount);
		 //////Put values from configValues of dataSheet in dicCommonValue map ////
	    //getCommonValuesFromDataSheet(dataSheetFileName);
		putKeyValuesFromSheetToMap(dataSheetFileName,"CommonValues", 0, "ITEM", 0, 1, Script.dicCommonValue);
		
		try  	
		{
			int size=ZephyrUtil.getTestMap().size();
			for(int count=4;count<strSheetCount;count++) // Start from count=0
			{
				int n=0;
				td.setDefaultSheetName(count); // @Added by Ashish
				System.out.println("count loop "+ count);		
			 	HSSFSheet sheet = workbook.getSheetAt(count);
			  	sheetn=sheet.getSheetName();					  
			  	System.out.println(sheetn);
			  	testSetName = sheetn;
			  	workbook.setActiveSheet(count);
			  	System.out.println("Active sheet num: "+ workbook.getActiveSheetIndex());

			  	td.setRowIdentifierColName("TestCaseId"); // @Added by Ashish

			  	String testIds[] = td.getAllRowIdentifierValues(); // @Added by Ashish

	
				if(launcher.scriptFile!=null) 
				{
					logger.info("Running test script:"+launcher.scriptFile);
					//***************
					if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
					{
						driver = launcher.getWebDriver();	
					}
					else
					{
						try
						{
						selenium=getSelenium();
						selenium.start();
						}
						catch(Exception e)
						{
							System.out.println(e.getMessage());
						}
					}
					launcher.runScript(new File(launcher.scriptFile), selenium, driver, td,or);
				} 
				else 
				{
					logger.info("Executing tests directly from Jira.");
					for (String testId : testIds)
					{				
						if(testId!="")     // rowIds[] contains extra values which have no data i.e where testCaseName is also "" are added in this array
						{
						n=n+1;
						
						for (int j=0;j<size;j++)
						{
							TestCaseId=ZephyrUtil.getTestMap().get(j);
						if(testId.equals(TestCaseId))
						{
							String runValue = "Y";
						
							File f11 = new File("./java-Testscripts");
							File[] f21 =f11.listFiles();
							for(File strFile1:f21)
							{
								 File[] f3 =strFile1.listFiles();
								 for(File strFile11:f3)
								 {
									 if(strFile11.getName().contains(testId))
									 strFile11.delete();
								 }
							 }
							
								
							// Setting iteration to maintaining the uniformatity in code 
							String iteratefrom ="1";
							String iterateto ="1";
							// Convert into int values  
							int iterateFrom=(int)Float.valueOf(iteratefrom).floatValue();  
							int iterateTo=(int)Float.valueOf(iterateto).floatValue();      
							 
							//append TestCaseId as a prefix of the testCaseName
							TestName=td.getValue(testId, "TestCaseName"); // @Added by Ashish
							String testNameModified1 = TestName.replaceAll(" : ", "");
							String testNameModified2 = testNameModified1.replaceAll(" - ", "");
							String testNameModified = testNameModified2.replaceAll(" ", "");
							testCaseName = testId + "_" + testNameModified;   
							//driver object
							//selenium = new WebDriverBackedSelenium(driver, "");
							if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
							{
								driver = launcher.getWebDriver();	
							}
							else
							{
								try
								{
								//selenium = new DefaultSelenium("localhost", 4444, Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
									selenium = getSelenium();
								selenium.start();
								}
								catch(Exception e)
								{
									System.out.println(e.getMessage());
								}
							}
							
							
							Reporter reporter = launcher.getReporter(testCaseName, selenium, driver);
							
							
							// deleting files from LAST RUN Reports folder
							String reportsLocation = "";  //{}
							if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
								reportsLocation = Script.dicConfigValues.get("reportsPath");
							else
							reportsLocation = properties.getProperty("report.location");
						
							//@ Added code for summary report
							launcher.SummaryPath = reportsLocation;
							
							File f1 = new File(reportsLocation);
							if(!f1.isDirectory()) //{}
							(new File(reportsLocation)).mkdirs();
							if(!f1.isDirectory())
							(new File(reportsLocation)).mkdir();
							(new File(reportsLocation + "\\Last Run")).mkdir();
	
							String testsetfolder = reportsLocation+ "/" + Launcher.testSetName + "/" + testCaseName.split("_")[0];
							File testSetFolder = new File(testsetfolder);
							if(!testSetFolder.isDirectory())
							{
							(new File(testsetfolder)).mkdirs();
							if(!testSetFolder.isDirectory())
							(new File(testsetfolder)).mkdir();
							}
	
							
							//File f1 = new File(properties.getProperty("report.location"));
							 File[] f2 =f1.listFiles();
							 for(File strFile:f2)
							 {
		
								 if(strFile.getName().equals("Last Run"))
								 {
									 File[] f3 =strFile.listFiles();
									 for(File strFile1:f3)
									 {
										 strFile1.delete();
									 }
									 break;
								 }
							 }
							 
							 //loop for multiple iterations
							int howmanytimesiterated=0;
							for(int i=iterateFrom;i<=iterateTo;i++)
							{
								AbstractReporter.testIterationNumber =i;
								Script.dicTestData.clear();      // clears the test specific data from
			                    getTestDataFromDataSheet(sheetn,TestName ,dataSheetFileName , i); 
								if(howmanytimesiterated>=1)
									Launcher.chkIteration=true;
								howmanytimesiterated++;
								logger.info("Running testcase:" + testCaseName);
								reporter.setIsFromQC(launcher.isFromQC);
								String dataSheetName = sheetn;
								
									 
								// if folder name is provided in testsuite then use this value as sheet name
								if (StringUtils.isNotEmpty(dataSheetName)) 
								{
									td.setDefaultSheetName(dataSheetName);
								} 
								else 
								{ // otherwise take first sheet as a default sheet for data
									td.setDefaultSheetName(0);
								}
								ExcelReporter excelreporter=new ExcelReporter(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl, selenium, driver);
								if("XLS".equals(reportType) || ("EXCEL".equals(reportType)))
								{													
									excelreporter.Enterdatainexcel(testCaseName,environment,buildVersion,getBrowser(selenium , driver),applicationUrl);							
								}
								
								 ScriptRunner scriptRunner = new ExternalScriptRunner(
										selenium, driver, or, td, reporter,testCaseName);
			
								 String testScriptFile =  properties.getProperty("script.file.location") + "/" +testSetName + "//" + testCaseName;
								
								 // @ Deleting the pre-compiled class files from bin folder to avoid not necessary debugging while updation of code.
							 		final String packageName = JVMUtil.generatePackageName();
							 		final File root = new File(
											properties.getProperty("script.java.class.location"),
											packageName.replace(".", "/"));
									FileUtils.cleanDirectory(root);
								
								//script starts running
								scriptRunner.runScript(testScriptFile);
								boolean isPassed1=reporter.isTestPassed();
								 
								
								if (reporter != null) 
								{
										reporter.saveReport();									
									//***************************************
									//***************************************
									boolean isPassed=reporter.isTestPassed();

									//@ Added Script.isNotePadFileError flag to make script fail [Compile error or Script not present in dedicated folder]
									////////////////Code added by kamlesh for Summary Report/////////////////
									//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////											
									if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
									{
										if (intcount == 0)
										{
											launcher.PrepSummaryReport(launcher.SummaryPath);
											launcher.NewSummaryHeader();	
											
										}

										
										String strResult = null;
										
										if(isPassed)
										{
											strResult = "PASS";
										}
										else
										{
											strResult = "FAIL";
										}
										

											
										//launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
										launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
										if (iterateTo == 1)
										{
											launcher.HTMLSummaryReport(testCaseName, strResult, Script.dicConfigValues.get("totalduration"), "file:///"+reporter.HTMLSummaryReportPath());
										}
										else
										{
											//@ Code for Iteration
											//launcher.ConvertTimeIntoInt(Script.dicConfigValues.get("totalduration"));
										   String[] iteDate=Script.dicConfigValues.get("totalduration").split(":");  
											  
											int itehour=Integer.parseInt(iteDate[0]);  
											int iteminute=Integer.parseInt(iteDate[1]);  
											int itesecond=Integer.parseInt(iteDate[2]);  
											int iteTemp = itesecond + (60 * iteminute) + (3600 * itehour);
											iteExceutionTime = iteExceutionTime + iteTemp;
											
											if (strResult == "FAIL")
											{
												strStatusHTMLResult = strResult;
											}
											
											if (howmanytimesiterated == iterateTo)
											{
												if(strStatusHTMLResult == null)
													strStatusHTMLResult = strResult;
												launcher.HTMLSummaryReport(testCaseName, strStatusHTMLResult, new SimpleDateFormat("HH:mm:ss"){{setTimeZone(TimeZone.getTimeZone("UTC"));}}.format(new Date(iteExceutionTime * 1000)).toString(), "file:///"+reporter.HTMLSummaryReportPath());
											}
										}
										
									}
                               ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
									
									
									//@ Added Script.isNotePadFileError flag to make script fail [Compile error or Script not present in dedicated folder]
									if(Script.isNotePadFileError == true)
										isPassed = false;
									
									String linkValueActual = "";
									String execStatus = "";
									if(isPassed)
									{								
										execStatus = "PASS";

										AbstractReporter.testStepFailCount=0;
										
									}
									else
									{
										execStatus = "FAIL";
										
										ExcelReporter.a.clear();
										AbstractReporter.testStepFailCount=0;
										
										//@ added code by Aarti
										if(jiraLogDefect.equalsIgnoreCase("Yes"))
										{
											// Get the Execution ID/ Test Summary of the failed test script																			
											execId = ZephyrUtil.getExecutionMap().get(jiraCycleName+"~"+TestCaseId);
											String strTestSummary = ZephyrUtil.getTestSummary(execId);
											// Create a bug in JIRA with the for the failed test script
											String strBugKey = ZephyrUtil.addIssue(jiraProjectName,"The functionality ("+strTestSummary+") failed.","The functionality ("+strTestSummary+") failed during automation script replay. Please refer to attachments.",jiraDefectAssignee);													
											strReportsPath=AbstractReporter.strReportPath;
											// Add reports and screenshots to the raised bug in JIRA
											ZephyrUtil.addAttachmenttoBug(strReportsPath,strBugKey);
											// Link the bug raised to the failed script in JIRA
											ZephyrUtil.linkDefect(execId,strBugKey);
											logger.info("Issue '"+strBugKey+"' has been raised for the failed test script '"+TestCaseId+"' in Cycle '"+jiraCycleName+"' and Project '"+jiraProjectName+"'. The issue has been linked to the failed test script in JIRA.");
										}
										else
										{
											logger.info("Jira Integration for raising Bugs is not requested.");
										}
										
									}
									
									//@ added code by Kamlesh
									linkValue = "";
									
									//Call ZephyrUtill to updateExecutionStatus and addAtachment method 

										strReportsPath=AbstractReporter.strReportPath;
										// Get the Execution ID based on the Cycle Name and Test Case Id and Fix Version																			
										execId = ZephyrUtil.getExecutionMap().get(jiraCycleName+"~"+testId);
										
										//ZephyrUtil.setCredentials(Script.dicConfigValues.get("JiraUser")+":"+Script.dicConfigValues.get("JiraPassword"));
										
										ZephyrUtil.updateTestExecution(execId, execStatus, "");
										
										ZephyrUtil.addAttachment(strReportsPath, execId);
										
										AbstractReporter.strReportPath = "";
										
										logger.info("Scripts are executing directly from Jira hence status of execution updated in Jira.");

									
									//***************** closing all the "WebDriver" browser windows ********** ///////////////
									if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
									{
										Set<String> windowHandler = new HashSet<String>(5);
										windowHandler = driver.getWindowHandles();
										for(String browserWindowHandle : windowHandler)
											driver.switchTo().window(browserWindowHandle).close();
									}
									else
									{
										launcher.shutDown(selenium);
										selenium.stop();
										if(!(selenium1 == null))
										{
											launcher.shutDown(selenium1);
											selenium1.stop();
										}
									}
							}
							
							if((i>=iterateFrom)&&(i<iterateTo))
							{
								
								if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
								{
									driver = launcher.getWebDriver();	
								}
								else
								{
									try
									{
									//selenium = new DefaultSelenium("localhost", 4444,Script.dicConfigValues.get("strBrowserType").toLowerCase(), Script.dicConfigValues.get("strApplicationURL"));
										selenium = getSelenium();
									selenium.start();
									}
									catch(Exception e)
									{
										System.out.println(e.getMessage());
									}
								}
								
								reporter = launcher.getReporter(testCaseName, selenium, driver); 
							    logger.info("Script end");
							}
						}
						Launcher.chkIteration=false;
						AbstractReporter.filename=null;
					}
					}
				}
			}
		}

		}
		}
	catch (final Throwable t) 
	{
			logger.error("Error occurred while processing.",t);
	} 
	finally 
	{
		if(!Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
		{
			try{
			launcher.shutDown(selenium);		// in case of exception , the last code to be executed is this, which will close the browser window
			}
			catch(Exception e)
			{
				logger.info("No other selenium RC window Opened");
			}
			if(!(selenium1 == null))
			{
				launcher.shutDown(selenium1);
				selenium1.stop();
			}
		}
		else
		{
			// in case of exception , the last code to be executed is this, which will close the webdriver browser window
			try{
			driver.close();
			}
			catch(Exception e)
			{
				logger.info("No other Driver window opened.");
			}
		}
		killProcess("chromedriver.exe");
		logger.info("Script end");
		//@ Summary Report :- added by kamlesh
		if(Script.dicConfigValues.get("SummaryReport").toLowerCase().contains("yes"))
		{
		 launcher.EndSummaryTestReport();
		}		
		
	}
		}
		}

	}
	
	
	public static void killProcess(String serviceName) throws Exception {
	       Process p = Runtime.getRuntime().exec("tasklist");
	       BufferedReader reader = new BufferedReader(new InputStreamReader(
	          p.getInputStream()));
	       String line;
	       while ((line = reader.readLine()) != null) {

	         
	         if (line.contains(serviceName)) {
	                Runtime.getRuntime().exec("taskkill /F /IM " + serviceName);
	         }
	       }
	       }

	private static int findRowNumber(HSSFSheet sheet, String cellContent) {
	    for (Row row : sheet) {
	        for (Cell cell : row) {
	            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
	                    return row.getRowNum();  
	                }
	            }
	        }
	    }               
	    return 0;
	}
	
	//----------------------------------------------------------------------------------------//
    // Function Name : getConfigValuesFromDataSheet
    // Function Description : Get all values from ConfigValues sheet of dataSheet file
    // Input Variable : dataSheetFileName
    // OutPut : void
    // example : getConfigValuesFromDataSheet("mastertestdata")
    //---------------------------------------------------------------------------------------//
	// ///////////////////////////// Get all values from ConfigValues sheet of dataSheet file /////////////////////////////////////////// // ?? chk
    public static void getCommonValuesFromDataSheet(String dataSheetFileName)
    {
        ResultSet rsdataSheet = null;
        Connection dataSheetcon = null;
        try 
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            //String dataSheetDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ="+ dataSheetFileName + ";" + "DriverID=22;READONLY=false";
            String dataSheetDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=sd;.\\Datasheets\\Config.xls;" + "DriverID=22;READONLY=false";
            dataSheetcon = DriverManager.getConnection(dataSheetDB, "", "");
            //String query = "SELECT * FROM [CommonValues$]";
            String query = "SELECT * FROM [Configs$]";
            PreparedStatement objdataSheet = null;
            objdataSheet = dataSheetcon.prepareStatement(query);
            rsdataSheet = objdataSheet.executeQuery(); // This result set contains all rows in sheetn (of testSuite file) with Run value = Y
            while (rsdataSheet.next()) 
            {
                String key = rsdataSheet.getString("TestParameters");
                //String value = rsdataSheet.getString("VALUE");
                //byte[] b = null;
                rsdataSheet.getAsciiStream("AbsolutePath");
                Reader r = new InputStreamReader(rsdataSheet.getBinaryStream("AbsolutePath"));
                int intch;
                while ((intch = r.read()) != -1)
                {
                	int ch = (char) intch;
                }
                //Script.dicCommonValue.put(key, value);
            }
            rsdataSheet.close();
        }
        catch(Exception ee)
        {
            System.out.println("");
        }
    }

    /////////////////////////////////////////////////
    //  This function puts all the test case specific values from the specified 
    //  sheet of the dataSheet in dicTestData. It runs for current test case whose run value is 'Y' in test suite.
/////////////////////////////////////////////////
    public static void getTestDataFromDataSheet1(String sheetName, String testCaseName, String dataSheetFileName, int i)
    {
        ResultSet rsdataSheet = null;
        ResultSetMetaData rsmddataSheet = null;
        Connection dataSheetcon = null;
        int colCount = 0 ;
        int rowCount = 0;
        try 
        {
  		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String dataSheetDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ="+ dataSheetFileName + ";" + "DriverID=22;READONLY=false";
        dataSheetcon = DriverManager.getConnection(dataSheetDB, "", "");
        String query = "SELECT * FROM [" + sheetName +"$] WHERE TESTCASENAME = '" + testCaseName + "'";
        PreparedStatement objdataSheet = null;
        objdataSheet = dataSheetcon.prepareStatement(query);
        rsdataSheet = objdataSheet.executeQuery(); // This result set contains the row of the current testcase in  
        rsmddataSheet = objdataSheet.getMetaData();
        rsdataSheet.getRow();
        while(rsdataSheet.next())
        {
        	rowCount ++;
	    	if(rowCount == i)
	    	{
	            colCount = rsmddataSheet.getColumnCount();
	            for(int k=2; k<=colCount; k++)
	            {                                   

	                String val ;
	                if((val = rsdataSheet.getString(rsmddataSheet.getColumnLabel(k).toString().trim())) != null)
	                	Script.dicTestData.put(rsmddataSheet.getColumnLabel(k), val);
	            }
	    	}

        }
        rsdataSheet.close();
        }
        catch(Exception ee)
        {
            try
            {
            rsdataSheet.close();
            }
            catch(Exception e){}
            System.out.println("");
        }
    }
    // //////////////////////////////////////////////////////////////////////////////
    
    public static void getTestDataFromDataSheet(String sheetName, String testCaseName, String dataSheetFilePath, int i)
    {
    	// put values in map in either case (AccessMethod = excel or DBQuery)
    	
    	POIFSFileSystem fileSystem;
    	List<String> lstHeaderCells = new ArrayList<String>();
    	try
		{
    		int requiredRowsCount = 0;
    		int rowIndex = 0;
    		String key = "";
    		String value = "";
    		ExcelReader testSuite = new ExcelReaderImpl(dataSheetFilePath);
    		testSuite.setRowIdentifierColName("TestCaseName", sheetName);
    		String rowIds[] = testSuite.getAllRowIdentifierValues(sheetName);
    		
    		fileSystem = new POIFSFileSystem(new FileInputStream(dataSheetFilePath));
			HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workbook.getSheet(sheetName); 
			
			////////////// for getting column labels
			HSSFRow headerRow =sheet.getRow((short)0);
			for(Cell headerCell : headerRow)
			{
				try
				{
					lstHeaderCells.add(headerCell.getStringCellValue());  // this list contains all the column labels of the sheet passed
				}
				catch(Exception e)
				{
					lstHeaderCells.add((int)headerCell.getNumericCellValue()+"");
				}
			}
    		
		  	for (String rowId : rowIds)
		  	{
		  		rowIndex++;  // tells which row of excel is being traversed. 
		  		if(rowId.equals(testCaseName))
		  		{
		  			requiredRowsCount++;  // variable i tells which iteration in datasheet to run.
		  			if(requiredRowsCount == i) 
		  			{
						HSSFRow row =sheet.getRow((short)rowIndex);	
						for (Cell cell : row)
						{
							int colIndex = cell.getColumnIndex();
							
							if(colIndex > 1)
							{
								key = lstHeaderCells.get(colIndex);
								cell.setCellType(cell.CELL_TYPE_STRING);
								value = cell.getStringCellValue();		
								// If the test data contains the variables that need to be passed on from other columns of the data sheet, it is marked within << >>								
								if(value.contains("<<"))
								{									
									String [] strtemparray = value.split("<<");
									int inttemp = strtemparray.length;
									for (int counter = 0;counter < inttemp; counter++)
									{
										if(strtemparray[counter].contains(">>"))
										{
											String [] strtemparray2 = strtemparray[counter].split(">>");
											// Check if the value needs to be retrieved from dicCommon or dicTestData
											if (strtemparray2[0].contains("CommonValues."))
											{
												strtemparray2[0] = strtemparray2[0].replace("CommonValues.", "");											
												strtemparray2[0] =Script.dicCommonValue.get(strtemparray2[0]);
											}
											else
												strtemparray2[0] =Script.dicTestData.get(strtemparray2[0]);											
											
											strtemparray[counter] = StringUtils.join(strtemparray2);
										}										
									}				
																	
									value = StringUtils.join(strtemparray);
								}
								Script.dicTestData.put(key, value);
								// We are now fetching the execution ID at run-time based on Project/Cycle/Version
								//execId=Script.dicTestData.get("ExecutionId");
							}
						}
						
						//Hard code the Access Method to 'DB Query'
						Script.dicConfigValues.put("AccessMethod","db query");
						
						// else override values in map with the values got from execution of the query written the Queryref cell
						if(Script.dicConfigValues.get("AccessMethod").toLowerCase().equals("db query")) 
				    	{
							String query ="";
				    		String queryRef = Script.dicTestData.get("QueryRef");
				    		if(queryRef.equals("") || queryRef.equals(null))
				    			logger.info("QueryRef column is empty");
							// Execute the next steps only if the query reference is present
							else
				    		{
				    		//testSuite.setRowIdentifierColName("QueryRef", "DBQueries");
				    		//String rowIds[] = testSuite.getAllRowIdentifierValues(sheetName);
				    		try
				    		{
				    			int n =0;
				    			testSuite.setRowIdentifierColName("TestCaseName", "DBQueries");
				        		String queryrowIds[] = testSuite.getAllRowIdentifierValues("DBQueries");
				        		for (String queryrowId : queryrowIds)
				        		{
				        			n++;
				        			if(queryrowId.equals(queryRef))
				        			{
					    			HSSFSheet querySheet = workbook.getSheet("DBQueries");
					    			HSSFRow queryRow = querySheet.getRow((short)n);
					    			HSSFCell queryCell = queryRow.getCell(1);
					    			try
					    			{
					    				query = queryCell.getStringCellValue(); // **************** //{{					    				
										// If the query contains the variables that need to be passed on from Common sheet, it is marked within << >>								
										if(query.contains("<<"))
										{									
											String [] strtemparray = query.split("<<");
											int inttemp = strtemparray.length;
											for (int counter = 0;counter < inttemp; counter++)
											{
												if(strtemparray[counter].contains(">>"))
												{
													String [] strtemparray2 = strtemparray[counter].split(">>");											
													strtemparray2[0] ="'" + Script.dicCommonValue.get(strtemparray2[0])+ "'";								
													strtemparray[counter] = StringUtils.join(strtemparray2);
												}										
											}																			
											query = StringUtils.join(strtemparray);
										}
					    				break;
					    			}
					    			catch(Exception e)
					    			{
					    				logger.error("Query is not in proper format.");
					    			}
				        			}
				        		}
				    		}
				    		catch(Exception e)
				    		{
				    			logger.error(e.getMessage());
				    		}
				    		//now execute the query and put values in map, override in case keyvalue pair is already present in dicTestData map
//							String connectionString = Script.dicCommonValue.get("strDBConnString");
//							String dbUser = Script.dicCommonValue.get("strDBUser");
//							String dbPassword = Script.dicCommonValue.get("strDBPassword");
							
							String connectionString = Script.dicConfigValues.get("strDBConnString");
							String dbUser = Script.dicConfigValues.get("strDBUser");
							String dbPassword = Script.dicConfigValues.get("strDBPassword");
							
							
							try
							{
								ResultSet rs = null;
								ResultSetMetaData rsmd = null;
								// String query1 = "Select * FROM User_ProviderAccount where UserName='mandeep'";
								Connection conn = null;

								  Class.forName("com.mysql.jdbc.Driver");
								  //conn = DriverManager.getConnection( "jdbc:mysql://Z5-QA3-DB01.KABBAGE.COM/KOLTPWeb3","QAUser", "Kadmin123!" ) ;
								  conn = DriverManager.getConnection(connectionString, dbUser, dbPassword);
								  System.out.println("Connected to the database");
								  PreparedStatement obj = null;
								  obj = conn.prepareStatement(query);
								  rs = obj.executeQuery();
								  rsmd = rs.getMetaData();
								  int colCount = rsmd.getColumnCount();
								  while (rs.next()) 
						            {
									  for(int k=1; k<=colCount; k++)
							            {                                   

							                String val ;
							                if((val = rs.getString(rsmd.getColumnLabel(k).toString().trim())) != null)
							                {
							                	if(!Script.dicTestData.containsKey(rsmd.getColumnLabel(k)))
							                		Script.dicTestData.put(rsmd.getColumnLabel(k), val);
							                	else
							                	{
							                		Script.dicTestData.remove(rsmd.getColumnLabel(k));
							                		Script.dicTestData.put(rsmd.getColumnLabel(k), val);
							                	}
							                }
							            }

									  // String val=rs.getString("UserName");

						            }
								  conn.close();
							  System.out.println("Disconnected from database");
								  } 
							catch (Exception e)
							{
								e.printStackTrace();
							}
				    	}
						}
		  			}
		  		}
		  	}
		}
    	catch (Exception e)
    	{
    		logger.error(e.getMessage().toString());
    	}
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    public static void putKeyValuesFromSheetToMap(String WorkbookPath , String sheetname, int sheetIndex, String keysColName, int keysColIndex, int valuesColIndex, Map<String, String> map)
    {
    	POIFSFileSystem fileSystem;
		try
		{
			ExcelReader testSuite = new ExcelReaderImpl(WorkbookPath);
			int n=0;
			
			testSuite.setRowIdentifierColName(keysColName,sheetname);
		  	String rowIds[] = testSuite.getAllRowIdentifierValues(sheetname);
		  	for (String rowId : rowIds)
			{				
				if(rowId!="")     // rowIds[] contains extra values which have no data i.e where testCaseName is also "" are added in this array
				{
					n=n+1;      // n gives the current row number
					fileSystem = new POIFSFileSystem(new FileInputStream(WorkbookPath));
					HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
					HSSFSheet sheet = workbook.getSheetAt(sheetIndex);     // "1" Because config sheet is second sheet
					HSSFRow row =sheet.getRow((short)n);							
					HSSFCell cell = row.getCell(keysColIndex);
					HSSFCell cell2 = row.getCell(valuesColIndex);
					String key=cell.getStringCellValue();
					String value = "";
					try
					{
						try
						{
							value=cell2.getStringCellValue();
							System.out.println(value);
						}
						catch(Exception e)
						{
							value = (int)cell2.getNumericCellValue()+"";
						}
					   
					}
					catch(Exception ee)
					{
						System.out.println("No value found for " + key);
					}
 					map.put(key, value);
				}
			}
				
		} 
		catch (Exception e) {
			
			System.out.println("");
		}
		} 
    //////////////////////////////////////////////////////////////////////////////
	private String getFileName(final File file) {
		final String fileName = file.getName();
		return fileName;
	}

	private void runScript(final File scriptFile, final Selenium selenium, final WebDriver driver, final ExcelReader td, final ExcelReader or) {
		logger.info("Running testcase:" + testName);
		try {
			String reportsLocation = "";  //{}
			if(!(Script.dicConfigValues.get("reportsPath").equals("") || Script.dicConfigValues.get("reportsPath").equals(null)))
				reportsLocation = Script.dicConfigValues.get("reportsPath");
			else
				reportsLocation = properties.getProperty("report.location");
			 Reporter reporter = getReporter(testName, selenium, driver);

			td.setDefaultSheetName(0);

		 ScriptRunner scriptRunner = new ExternalScriptRunner(
					selenium, driver, or, td, reporter,testName);

			scriptRunner.runScript(scriptFile);

			if (reporter != null) {
				reporter.saveReport();
				if(isFromQC) {
					 QCManager qcManager = getQcManager();
					if(qcManager!=null) {
						System.out.println("testsetname is : "+testSetName);
						 //File reportLocation = new File(properties.getProperty("report.location".trim() + "/" +testSetName + "//" ));						
						 File reportLocation = new File(reportsLocation + "/" +testSetName + "//" );
						logger.info("attaching files from :"+reportLocation.getAbsolutePath());
						qcManager.attachFolder(testName, testSetName, reportLocation);
						if(!reporter.isTestPassed()) {
							qcManager.updateTestStatus(testRunName, testName, testSetName, "Failed");
						}
					} else {
						logger.error("Could not connect to QC");
					}
				}
			}
		} catch (final Exception e) {
			logger.error("Error while executing script:"+scriptFile.getAbsolutePath(),e);

		}
	}

	/**
	 * This method Shut down the selenium connection.
	 * @param selenium
	 * 				: Indicates the selenium object
	 *
	 */
	private void shutDown(Selenium selenium) {
		try{
		if (selenium != null) {
			selenium.close();
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * This method returns the appropriated browser name
	 * @param selenium
	 * 				: Indicates the selenium object
	 * @return
	 * 				: [String]  the appropriated browser name
	 */
	private static String getBrowser(Selenium selenium, WebDriver driver) {
		String browserName="";
		String fullVersion = "";
		if(Script.dicConfigValues.get("SeleniumVariant").toLowerCase().contains("webdriver"))
		{
			JavascriptExecutor js = (JavascriptExecutor) Launcher.driver;
			final String nAgt = (String) js.executeScript("return navigator.userAgent;");
			
			
			//final String nAgt = driver.getEval("navigator.userAgent;");
			browserName = (String) js.executeScript("return navigator.appName;");

			//browserName = selenium.getEval("navigator.appName;");
			fullVersion = (String) js.executeScript("return navigator.appVersion;");
						
			//fullVersion = selenium.getEval("navigator.appVersion;");
			int nameOffset,verOffset,ix;

			// In Opera, the true version is after "Opera" or after "Version"
			if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
				browserName = "Opera";
				fullVersion = nAgt.substring(verOffset+6);
				if ((verOffset=nAgt.indexOf("Version"))!=-1) {
					fullVersion = nAgt.substring(verOffset+8);
				}
			}
			// In MSIE, the true version is after "MSIE" in userAgent
			else if ((verOffset=nAgt.indexOf("Trident"))!=-1) {
				browserName = "Microsoft Internet Explorer";
//				fullVersion = nAgt.substring(verOffset+5);
				fullVersion = nAgt.substring(verOffset+124,verOffset+128);
				
			}
			// In Chrome, the true version is after "Chrome"
			else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
				browserName = "Chrome";
				fullVersion = nAgt.substring(verOffset+7);
			}
			// In Safari, the true version is after "Safari" or after "Version"
			else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
				browserName = "Safari";
				fullVersion = nAgt.substring(verOffset+7);
				if ((verOffset=nAgt.indexOf("Version"))!=-1) {
					fullVersion = nAgt.substring(verOffset+8);
				}
			}
			// In Firefox, the true version is after "Firefox"
			else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
				browserName = "Firefox";
				fullVersion = nAgt.substring(verOffset+8);
			}
			// In most other browsers, "name/version" is at the end of userAgent
			else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) <
					(verOffset=nAgt.lastIndexOf('/')) )
			{
				browserName = nAgt.substring(nameOffset,verOffset);
				fullVersion = nAgt.substring(verOffset+1);
				if (browserName.toLowerCase().equals(browserName.toUpperCase())) {
					browserName = (String) js.executeScript("return navigator.appName;");
				}
			}
			
			
			// trim the fullVersion string at semicolon/space if present
			if ((ix=fullVersion.indexOf(";"))!=-1) {
				fullVersion=fullVersion.substring(0,ix);
			}
			if ((ix=fullVersion.indexOf(" "))!=-1) {
				fullVersion=fullVersion.substring(0,ix);
			}

		}
		else
		{
		final String nAgt = selenium.getEval("navigator.userAgent;");
		browserName = selenium.getEval("navigator.appName;");
		fullVersion = selenium.getEval("navigator.appVersion;");
		int nameOffset,verOffset,ix;

		// In Opera, the true version is after "Opera" or after "Version"
		if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
			browserName = "Opera";
			fullVersion = nAgt.substring(verOffset+6);
			if ((verOffset=nAgt.indexOf("Version"))!=-1) {
				fullVersion = nAgt.substring(verOffset+8);
			}
		}
		// In MSIE, the true version is after "MSIE" in userAgent
		else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
			browserName = "Microsoft Internet Explorer";
			fullVersion = nAgt.substring(verOffset+5);
		}
		// In Chrome, the true version is after "Chrome"
		else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
			browserName = "Chrome";
			fullVersion = nAgt.substring(verOffset+7);
		}
		// In Safari, the true version is after "Safari" or after "Version"
		else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
			browserName = "Safari";
			fullVersion = nAgt.substring(verOffset+7);
			if ((verOffset=nAgt.indexOf("Version"))!=-1) {
				fullVersion = nAgt.substring(verOffset+8);
			}
		}
		// In Firefox, the true version is after "Firefox"
		else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
			browserName = "Firefox";
			fullVersion = nAgt.substring(verOffset+8);
		}
		// In most other browsers, "name/version" is at the end of userAgent
		else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) <
				(verOffset=nAgt.lastIndexOf('/')) )
		{
			browserName = nAgt.substring(nameOffset,verOffset);
			fullVersion = nAgt.substring(verOffset+1);
			if (browserName.toLowerCase().equals(browserName.toUpperCase())) {
				browserName = selenium.getEval("navigator.appName;");
			}
		}
		// trim the fullVersion string at semicolon/space if present
		if ((ix=fullVersion.indexOf(";"))!=-1) {
			fullVersion=fullVersion.substring(0,ix);
		}
		if ((ix=fullVersion.indexOf(" "))!=-1) {
			fullVersion=fullVersion.substring(0,ix);
		}
		}
		
		strfullVersion = fullVersion;
		return browserName + "<br>" + fullVersion;
	}
	/**
	 * This method returns the test data file name
	 * @return
	 * 				: [String]  the test data file name
	 */
	
	String getTestDataFileName() {

		String fileName = getArgumentValue( ArgumentKey.tcdfName, Constants.APP_TEST_CASE_DATA_FILE_NAME);
		if (fileName == null) {
			fileName = generateTestDataFileName();
		}
		logger.info("The TestCase Data File Name from where the data will be taken to execute testcase = "+ fileName);
		//fileName = properties.getProperty("testdata.location") + fileName;
		return fileName;
	}
	/**
	 * This method returns the Object Repository name
	 * @return
	 * 				: [String] the Object Repository name
	 */
	private String getORFileName() {
		String fileName = properties.getProperty("objectrepository.location")
				+ Script.dicConfigValues.get("ObjectRepository");
		return fileName;
	}
	/**
	 * This method returns the test case name from command line arguments
	 * @return
	 * 				: [String] the test case name from command line arguments
	 */
	private String getTestCaseNameFromArgs() {
		String testCaseName = getArgumentValue( ArgumentKey.tcName );
		return testCaseName;
	}

	/**
	 * This method returns the test suite name
	 * @return
	 * 				: [String] the test suite name
	 */
	private String getTestSuiteFileName() {
		String fileName = getArgumentValue(ArgumentKey.tsfName, Constants.APP_TEST_SUIT_FILE_NAME);
		if (StringUtils.isBlank(fileName)){
			fileName = Script.dicConfigValues.get("TestSuiteFileName") + ".xls";
		}
		fileName = properties.getProperty("testsuite.location") + fileName;
		logger.info("The Opted TescSuite file name is = "+ fileName);
		return fileName;
	}

	/**
	 * This method returns the auto-generated  Reporter object
	 * @param testCaseName
	 * 					: Indicates the test case name
	 * @param selenium
	 * 					: The selenium object
	 * @return
	 * 					: [Reporter] the auto-generated  Reporter object
	 */
	private Reporter getReporter(final String testCaseName,
			Selenium selenium, WebDriver driver) {
		Reporter reporter = null;
		 String reportType = this.reportType == null ? "HTML" : this.reportType.toUpperCase();
		if("HTML".equals(reportType) || "HTM".equals(reportType)) {
			reporter = new HtmlReporter(testCaseName,environment, buildVersion,getBrowser(selenium , driver), applicationUrl, selenium, driver);
		}
		if(("XLS".equals(reportType)) || ("EXCEL".equals(reportType)))
		{
			reporter=new ExcelReporter(testCaseName,environment,buildVersion,getBrowser(selenium, driver),applicationUrl, selenium, driver);
		}
			
		return reporter;
	}

	/**
	 * This method returns the Web Driver object.
	 * @Parameter : String executionMethod : Valid arguments -> Hub or Local
	 * @return
	 * 			: [WebDriver] The Web Driver object.
	 * @throws MalformedURLException 
	 * @enum BrowserList : contains all possible browser names for use in switch(String s)-case
	 */
	public static enum BrowserList {
		FIREFOX, FF, INTERNET_EXPLORER, IE, CHROME, SAFARI 
	}
	private WebDriver getWebDriver() throws MalformedURLException {
		WebDriver driver = null;
		browser = browser.toUpperCase();
		switch(BrowserList.valueOf(browser))
		{
		case FIREFOX:
		case FF:
			if(executionMethod.toLowerCase().equals("hub"))
			{
				if(HubIP != null)
				{
					DesiredCapabilities dc = new DesiredCapabilities();
					dc.setBrowserName("firefox");
					driver = new RemoteWebDriver(new URL("http://" + HubIP + "/wd/hub"), dc);
					try
					{
						driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
					}
					catch(Exception e)
					{
						logger.error("error in setting firefox timeout in case of distributed execution.");
					}
				}
				else if(executionMethod.toLowerCase().equals("local"))
				{
					logger.error("Enter Hub IP for distributed execution");
				}
			}
			else
			{
				driver = new FirefoxDriver();
				try{
					driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			        //driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
					}
					catch(Exception e)
					{
						logger.error("error in setting firefox timeout in case of local execution.");
					}
			}
			break;
			
		case INTERNET_EXPLORER:
		case IE:
			if(executionMethod.toLowerCase().equals("hub"))
			{
				if(HubIP != null)
				{
					File file = new File("./IEDriverServer.exe"); 
					System.setProperty("webdriver.ie.driver", file.getAbsolutePath());	
					DesiredCapabilities dc = DesiredCapabilities.internetExplorer(); 
					dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 
					driver = new RemoteWebDriver(new URL("http://" + HubIP + "/wd/hub"), dc);
				}
				else
				{
					logger.error("Enter Hub IP for distributed execution");
				}
			}
			else if(executionMethod.toLowerCase().equals("local"))
			{
				/*File file = new File("./IEDriverServer.exe"); 
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());*/
				System.setProperty("webdriver.ie.driver", "./IEDriverServer.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				//Delete Browser Cache since IE does not open a clean profile unlike Chrome & FireFox
				try 
				{
					Runtime.getRuntime().exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				driver = new InternetExplorerDriver(capabilities);
				driver.manage().deleteAllCookies();
			}
			break;
			
		case CHROME:
			if(executionMethod.toLowerCase().equals("hub"))
			{
				if(HubIP != null)
				{
					DesiredCapabilities dc = new DesiredCapabilities();
					dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
					driver = new RemoteWebDriver(new URL("http://" + HubIP + "/wd/hub"), dc);
				}
				else
				{
					logger.error("Enter Hub IP for distributed execution");
				}
			}
			else if(executionMethod.toLowerCase().equals("local"))
			{
				System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
//				options.addArguments("test-type");
				options.setExperimentalOptions("excludeSwitches", Arrays.asList("test-type","ignore-certificate-errors"));
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver(capabilities);
			}
			break;
			
		case SAFARI:
			if(executionMethod.toLowerCase().equals("hub"))
			{
				if(HubIP != null)
				{
				//DesiredCapabilities dc = new DesiredCapabilities();
				DesiredCapabilities dc = DesiredCapabilities.safari();
//				dc.setVersion("5.1.7");
//				dc.setPlatform(Platform.WINDOWS);
				//dc.setBrowserName(DesiredCapabilities.safari().getBrowserName());				
				driver = new RemoteWebDriver(new URL("http://" + HubIP + "/wd/hub"), dc);
				//driver = new SafariDriver();
				}
				else
				{
					logger.error("Enter Hub IP for distributed execution");
				}
			}
			else if(executionMethod.toLowerCase().equals("local"))
			{
				startSeleniumServer(Script.dicConfigValues.get("ServerName"));
				//RunFileFromcmd(Script.dicConfigValues.get("ServerPath") + Script.dicConfigValues.get("ServerName") + " -trustAllSSLCertificates -browserSessionReuse");
                DesiredCapabilities dc = DesiredCapabilities.safari();
                driver = new RemoteWebDriver(dc);

			}
			break;
		default :
			logger.error("Unsupported browser:"+browser);
			throw new RuntimeException("Unsupported browser:"+browser);
		}

		if (driver != null) {
			//driver.get(applicationUrl);
			if(executionMethod.toLowerCase().equals("hub"))
			{
				augmentedDriver = new Augmenter().augment(driver);
				Browser.SelectOriginalWindow();
			}
			driver.get("about:blank");
			
		}
		return driver;
	}
	
	
	//private void RunFileFromcmd(String FilePath)
	
//    {
//		try{
//		PrintWriter writer = new PrintWriter(".//test.bat", "UTF-8");
//		//writer.println("cd \"" + Script.dicConfigValues.get("ServerPath") + "\"");
//		writer.println("cd \"D:\\JavaSeleniumR&D\\JavaSeAP(distributedExecutionWorkingWithoutIE)\\jar\"");
//		writer.println("java -jar " + Script.dicConfigValues.get("ServerName"));
//		writer.close();
//		}
//		catch(Exception ee)
//		{
//			System.out.println(ee.getMessage());
//		}
//		
//		// running the batch file
//	    try
//	    {
//		Runtime runtime = Runtime.getRuntime();
//		// runtime.exec("cmd /c .\\BrowserSettings\\BrowserSettings.bat");
//	    Process p1 = runtime.exec("cmd /c D:\\JavaSeleniumR&D\\JavaSeAP(distributedExecutionWorkingWithoutIE)\\test.bat");
//	    Thread.sleep(3000);
//	    }
//	    catch(Exception e)
//	    {
//	    System.out.println(e.getMessage() );
//	    }
//		
//		
//		//////////
////        StreamWriter sw = new StreamWriter("test.bat");
////
////        sw.WriteLine("cd \"" + GenericClass.dicConfig["ServerPath"] + "\"");
////        sw.WriteLine("java -jar " + GenericClass.dicConfig["ServerName"]);
////        sw.Close();
////
////        ProcessStartInfo procInfo = new ProcessStartInfo("test.bat");
////        procInfo.UseShellExecute = false;
////        procInfo.CreateNoWindow = true;
////        procInfo.Verb = "runas";
////        Process process = new Process();
////        process.StartInfo = procInfo;
////        process.Start();
//    }

	
	

	/**
	 * This method returns the argument value of the particular option
	 * @param key
	 * 			: Indicates the option Key
	 * @param defaultValueKey
	 * 			: It will be gathered from property file, and used if it doesn't found in the command line arguments
	 * @return
	 * 			:[String]  the argument value of the particular option
	 */
	private String getArgumentValue( ArgumentKey key) {
		return getArgumentValue(key, null, null);
	}

	private String getArgumentValue( ArgumentKey key,  String defaultValueKey) {
		return getArgumentValue(key, defaultValueKey,null);
	}
	public  String getArgumentValue( ArgumentKey key,  String defaultValueKey,  PropertyUtil properties) {
		String argvValue= null;
		 Options options = getOptions();
		 CommandLineParser parser = new PosixParser();
		if(cmd == null){
			try {
				cmd = parser.parse(options, args);
			} catch ( ParseException e) {
				logger.error("Error in parsing arguments" , e);
				throw new RuntimeException("Error in parsing arguments , The Exception is "+e);
			}
		}
		if(properties!=null) {
			argvValue = cmd.getOptionValue(key.name(), properties.getProperty(defaultValueKey));
		} else  {
			argvValue = cmd.getOptionValue(key.name());
		}
		return argvValue;
	}

	private void displayCommandLineArgs( String ...args) {
		if(args!=null) {
			for( String arg : args) {
				logger.debug("Argument:"+arg);
			}
		}
	}

	/**
	 * This method is for initialize the data members of Launcher's object
	 * @param args
	 * 				: The command line values
	 */
	public void init( String args[]) {
		try{
		displayCommandLineArgs(args);
		this.args = args;

		environment = Script.dicConfigValues.get("strEnvironment");
		//hostName = getArgumentValue(ArgumentKey.hostName,Constants.APP_DEFAULT_HOST_NAME, appConfig) ;
		applicationUrl = Script.dicConfigValues.get("strApplicationURL");
		buildVersion = Script.dicConfigValues.get("strBuildVersion");
		browser = Script.dicConfigValues.get("strBrowserType");
		reportType =Script.dicConfigValues.get("ReportingFormat");
		appName = Script.dicConfigValues.get("ProjectName");


		isFromQC = Boolean.valueOf(getArgumentValue(ArgumentKey.isFromQC));
		testName = StringUtils.replace(getArgumentValue( ArgumentKey.tcName ),"***"," ");
		testSetName = StringUtils.replace(getArgumentValue( ArgumentKey.tsName ),"***"," ");
		scriptFile = StringUtils.replace(getArgumentValue( ArgumentKey.scriptFile ),"***"," ");
		testRunName = StringUtils.replace(getArgumentValue( ArgumentKey.runName ),"***"," ");



		logger.debug("isFromQC="+isFromQC+",testName="+testName+",testSetName="+testSetName+",scriptFile="+scriptFile);

		if(StringUtils.isBlank(environment) || StringUtils.isBlank(appName)) {
			throw new RuntimeException("environment and app name, both can't be empty. either pass in command line or set in Config file.");
		}
	}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * This method is for generate test case file name
	 * @return
	 * 				:[String] the test case file name
	 */
	private String generateTestDataFileName() {   //{{
		String fileName =null;
		String template = Script.dicConfigValues.get("MasterTestDataPath");
		if(template == "" || template == null )
		template = properties.getProperty("testdata.location") + properties.getProperty("template.testData.fileName")+Script.dicConfigValues.get("strEnvironment") + ".xls";
		if(StringUtils.isBlank(template)){
			logger.debug("The generation of testdata file name cannt complete due to there nothing degined in property file for the property 'testdata.default.filename'");
			throw new RuntimeException("The generation of testdata file name cannt complete due to there nothing degined in property file for the property 'testdata.default.filename'");
		}

		fileName = template.replace(APPLICATION_NAME_PH,appName)
				.replace(ENVIRONMENT_PH, environment);

		return fileName;
	}
	
	
	
	//////@ Added Report Code
	
	public void PrepSummaryReport(String sTestSuiteName) throws IOException
	{
		
		iSummaryStepCount = 1;
		intcount++;
		//iExceutionTime = new Date();
		Date testStartTime = null;
		//file = new File("C:\\MiMedx_WrkingDir\\Reports\\Reports\\SummaryReport.txt");
		//SummaryPath = "";
		//file = new File(SummaryPath);
		testStartTime = new Date();
		String strSummaryReport = String.format("%s_%s","SummaryReport",DateFormatUtils.format(testStartTime, properties.getProperty("report.dateFormat")));
		
		if (Script.dicConfigValues.get("SummaryReportPath").toLowerCase().contains("default") || Script.dicConfigValues.get("SummaryReportPath").length() == 0 )
		{
			file = new File(sTestSuiteName + "\\"+ strSummaryReport +".html");
		}
		else
		{
			file = new File(Script.dicConfigValues.get("SummaryReportPath") + "\\"+ strSummaryReport +".html");
		}
		
		
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		oReportSummaryWriter = new FileWriter(file.getAbsoluteFile());
		
		 oReportSummaryWriter.write("<html>");
		 oReportSummaryWriter.write("\r\n");
         oReportSummaryWriter.write("<style>");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".subheading { BORDER-RIGHT:#000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-WEIGHT: bold;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #000000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,helvetica, sans-serif;HEIGHT: 20px;BACKGROUND-COLOR: #FAC090;Color: #000000}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".subheading1{BORDER-RIGHT: #000000 1px solid;BACKGROUND-COLOR: #CCC0DA;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-WEIGHT: bold;FONT-SIZE: 13pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #000000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,helvetica, sans-serif;HEIGHT: 10px;}");
         oReportSummaryWriter.write("\r\n");

         //oReportSummaryWriter.write(".subheading2{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 2px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 2px;FONT-WEIGHT: bold;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #000000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,helvetica, sans-serif;HEIGHT: 10px;BACKGROUND-COLOR: #C2DC9A;Color: #000000}");
         oReportSummaryWriter.write(".subheading2{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 2px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 2px;FONT-WEIGHT: bold;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #000000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,helvetica, sans-serif;HEIGHT: 10px;BACKGROUND-COLOR: #6699cc;Color: #000000}");
         
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #000000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri, helvetica,  sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1_Pass{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #00ff00;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,  helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".SnapShotLink_style{PADDING-RIGHT: 4px;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;COLOR: #0000EE;PADDING-TOP: 0px;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1_Fail{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid; COLOR: #ff0000;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1_Done{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid; COLOR: #ffcc00;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri,  helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1_Skipped{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px  solid;COLOR: #00ccff;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".tdborder_1_Warning{BORDER-RIGHT: #000000 1px solid;PADDING-RIGHT: 4px;BORDER-TOP: #000000 1px solid;PADDING-LEFT: 4px;FONT-SIZE: 12pt;PADDING-BOTTOM: 0px;BORDER-LEFT: #000000 1px solid;COLOR: #660066;PADDING-TOP: 0px;BORDER-BOTTOM: #000000 1px solid;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".heading {FONT-WEIGHT: bold; FONT-SIZE: 17px; COLOR: #005484;FONT-FAMILY: Calibri, Verdana, Tahoma, Calibri;}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".style1 { border: 1px solid #8eb3d8;padding: 0px 4px;FONT-WEIGHT: bold;FONT-SIZE: 12pt;COLOR: #000000;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px;width: 180px;}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write(".style3 { border: 1px solid #8eb3d8;padding: 0px 4px;FONT-WEIGHT: bold;FONT-SIZE: 12pt;COLOR: #000000;FONT-FAMILY: Calibri, helvetica, sans-serif;HEIGHT: 20px;width: 2px;}");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write("</style>");
         oReportSummaryWriter.write("\r\n");


         oReportSummaryWriter.write("<head><title>" + Script.dicConfigValues.get("ProjectName") + " Test Result</title></head>");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write("<body vlink=\"FF00FF\">");
         oReportSummaryWriter.write("\r\n");

         oReportSummaryWriter.write("<table cellSpacing='0' cellPadding='0' border='0' align='center' style='width:96%;" +
                                 " margin-left:20px;'><td class='subheading1' colspan=5 align=center><p style='font-size:1.8em'>" +
                                 "<body link='#00ff00' vlink=\"FF00FF\">" + Script.dicConfigValues.get("ProjectName") + " Test Execution Report </body></td><tr></tr></table>");
         oReportSummaryWriter.write("\r\n");
         
		
		
	}
	
	   public void NewSummaryHeader()
       {
			String TestSuiteFileName = null;
		   String BuildVersion= null;
		   String strPrintTestSuiteName = null;
		   
		   //@ Pick data according to different exceution platform[Test Suite or Jira]
		   if(Script.dicConfigValues.get("ExecutionVariant").equalsIgnoreCase("JIRA"))
		   {
			  
			   TestSuiteFileName = Script.dicConfigValues.get("JiraProjectName").toString() + "/"+ Script.dicConfigValues.get("JiraCycleName").toString() ;
			   BuildVersion = Script.dicConfigValues.get("JiraFixVersion").toString();
			   strPrintTestSuiteName = "Jira Project/Cycle";
		   }
		   else
		   {
			   TestSuiteFileName = Script.dicConfigValues.get("TestSuiteFileName").toString();
			   BuildVersion = Script.dicConfigValues.get("strBuildVersion").toString();
			   strPrintTestSuiteName = "Test Suite Name";
		   }

		   
		   String strBrowserType = Script.dicConfigValues.get("strBrowserType").toString();

		   String strEnvironment = Script.dicConfigValues.get("strEnvironment").toString();

		   String strApplicationURL = Script.dicConfigValues.get("strApplicationURL").toString();		   
		   
           //sStartDate = DateTime.Now;
           iSummaryPassStepCount = 0;
           iSummaryFailStepCount = 0;
           iPassTotalCount = 0;
           iFailTotalCount = 0;
           iNoRunTotalCount = 0;
           //iExceutionTime = "";
           
           try{
           
           oReportSummaryWriter.write("<table cellSpacing='0' cellPadding='0' border='0' align='center' style='width:96%; margin-" +
                                   "left:20px;'>");
           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write("<TR>" +
               //" <TD class='subheading2' width ='10%' align='center' >Test Case Id</TD>" +
               //" <TD class='subheading2' align='center' >Test Case Name</TD>" +
               //" <TD class='subheading2' align='center'>Environment</TD>");
                                    " <TD class='subheading2' width ='10%' align='center' >"+ strPrintTestSuiteName +"</TD>" +
                                   " <TD class='subheading2' align='center'>Environment</TD>" +
                                   "<TD class='subheading2' align='center'>Build Version</TD>");
           oReportSummaryWriter.write("\r\n");

  

           oReportSummaryWriter.write(" <TD class='subheading2'align='center'>Browser</TD>");
           oReportSummaryWriter.write("\r\n");

           oReportSummaryWriter.write(" <TD class='subheading2' align='center'>Application URL</TD>" +
                                   " </TR>");
           oReportSummaryWriter.write("\r\n");

           oReportSummaryWriter.write("<TR>" +
               //" <TD class='tdborder_1'  vAlign=center  align=middle >" + dicDict["TestCaseId"] + "</TD>" +
               //" <TD class='tdborder_1'  vAlign=center  align=middle >" + dicDict["TestCaseName"] + "</TD>" +
               //" <TD class='tdborder_1'  vAlign=center  align=middle >" + dicConfig["strEnvironment"] + "</TD>");
                       " <TD class='tdborder_1'  vAlign=center  align=middle >" + TestSuiteFileName + "</TD>" +
                       " <TD class='tdborder_1'  vAlign=center  align=middle >" + strEnvironment + "</TD>" +
                       "<TD class='tdborder_1'  vAlign=center  align=middle >" + BuildVersion + "</TD>");

           oReportSummaryWriter.write("\r\n");
               //string strBVer = GetBrowserVersion();
               ////Console.WriteLine(strBVer);
          oReportSummaryWriter.write(" <TD class='tdborder_1'  vAlign=center  align=middle >" + strBrowserType + "<br> Version( " + strfullVersion + " )</TD>");


           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write(" <TD class='tdborder_1'  vAlign=center  align=middle >" + strApplicationURL + "</TD>" + "</TR>");
           
                                  
           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write("</table>");

           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write("<table cellSpacing='0' cellPadding='0' border='0' align='center' style='width:96%;" +
                                   " margin-left:20px;'>");

           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write("<tr></tr>");

           oReportSummaryWriter.write("\r\n");
           oReportSummaryWriter.write("<tr>" +
                                   " <td class='subheading2' width ='5%' align='center'>TC#</td>" +
                                   " <td class='subheading2'  width ='40%' align='center'>Test Case Scenarios</td>" +
                                   " <td class='subheading2'  width ='45%' align='center'>Test Case Status</td>" +
                                   " <td class='subheading2' width ='10%' align='center'>Execution Time</td>" +
                                   " </tr>");

           oReportSummaryWriter.write("\r\n");
           }
           
           catch (Exception e){}

       }
	
	   
	   public void HTMLSummaryReport (String TestCaseScenario, String TestCaseStatus, String ExecutionTime, String strAttachReportPath)
       {
		   
		   try{

           oReportSummaryWriter.write("<tr>"); 
           oReportSummaryWriter.write("\r\n");	
           oReportSummaryWriter.write("<TD class='tdborder_1'  align='center'>" + iSummaryStepCount++ + "</TD>");
           oReportSummaryWriter.write("\r\n");	
           //oReportSummaryWriter.WriteLine("<td class ='tdborder_1'>" + TestCaseScenario + "</td>");
           //oReportSummaryWriter.WriteLine("<td class ='tdborder_1'><a href=" + strAttachReportPath + " style='color:blue'>" + TestCaseScenario + "</a></td>");
           oReportSummaryWriter.write("<td class ='tdborder_1'><a href=" + strAttachReportPath + " style='color:blue' target=\"_blank\">" + TestCaseScenario + "</a></td>");
           oReportSummaryWriter.write("\r\n");
           
           
           if (TestCaseStatus.toUpperCase() == "FAIL")
           {

               oReportSummaryWriter.write("<td class ='tdborder_1' style='color:red'>" + TestCaseStatus.toUpperCase() + "</td>");
               ++iFailTotalCount;
           }
           else
           {
               oReportSummaryWriter.write("<td class ='tdborder_1' style='color:green'>" + TestCaseStatus.toUpperCase() + "</td>");
               //oReportSummaryWriter.WriteLine("<td class ='tdborder_1'><a href=" + strReportPath + ">" + TestCaseStatus + "</a></td>");
               ++iPassTotalCount;
           }

           oReportSummaryWriter.write("\r\n");
         	
           
			/*String[] h1=ExecutionTime.split(":");  
			int hour=Integer.parseInt(h1[0]);  
			int minute=Integer.parseInt(h1[1]);  
			int second=Integer.parseInt(h1[2]);  
			int Temp = second + (60 * minute) + (3600 * hour);
			
           
			iExceutionTime = iExceutionTime + Temp;*/
			
           //DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
           //iExceutionTime = iExceutionTime + ExecutionTime;          

          
           
           oReportSummaryWriter.write("<td class ='tdborder_1 width ='40%' align='center'>" + ExecutionTime + "</td>");
           oReportSummaryWriter.write("\r\n");
           //oReportSummaryWriter.WriteLine(" <td class ='tdborder_1_Pass' width ='20%' align='center'>" + ExecutionTime + "</td>");
           //iPassStepCount += 1
           iSummaryPassStepCount += 1;
           
           oReportSummaryWriter.write("</tr>");

           //TestCaseExecutionTime = TimeSpan.Parse("00:00:00");
		   }
		   catch(Exception e){}
       }
	   
	   public void EndSummaryTestReport()
       {


		   try{
           //if (dicConfig["ReportingFormat"].ToLower().Contains("html"))
           if ("html".toLowerCase().contains("html"))
           {
               //string sTestDur = strHour + ":" + strMin + ":" + strSec;//DDiff.Hours.ToString("HH") + ":" + DDiff.Minutes.ToString("mm") + ":" + DDiff.Seconds.ToString("ss");

               oReportSummaryWriter.write("</table>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("<table cellSpacing='0' cellPadding='0' border='0' align='center' style='width:96%; " +
                                       " margin-left:20px;'>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("<TR></TR>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("<TR>");
               oReportSummaryWriter.write("<TD class='subheading2' align='center'>Test Cases-Passed</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='subheading2' align='center'>Test Cases-Failed</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='subheading2' align='center'>Test Cases-Not Run</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='subheading2' align='center'>Totals</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='subheading2' align='center'>Test Suite Duration</TD>");
               oReportSummaryWriter.write("\r\n");


               oReportSummaryWriter.write("</TR>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("<TR>");
               oReportSummaryWriter.write("<TD class='tdborder_1'  align='center' >" + iPassTotalCount + "</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='tdborder_1'  align='center'>" + iFailTotalCount + "</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='tdborder_1'  align='center'>" + iNoRunTotalCount + "</TD>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<TD class='tdborder_1'  align='center'>" + (iPassTotalCount + iFailTotalCount) + "</TD>");
               oReportSummaryWriter.write("\r\n");
               
               String time = new SimpleDateFormat("HH:mm:ss"){{setTimeZone(TimeZone.getTimeZone("UTC"));}}.format(new Date(iExceutionTime * 1000));
               
               oReportSummaryWriter.write("<TD class='tdborder_1' align='center' >" + time + "</TD>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("</TR>");
               oReportSummaryWriter.write("\r\n");
               oReportSummaryWriter.write("<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>");
               oReportSummaryWriter.write("\r\n");

               oReportSummaryWriter.write("</table>");
               oReportSummaryWriter.write("\r\n");
               //Finishing Report
               oReportSummaryWriter.write("</table></body></html>");
   
               oReportSummaryWriter.close();
           
           
           }
		   
		   }
		   catch (Exception e){}


           //Check if the iteration has passed or not and if not, make final status as fail

           //if (!(iSummaryFailStepCount == 0 && iSummaryPassStepCount != 0))
           //{
           //    //strFinalStatus = "Fail";
           //}

           //Microsoft.Win32.RegistryKey key;
           //key = Microsoft.Win32.Registry.CurrentUser.CreateSubKey("strStatus");
           //key.SetValue("strStatus", iSummaryFailStepCount);
           //key.Close();

       }
	
	
	   public int ConvertTimeIntoInt(String ExecutionTime)
	   {
		   String[] h1=ExecutionTime.split(":");  
			  
			int hour=Integer.parseInt(h1[0]);  
			int minute=Integer.parseInt(h1[1]);  
			int second=Integer.parseInt(h1[2]);  
			int Temp = second + (60 * minute) + (3600 * hour);
			iExceutionTime = iExceutionTime + Temp;
			return iExceutionTime;
	   }
	   
	
	
}



