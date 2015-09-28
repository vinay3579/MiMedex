package com.pyramidconsulting.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.pyramidconsulting.Launcher;
import com.pyramidconsulting.runner.Script;
import com.pyramidconsulting.util.ScreenUtil;
import com.thoughtworks.selenium.Selenium;
import org.apache.commons.codec.digest.DigestUtils;

public class ExcelReporter extends AbstractReporter {

	public ExcelReporter( String testCaseName,  String environment,
			  String buildVersion,
			 String browser,  String applicationUrl,  Selenium selenium,  WebDriver driver) {

		super(testCaseName, environment, buildVersion, browser,
				applicationUrl, selenium, driver);
	}

	public File screenshotFileName;
	public int failstep=0;
	public static int col=10,row=1;
	public static int colnew=0,rownew=0;
	public static List a = new ArrayList();		// this list is used to store failsteps to use in naming screenshot files
	private static int excelRowCount=0;

	@Override
	protected byte[] getReportContent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected byte[] getReportHeader() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addStepStatus(final String description, String expectedResult,
			 String actualResult, final String stepStatus) {
		// TODO Auto-generated method stub

	}
	
	public String getTimeDiff(long timeDuration)
	{
		return null;
	}

	@Override
	protected String getFileExtension() {
		return "xls";
	}
	
