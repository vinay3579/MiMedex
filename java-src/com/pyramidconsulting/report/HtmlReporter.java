package com.pyramidconsulting.report;

import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_APPLICATION_URL;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_BROWSER;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_BUILD_VERSION;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_CASE_NAME;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_ENVIRONMENT;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.PROJECT_NAME;      //{}
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_EXECUTION_TIMESTAMP;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_ITERATION_COUNT;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_MACHINE_NAME;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_RUN_DURATION;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_STEPS_FAIL_COUNT;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_STEPS_PASS_COUNT;
import static com.pyramidconsulting.report.ReportingConstants.TestReport.TEST_STEP_REPORTS;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_EXPECTED_RESULT;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_ACTUAL_RESULT;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_DESCRIPTION;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_NUMBER;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_STATUS;
import static com.pyramidconsulting.report.ReportingConstants.TestStepReport.TEST_STEP_STATUS_CLASS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.pyramidconsulting.util.IOUtils;
import com.thoughtworks.selenium.Selenium;



public class HtmlReporter extends AbstractReporter {

	private static Logger logger = Logger.getLogger(HtmlReporter.class);
	private StringBuilder testStepReports = new StringBuilder();
	private String testStepTemplate = null;
	private String testReportTemplate = null;
	private String testReportContentTemplate = null;
	
	//@ Added Code for @ HTML Summary Report by kamlesh  
	public static String strExceutionDuration = null;
	////////////////////////////////////////////////////
	
	//lines below added for properties access

	public HtmlReporter( String testCaseName,  String environment,
			 String buildVersion,
			 String browser,  String applicationUrl,  Selenium selenium,  WebDriver driver)  {

		super(testCaseName, environment, buildVersion, browser,
				applicationUrl, selenium, driver);
	}


