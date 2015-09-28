package com.pyramidconsulting.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class ExcelReaderImpl implements ExcelReader {

	/**
	 * Data-member Description
	 * 
	 *  	logger   				To produce the logger informations
	 *  	isInitialized			To indicate the object of ExcelReaderImp is initialized or not.
	 *  	excelFileName			Contains the Excel file name that is associated with the current object
	 * 		workBookData			Contains the data of all sheets.
	 * 		headers					Contains the names of Headers of every sheets.
	 * 		totalSheetsCount		Indicates the total sheet numbers.
	 * 		workBook				Contains the Excel file (HSSFWorkBook).
	 * 		rowIdentiferColNames	It stores the "row identifier column names " of every sheets.
	 * 		deafaultRetColumnName	It stores the "row retrieval column names " of every sheets
	 * 		defaultSheetIndex		It stores the default sheet index.
	 * 
	 */
	private static  Logger logger = Logger
			.getLogger(ExcelReaderImpl.class);
	private boolean isInitialized = false;
	private  String excelFileName;
	private ArrayList<String[][]> workBookData = null;
	private HashMap<String,ArrayList<String>> headers =null;
	private int totalSheetsCount = 0;
	private HSSFWorkbook workBook =null;
	private  HashMap<String, String> rowIdentiferColNames = new HashMap<String, String>();
	private  HashMap<String, String> deafaultRetColumnName = new HashMap<String, String>();
	private int defaultSheetIndex = 0;


	public ExcelReaderImpl( File excelFile) {		
		this(excelFile.getAbsolutePath());			
	}

	/**
	 * @param excelFileName
	 *            : name of excel file containing the metadata.
	 */
	public ExcelReaderImpl( String excelFileName) {
		this.excelFileName = excelFileName;
		headers = new HashMap<String, ArrayList<String>>();
		workBookData = new ArrayList<String[][]>();		
	}

	/**
	 * returns the sheet index if sheetName found in workbook. otherwise throws
	 * an runtime exception.
	 * 
	 * @param sheetName
	 *            : name of sheet for which index is required.
	 * @return: sheet index.
	 */
	private int  getSheetIndex( String sheetName) {
	
		int sheetIdx = -1;
		if(isInitialized == false) {
			init();
		}
		sheetIdx = workBook.getSheetIndex(sheetName);
		if(sheetIdx < 0) {
			logger.error("sheet '" + sheetName
					+ "' sheet does not exist in the workbook " + excelFileName);
			throw new RuntimeException("sheet '" + sheetName
					+ "' sheet does not exist in the workbook " + excelFileName);
		}
		return sheetIdx;
		
	}

	/**
	 * This method is used for get the sheet name
	 * @param sheetIndex
	 * 					: Index of sheet for which Name is required.
	 * @return			: The Sheet Name
	 */
	private String getSheetName( int sheetIndex) {
		String sheetName = null;
		if (isInitialized == false) {
			logger.info("The sheet index '"+sheetName+"' is not loaded yet, is going to be loaded");
			init();
		}
		sheetName = workBook.getSheetName(sheetIndex);
		if (sheetName == null) {
			logger.error("sheet index " + sheetIndex
					+ " does not exist in the workbook " + excelFileName);
			throw new RuntimeException("sheet index " + sheetIndex
					+ " does not exist in the workbook " + excelFileName);
		}
		return sheetName;
	}

	/**
	 * This method is used for load the particular sheet
	 * @param sheetIndex
	 * 				:Index of the sheet which will be loaded.
	 */
	private void load( int sheetIndex) {
		try{
		if (isInitialized == false) {
			init();
		}
		if (workBookData.get(sheetIndex) == null) {
			// method call for load the sheet data of index at sheetIndex
			workBookData.set(sheetIndex, getSheetData(sheetIndex));
			logger.info("The data has been loaded of sheet : '"
					+ getSheetName(sheetIndex) + "', of the workbook : '"
					+ excelFileName+"'.");
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * This method is used for load the particular sheet
	 * @param sheetIndex
	 * 				:Name of the sheet which will be loaded.
	 */
	private void load( String sheetName)
	{
		try{
		 int sheetIndex = getSheetIndex(sheetName);
		load(sheetIndex);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getValue(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public String getValue( String sheetName,
			 int rowIdentifierColNo,  String rowIdentifierValue,
			 String columnName) {
		logger.debug("Getting the column value for column = "+columnName+" in sheet ="+sheetName+" as per  rowIdentifierValue ="+rowIdentifierValue+" in rowIdentifierColumnNmae = "+rowIdentiferColNames.get(sheetName));
		String cellValue = null;
		 int sheetIdx  = getSheetIndex(sheetName);
		// call the sheet load method which will check for valid sheet and load if it is valid and not already loaded
		load(sheetName);
		if(rowIdentifierColNo > headers.get(workBook.getSheetName(sheetIdx)).size() || rowIdentifierColNo <0){
			logger.error("the "+rowIdentifierValue +" Doesnt Exist in the Sheet "+sheetName);
			throw new RuntimeException("the "+rowIdentifierValue +" Doesnt Exist in the Sheet "+ sheetName);
		}
		 int rowNum = getRowIndexOfValue(rowIdentifierValue, rowIdentifierColNo, sheetIdx);

		if(rowNum == -1){
			logger.error("the " + rowIdentifierValue
					+ " doesn't exist in the sheet '" + sheetName
					+ "' in the column "
					+ headers.get(sheetName).get(rowIdentifierColNo));

			throw new RuntimeException("the '" + rowIdentifierValue
					+ "' Doesnt Exist in the Sheet " + sheetName
					+ " in the column " + headers.get(sheetName).get(rowIdentifierColNo));
		}
		cellValue = workBookData.get(sheetIdx)[rowNum][headers.get(sheetName)
		                                               .indexOf(columnName.toUpperCase())];
		logger.debug("The column value for column = " + columnName
				+ " in sheet =" + sheetName + " as per  rowIdentifierValue ="
				+ rowIdentifierValue + " in rowIdentifierColumnNmae = "
				+ rowIdentiferColNames.get(sheetName) + " is " + cellValue);
		return cellValue;
	}

	/**
	 * This method returns the Row Index of searching row identifier column value.
	 * @param rowIdentifierValue
	 * 					:  Row Identifier Column Value indicates the criteria string against which value is needed.
	 * @param rowIdentifierColNo
	 * 					: Row Identifier Column Number in which column the value will be searched.
	 * @param sheetIdx
	 * 					: Index of the Sheet
	 * @return			: The row index value of int type.
	 */
	private int getRowIndexOfValue( String rowIdentifierValue ,  int rowIdentifierColNo , int sheetIdx)
	{
		 String sheetName = getSheetName(sheetIdx);
		 String[][] sheetData = workBookData.get(sheetIdx);
		logger.debug("Getting the Row Index Of Value for rowidentifiervalue ="+rowIdentifierValue+" in coumn = "+ rowIdentiferColNames.get(sheetName)+" in the sheet = "+workBook.getSheetName(sheetIdx));

		// get the row number matching the search string
		boolean isFound = false;
		int rowNo = -1;
		while (!isFound && rowNo < sheetData.length - 1) {
			rowNo++;
			if (sheetData[rowNo][rowIdentifierColNo]
					.equals(rowIdentifierValue)) {
				isFound = true;
			}
		}
		if(!isFound) {
			rowNo = -1;
		}
		logger.debug("The Row Index Of Value for rowidentifiervalue ="
				+ rowIdentifierValue + " in coumn = "
				+ rowIdentiferColNames.get(sheetName) + " in the sheet = "
				+ workBook.getSheetName(sheetIdx) + " is " + rowNo);
		return rowNo;
	}
	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getValue( String sheetName,
			 String rowIdentifierValue, final String columnName) {
		load(sheetName);
		return getValue(sheetName, getColIndex(rowIdentiferColNames.get(sheetName),sheetName), rowIdentifierValue, columnName);
	}

	/**
	 * This method initialize  the object of ExcelReaderImpl ,
	 * in which it checks the type , existence validity of the file.
	 * @throws DataFileNotFoundException
	 * @throws UnsupportedFileTypeException
	 */
	private void init() throws DataFileNotFoundException,
	UnsupportedFileTypeException {
		logger.info("The '"+excelFileName+"' workbook is not initialized yet , is going to be initialize");

		if (!excelFileName.toLowerCase().endsWith(".xls")
				&& !excelFileName.toLowerCase().endsWith(".xlsx")
				&& !excelFileName.toLowerCase().endsWith(".csv")) {
			throw new UnsupportedFileTypeException(
					"Unsupported file type:"
							+ excelFileName
							+ ", framework supports only following file types .XLS,.XLSX,.CSV");
		}

		 File file = new File(excelFileName);
		if (!file.exists()) {
			throw new DataFileNotFoundException("File " + file.getAbsolutePath()
					+ " does not exist");
		}

		try {
			workBook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(file)));
			totalSheetsCount = workBook.getNumberOfSheets();

			for (int i = 0; i < totalSheetsCount; i++) {
				workBookData.add(null);
			}
			logger.info("The workbook has been initialized from ["
					+ file.getAbsolutePath() + "] which contains ["
					+ totalSheetsCount + "] sheets.");
		} catch ( FileNotFoundException e) {
			logger.error("The ExcelFile Doesnt Exist "+e);
			throw new DataFileNotFoundException("File " + excelFileName
					+ " does not exist");
		} catch ( IOException e) {
			logger.error("The ExcelFile cannt be readable "+e);
			throw new DataFileNotFoundException("File " + excelFileName
					+ " can not be read");
		}

		isInitialized = true;
		logger.info("The '"+excelFileName+"' workbook has been initialized now");

	}

	/**
	 * This method returns the whole data of a particular sheet index
	 * @param sheetIdx
	 * 				: The index of sheet
	 * @return
	 * 				: String[][] containing the whole data of particular sheet index
	 */
	private String[][] getSheetData( int sheetIdx) {

		String[][] sheetData = null;
		int noOfFields;
		int noOfRecords;
		String[] record;
		if(isInitialized == false) {
			init();
		}
		 HSSFSheet hssfSheet = workBook.getSheetAt(sheetIdx);
		 Iterator rowIter = hssfSheet.rowIterator();
		HSSFRow hssfRow = (HSSFRow)rowIter.next();

		noOfRecords = hssfSheet.getLastRowNum();
		for(noOfFields=0; hssfRow.getCell(noOfFields) !=null && !hssfRow.getCell(noOfFields).toString().equals("");noOfFields++) {
			;
		}
		sheetData = new String[noOfRecords][noOfFields];
		 ArrayList<String> headers = new ArrayList<String>(noOfFields);
		for(int idx = 0;idx<noOfFields; idx++) {
			headers.add(idx, hssfRow.getCell(idx).toString().toUpperCase());
		}
		this.headers.put(workBook.getSheetName(sheetIdx), headers);
		rowIdentiferColNames.put(getSheetName(sheetIdx), headers.get(0));
		int counter =0;
		while (rowIter.hasNext()) {
			hssfRow = (HSSFRow) rowIter.next();
			record = new String[noOfFields];
			for( int idx = 0 ; idx < record.length ; idx++){
				if(hssfRow.getCell(idx) != null)
				{
					record[idx] = hssfRow.getCell(idx).toString();
				}

				else
				{
					record[idx] = "";
				}
			}
			sheetData[counter++]=record;
		}
		return sheetData;

	}



	/**
	 * This method returns the column index
	 * @param columnName
	 * 					: Name of the column
	 * @param sheetName
	 * 					: Name of the sheet
	 * @return
	 * 					: [int] The column index
	 */
	private int getColIndex( String columnName ,  String sheetName)
	{
		int index = -1;
		index = headers.get(sheetName).indexOf(columnName.toUpperCase());
		logger.debug("The index of the column = "+columnName +" in the sheet = "+sheetName +" in workbook ="+excelFileName +" is "+index);
		return index;
	}

	/**
	 * This method returns 'true' if the column name exist otherwise returns 'false'.
	 * @param columnName
	 * 				: The column Name
	 * @param sheetName
	 * 				: The sheet Name
	 * @return
	 * 				: [boolean] 'true' if the column name exist otherwise returns 'false'.
	 */
	private boolean isValidColumn( String columnName ,  String sheetName)
	{


		 int index = getColIndex(columnName,sheetName);
		return isValidColumn(index,sheetName);
	}


	/**
	 * This method returns 'true' if the column name exist otherwise returns 'false'.
	 * @param columnIdx
	 * 				: The index of column
	 * @param sheetName
	 * 				: The sheet Name
	 * @return
	 * 				: [boolean] 'true' if the column name exist otherwise returns 'false'.
	 */

	private boolean isValidColumn( int columnIdx ,  String sheetName)
	{
		logger.debug("checking validity of column name based on colId="
				+ columnIdx + ", and sheetName=" + sheetName + ", workbook="
				+ excelFileName);
		boolean isValid = false;
		if (columnIdx < headers.get(sheetName).size() && columnIdx >= 0) {
			isValid = true;
		}
		logger.debug("isColumnValid=" + isValid);
		return isValid;
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setRowIdentifierColName(java.lang.String, int)
	 */
	@Override
	public void setRowIdentifierColName(final String columnName,
			 int sheetIndex) {
		try{
		final String sheetName = getSheetName(sheetIndex);
		setRowIdentifierColName(columnName, sheetName);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setRowIdentifierColName(java.lang.String, java.lang.String)
	 */
	@Override
	public void setRowIdentifierColName( String columnName,
			 String sheetName) {
		try{
		// call the sheet load method which will check for valid sheet and load if it is valid and not already loaded
		logger.debug("Set the row identifier column name for the sheet ["+sheetName+"] by column name ["+columnName+"]");
		load(sheetName);
		if(!isValidColumn(columnName, sheetName)) {
			throw new RuntimeException("The Column "+columnName+" Doesnt exist in the sheet "+sheetName);
		}
		rowIdentiferColNames.put(sheetName, columnName);
		logger.info(String.format("The default row identifier column name for the sheet [%s] is [%s].",sheetName, columnName));
	}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setRowIdentifierColName(java.lang.String)
	 */
	@Override
	public void setRowIdentifierColName( String columnName) {
		try{
		setRowIdentifierColName(columnName, defaultSheetIndex);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setRowIdentifierColName(int)
	 */
	@Override
	public void setRowIdentifierColName( int columnNo) {
		try{
		setRowIdentifierColName(headers.get(defaultSheetIndex).get(columnNo));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getValue(java.lang.String, java.lang.String)
	 */
	@Override
	public String getValue( String rowIdentifierValue,
			 String columnName) {
		 String sheetName = getSheetName(defaultSheetIndex);
		 String string =getValue(sheetName, rowIdentifierValue, columnName);
		return string;
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getAllRowIdentifierValues()
	 */
	@Override
	public String[] getAllRowIdentifierValues() {
		return getAllRowIdentifierValues(defaultSheetIndex);
	}


	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getAllRowIdentifierValues(int)
	 */
	@Override
	public String[] getAllRowIdentifierValues( int sheetIdx) {
		 String sheetName = getSheetName(sheetIdx);
		logger.debug("Getting the all values for the  rowidentifire column = "+rowIdentiferColNames.get(sheetName) + " in the sheet name ="+sheetName +" in the workbook = "+excelFileName);
		 int colIdx = headers.get(sheetName).indexOf(
				rowIdentiferColNames.get(sheetName).toUpperCase());
		int counter =0;
		 String[] values =new String[workBookData.get(sheetIdx).length];
		for ( String string[] : workBookData.get(sheetIdx)) {
			values[counter++] = string[colIdx];
		}
		logger.info("Getting the all values for the  rowidentifire column = "+rowIdentiferColNames.get(sheetName) + " in the sheet name ="+sheetName +" in the workbook = "+excelFileName);
		return values;
	}


	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getAllRowIdentifierValues(java.lang.String)
	 */
	@Override
	public String[] getAllRowIdentifierValues( String sheetName) {
		logger.debug("Getting the all values for the  rowidentifire column = "+rowIdentiferColNames.get(sheetName) + " in the sheet name ="+sheetName +" in the workbook = "+excelFileName);
		 int sheetIdx = getSheetIndex(sheetName);
		logger.info("Getting the all values for the  rowidentifire column = "+rowIdentiferColNames.get(sheetName) + " in the sheet name ="+sheetName +" in the workbook = "+excelFileName);
		return getAllRowIdentifierValues(sheetIdx);
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setDefaultSheetName(java.lang.String)
	 */
	@Override
	public void setDefaultSheetName( String sheetName) {
		try{
		logger.debug("Setting the default sheet name by passing sheet name = "+sheetName +" for the WorkBook = " +excelFileName);
		 int index = getSheetIndex(sheetName);
		setDefaultSheetIndex(index);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setDefaultSheetName(int)
	 */
	@Override
	public void setDefaultSheetName( int sheetIndex) {
		try{
		setDefaultSheetIndex(sheetIndex);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * This method is used to set value to object variable 'defaultSheetIndex'
	 * @param index
	 * 				:The sheet index
	 */
	private void setDefaultSheetIndex( int index) {
		try{
		defaultSheetIndex = index;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#getValue(java.lang.String)
	 */
	@Override
	public String getValue( String rowIdentifierValue) {
		 String sheetName = getSheetName(defaultSheetIndex);
		load(sheetName);
		String columnName = deafaultRetColumnName.get(sheetName);

		if(StringUtils.isEmpty(columnName)) {
			columnName = headers.get(sheetName).get(1);
			logger.debug("no default column name is provided. reading value from ["+columnName+"].");
		}
		logger.debug("retreaving value from ["+columnName+"]");
		return getValue(rowIdentifierValue,columnName);
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setDefaultRetColumnName(java.lang.String)
	 */
	@Override
	public void setDefaultRetColumnName( String columnName) {
		try{
		deafaultRetColumnName.put(getSheetName(defaultSheetIndex),columnName);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.pyramidconsulting.reader.ExcelReader#setDefaultRetColumnName(int)
	 */
	@Override
	public void setDefaultRetColumnName( int columnNo) {
		try{
		setDefaultRetColumnName(headers.get(defaultSheetIndex).get(columnNo));
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}


}
