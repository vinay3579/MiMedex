package com.utilTest;
//import com.pyramidconsulting;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.pyramidconsulting.reader.ExcelReader;
import com.pyramidconsulting.reader.ExcelReaderImpl;
import com.pyramidconsulting.runner.Script;

public class ExcelUtil {
	
	public static void WriteToExcel(String colValuetoUpdate, String value){
	try {
		
		int rowNum = 0;
		
		String strEnvironment = Script.dicConfigValues.get("strEnvironment");
		
	    FileInputStream fis= new FileInputStream(".\\Datasheets\\MasterTestData_"+strEnvironment+".xls"); 
	    
        HSSFWorkbook wb = new HSSFWorkbook(fis); 
        
        HSSFSheet sheet = wb.getSheet("CommonValues");
        ExcelReader testSuite = new ExcelReaderImpl(".\\Datasheets\\MasterTestData_"+strEnvironment+".xls");
        testSuite.setRowIdentifierColName("ITEM", "CommonValues");
		String rowIds[] = testSuite.getAllRowIdentifierValues("CommonValues");        
		 
		for (String rowId : rowIds)
	  	{	
			rowNum = rowNum + 1;
	  		if(rowId.equals(colValuetoUpdate))
	  		{	  			
	  			break;
	  		}
	  	}
		
		if(rowNum !=0)
		{
	  		
        Row row = sheet.getRow(rowNum);    
        Cell cell = row.getCell(1);    
        if (cell == null)    
           cell = row.createCell(1);    
        cell.setCellType(Cell.CELL_TYPE_STRING);    
        cell.setCellValue(value);    
         
		}
		else
		{
			System.out.println("The Column value mentioned is incorrect ("+colValuetoUpdate+")");		
		}
        fis.close();

       
        FileOutputStream output_file = new FileOutputStream(".\\Datasheets\\MasterTestData_"+strEnvironment+".xls");
   
        wb.write(output_file); 
    
        output_file.close();  
		}
	
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
}
	public static void main(String[] args) {
		//ExcelUtil excel=new ExcelUtil();
		//ExcelUtil.WriteToExcel("strRecoveryId_DonorProcessing","2017");
		//System.out.println("Excel updated.");
	}
}