	public void Enterdatainexcel(String testCaseName, String environment, String buildVersion, String browser, String applicationUrl) throws IOException{
		HSSFWorkbook hwb =null;
		HSSFSheet sheet =null;
		if(Launcher.chkIteration == false)
		{
		hwb =new HSSFWorkbook();
		sheet =  hwb.createSheet("TestCase Report");	
		filename=generateFileNameWithLocation()+".xls";
		
		//**********code for topmost header-- displaying ProjectName(TESTCASE REPORT) ********
		HSSFRow rowTop=   sheet.createRow((short)excelRowCount);
		rowTop.setHeightInPoints((short) 20); 
		HSSFCell cellTop = rowTop.createCell((short)1);
		sheet.addMergedRegion(new Region(excelRowCount,(short)1,excelRowCount,(short)4));
		HSSFCellStyle cellStyleTop = hwb.createCellStyle();  
        cellStyleTop = hwb.createCellStyle();  
		HSSFFont hSSFFontTop = hwb.createFont();
		hSSFFontTop.setFontHeightInPoints((short) 15);  
        hSSFFontTop.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        hSSFFontTop.setColor(HSSFColor.BLACK.index);  
        cellStyleTop.setFont(hSSFFontTop);
        cellStyleTop.setAlignment(cellStyleTop.ALIGN_CENTER);
        cellTop.setCellStyle(cellStyleTop);
        cellTop.setCellValue(Script.dicConfigValues.get("ProjectName") + " - Test Case Report");
		excelRowCount++;
		////////////////*************************************************************************
		HSSFRow row=   sheet.createRow((short)excelRowCount);
		HSSFCell cell = row.createCell((short)0);
		
		HSSFCellStyle cellStyle = hwb.createCellStyle();  
        cellStyle = hwb.createCellStyle();  
		HSSFFont hSSFFont = hwb.createFont();
		hSSFFont.setFontHeightInPoints((short) 10);  
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        hSSFFont.setColor(HSSFColor.WHITE.index);  
        cellStyle.setFont(hSSFFont);
        cellStyle.setAlignment(cellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        cell.setCellStyle(cellStyle);  
		cell.setCellValue("Test Case Name");
		
		cell = row.createCell((short) 1);
		cell.setCellStyle(cellStyle);  
		cell.setCellValue("Environment");
		
		cell =row.createCell((short) 2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Build Version");
		
		cell= row.createCell((short) 3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Browser");
		
		cell= row.createCell((short) 4);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Application URL");
		
		sheet.setColumnWidth(0, 5120);
		sheet.setColumnWidth(1, (5120*2));
		sheet.setColumnWidth(2, (5120*2));
		sheet.setColumnWidth(3, 5120);
		sheet.setColumnWidth(4, (5120*2));
		excelRowCount++;
		
		HSSFRow row1=   sheet.createRow((short)excelRowCount);
		HSSFCellStyle tCs1 = hwb.createCellStyle();  
		tCs1.setFillForegroundColor(HSSFColor.BLUE.index);
		row1.createCell((short) 0).setCellValue(testCaseName);
		row1.createCell((short) 1).setCellValue(environment);
		row1.createCell((short) 2).setCellValue(buildVersion);		
		row1.createCell((short) 3).setCellValue(browser);
		row1.createCell((short) 4).setCellValue(applicationUrl);
		excelRowCount++;
		FileOutputStream fileOut =  new FileOutputStream(filename);
		hwb.write(fileOut);
		fileOut.close();
		}
		else
		{
			POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(filename)); 
			hwb = new HSSFWorkbook(fileSystem); 
			sheet = hwb.getSheetAt(0);
		}
		//Headers in the excel sheet
		excelRowCount++;
		HSSFRow rowhead=   sheet.createRow((short)excelRowCount);
		
		HSSFCell cell = rowhead.createCell((short)0);
		HSSFCellStyle cellStyle = hwb.createCellStyle();  
        cellStyle = hwb.createCellStyle();  
		HSSFFont hSSFFont = hwb.createFont();
		hSSFFont.setFontHeightInPoints((short) 10);  
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        hSSFFont.setColor(HSSFColor.WHITE.index);  
        cellStyle.setFont(hSSFFont);  
        cellStyle.setAlignment(cellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        cell.setCellStyle(cellStyle);  
		cell.setCellValue("Step No.");
		
		cell = rowhead.createCell((short) 1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Description");
		
		cell =rowhead.createCell((short) 2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Expected Result");
		
		cell =rowhead.createCell((short) 3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Actual Result");
		
		cell = rowhead.createCell((short) 4);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Status");
		
		cell = rowhead.createCell((short) 5);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("ScreenShot Link");
		excelRowCount++;
		FileOutputStream fileOut =  new FileOutputStream(filename);
		hwb.write(fileOut);
		fileOut.close();
		System.out.println("Your excel file has been generated!");
		
	
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

	public void ReportStep(String description, String expectedResult, String actualResult,  String stepStatus) throws FileNotFoundException, IOException{
		
		try{
			String screenShotLink="";
			incTestStepNo();
			String reportsPath = "";
			String reportPathinMap =Script.dicConfigValues.get("reportsPath");
			if(!(reportPathinMap.equals("") || reportPathinMap.equals(null)))
	        	reportsPath =  reportPathinMap;
			else
				reportsPath = properties.getProperty("report.location");
			String screenCaptureAres = properties.getProperty("report.screenshot.area");
			String screenshot = Script.dicConfigValues.get("ScreenShot").toLowerCase();
			String seleniumVariant = Script.dicConfigValues.get("SeleniumVariant").toLowerCase();
			
			
			////////////////////////code for screen shots
			if (stepStatus.equals("Pass")) 
			{
				failstep++;
				incTestStepPassCount();
				boolean isScreenCaptured = true;
				if(screenshot.toLowerCase().equals("yes"))
				{
					actualResult = capturescreen(screenCaptureAres, seleniumVariant, reportsPath, actualResult);
					screenShotLink = generateScreenshotFileName();
				}
			} 
			else	// when step fails 
			{
			incTestStepFailCount();
			failstep++;
			a.add(failstep);
			actualResult = capturescreen(screenCaptureAres, seleniumVariant, reportsPath, actualResult);
			screenShotLink = generateScreenshotFileName();
			}
			
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
		HSSFSheet sheet = workbook.getSheetAt(0);
		//HSSFRow row = sheet.getRow(step+1);
		
		//Adding data to excel
		HSSFRow row =  sheet.createRow(excelRowCount);
		HSSFCell cell = row.createCell((short) 0);
		CellStyle style = workbook.createCellStyle();
	    style.setAlignment(style.ALIGN_CENTER);
	    cell.setCellStyle(style);
		cell.setCellValue(getTestStepNo());
		row.createCell((short) 1).setCellValue(description);
		row.createCell((short) 2).setCellValue(expectedResult);
		row.createCell((short) 3).setCellValue(actualResult);
		row.createCell((short) 4).setCellStyle(style);
		CellStyle styleStatus;
		if(stepStatus.toString().toLowerCase().equals("pass"))
		{
			styleStatus = workbook.createCellStyle();
			styleStatus.setAlignment(style.ALIGN_CENTER);
			HSSFFont hSSFFontStatus = workbook.createFont();
			hSSFFontStatus.setColor(HSSFColor.GREEN.index);  
	        styleStatus.setFont(hSSFFontStatus);
			HSSFCell statusCell =row.createCell((short) 4);
			statusCell.setCellStyle(styleStatus);
		    statusCell.setCellValue(stepStatus.toString());
		}
		else
		{
			styleStatus = workbook.createCellStyle();
			styleStatus.setAlignment(style.ALIGN_CENTER);
			HSSFFont hSSFFontStatus = workbook.createFont();
			hSSFFontStatus.setColor(HSSFColor.RED.index);  
	        styleStatus.setFont(hSSFFontStatus);
			HSSFCell statusCell =row.createCell((short) 4);
			statusCell.setCellStyle(styleStatus);
		    statusCell.setCellValue(stepStatus.toString());
		}
		//row.createCell((short) 3).setCellValue(stepStatus.toString());
		if(!screenShotLink.equals(""))
		{
			row.createCell((short) 5).setCellFormula("HYPERLINK(\"" + screenShotLink+ "\",\"" + "Refer ScreenShot" + "\")");
		}
		excelRowCount++;
		//Writting data to excel
		FileOutputStream fileOut =  new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.close();	
		System.out.println("Your excel file has been generated!");
		}
		catch(Exception e)
		{
		System.out.println(e.getMessage());	
		}
		}

}