	@Override
	protected byte[] getReportHeader() throws FileNotFoundException,
	IOException {
		if (testReportTemplate == null) {
			loadReportTemplate();
		}

	/*	return testReportTemplate
				.replace(TEST_STEPS_PASS_COUNT,
						getTestStepPassCount().toString())
						.replace(TEST_STEPS_FAIL_COUNT, getTestStepFailCount().toString())
						.replace(TEST_ITERATION_COUNT,
								getTestIterationCount().toString())
								.replace(TEST_STEP_REPORTS, testStepReports.toString())
								.replace(
										TEST_EXECUTION_TIMESTAMP,
										DateFormatUtils.format(getTestExecutionTime(),
												properties.getProperty("report.dateFormat")))
												.getBytes();
*/
		return testReportTemplate.getBytes();
	}
	@Override
	protected byte[] getReportContent() throws FileNotFoundException,
	IOException {
		if (testReportContentTemplate == null) {
			loadReportContentTemplate();
		}

		return testReportContentTemplate
				.replace(TEST_STEPS_PASS_COUNT,
						getTestStepPassCount().toString())
						.replace(TEST_STEPS_FAIL_COUNT, getTestStepFailCount().toString())
						.replace(TEST_ITERATION_COUNT,
								getTestIterationCount().toString())
								.replace(TEST_STEP_REPORTS, testStepReports.toString())
								.replace(
										TEST_EXECUTION_TIMESTAMP,
												DateFormatUtils.format(getTestExecutionTime(),"MM/dd/yyyy_HH:mm:ss"))
												.getBytes();

		
	}
	private void loadTeststepReportTemplate() throws FileNotFoundException,
	IOException {
		try{
		 String templateFile = properties
				.getProperty("report.html.testresult.template");
		logger.info("Reading teststep report template file [" + templateFile
				+ "]");
		/*final File templateFileObj = new File(templateFile);
		if(!templateFileObj.exists()) {
			logger.error("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
			throw new RuntimeException("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
		}

		testStepTemplate = FileUtils.readFileToString(templateFileObj);*/
		testStepTemplate = IOUtils.readFileContentFromClasspath(templateFile);
		if(StringUtils.isEmpty(testStepTemplate)) {
			logger.error("could not read template file ["+templateFile+"] from classpath.");
			throw new RuntimeException("could not read template file ["+templateFile+"] from classpath.");
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	private void loadReportTemplate() throws FileNotFoundException, IOException {
		try{

		 String templateFile = properties
				.getProperty("report.html.report.template");

		/*final File templateFileObj = new File(templateFile);
		if(!templateFileObj.exists()) {
			logger.error("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
			throw new RuntimeException("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
		}
		logger.info("Reading report template file [" + templateFileObj.getAbsolutePath() + "].");
		 */
		testReportTemplate = IOUtils.readFileContentFromClasspath(templateFile);
		if(StringUtils.isEmpty(templateFile)) {
			logger.error("could not read template file ["+templateFile+"] from classpath.");
			throw new RuntimeException("could not read template file ["+templateFile+"] from classpath.");
		}
		
		
		 String machineName = InetAddress.getLocalHost().getHostName();

		 long timeDuration = new Date().getTime() - getTestExecutionTime().getTime();
		 
		 String diff = DurationFormatUtils.formatDuration(timeDuration,
				properties.getProperty("report.timeFormat"));

		testReportTemplate = testReportTemplate.replace(TEST_CASE_NAME, getTestCaseName())
				.replace(PROJECT_NAME, getProjectName())
				.replace(TEST_ENVIRONMENT, getEnvironment())
				//.replace(TEST_HOST_NAME, getHostName())
				.replace(TEST_BUILD_VERSION, getBuildVersion())
				.replace(TEST_BROWSER, getBrowser())
				.replace(TEST_MACHINE_NAME, machineName.toUpperCase())
				.replace(TEST_RUN_DURATION, diff)
				.replace(TEST_APPLICATION_URL, getApplicationUrl());

	}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	//@ Added Test Code by Kamlesh
	@Override
	public String getTimeDiff(long timeDuration)
	{
		 String diff = DurationFormatUtils.formatDuration(timeDuration,
					properties.getProperty("report.timeFormat"));
		
		 strExceutionDuration = diff;
		 return diff;
	}
	///////////////////////////////////////////////////////////
	
	private void loadReportContentTemplate() throws FileNotFoundException, IOException {
		try{

		 String templateFile = properties
				.getProperty("report.html.report.content.template");

		/*final File templateFileObj = new File(templateFile);
		if(!templateFileObj.exists()) {
			logger.error("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
			throw new RuntimeException("Template file ["+templateFileObj.getAbsolutePath()+"] does not exist.");
		}
		logger.info("Reading report template file [" + templateFileObj.getAbsolutePath() + "].");
		 */
		 testReportContentTemplate = IOUtils.readFileContentFromClasspath(templateFile);
		if(StringUtils.isEmpty(templateFile)) {
			logger.error("could not read template file ["+templateFile+"] from classpath.");
			throw new RuntimeException("could not read template file ["+templateFile+"] from classpath.");
		}
		 String machineName = InetAddress.getLocalHost().getHostName();
 
		 long timeDuration = new Date().getTime() - getTestExecutionTime().getTime();
/*		 
		 String diff = DurationFormatUtils.formatDuration(timeDuration,
				properties.getProperty("report.timeFormat"));*/
		 
		 String diff = getTimeDiff(timeDuration);
		 //strExceutionDuration = diff;
		 
		 testReportContentTemplate = testReportContentTemplate
				//.replace(TEST_ENVIRONMENT, getEnvironment())
				//.replace(TEST_HOST_NAME, getHostName())
				//.replace(TEST_BUILD_VERSION, getBuildVersion())
				//.replace(TEST_BROWSER, getBrowser())
				.replace(TEST_MACHINE_NAME, machineName.toUpperCase())
				.replace(TEST_RUN_DURATION, diff);
				//.replace(TEST_APPLICATION_URL, getApplicationUrl());*/

	}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	@Override
	public void addStepStatus( String description, String expectedResult,
			 String actualResult,   String stepStatus)
					throws FileNotFoundException, IOException {
		try{
		if (testStepTemplate == null) {
			loadTeststepReportTemplate();
		}
		String status = testStepTemplate
				.replace(TEST_STEP_NUMBER, getTestStepNo().toString())
				.replace(TEST_STEP_DESCRIPTION, description)
				.replace(TEST_STEP_EXPECTED_RESULT, expectedResult)
				.replace(TEST_STEP_ACTUAL_RESULT, actualResult)
				.replace(TEST_STEP_STATUS, stepStatus);
		if(stepStatus.equals("Pass")) {
			status = status.replace(TEST_STEP_STATUS_CLASS, "tdborder_1_Pass");
		} else {
			status = status.replace(TEST_STEP_STATUS_CLASS, "tdborder_1_Fail");
		}

		testStepReports.append(status).append("\n");
	}
		catch(Exception e)
		{
			System.out.println(e);
		}
		}

	@Override
	protected String getFileExtension() {
		return "html";
	}
	


}
