package com.pyramidconsulting.reader;


public interface ExcelReader {

	/**
	 * This method returns the cell value from the excel sheet .
	 * @param sheetName		
	 * 				: Name of the Sheet.
	 * @param rowIdentifierColNo
	 * 				: Row Identifier Column Number in which column the value will be searched. 
	 * @param rowIdentifierValue
	 * 				: Row Identifier Column Value indicates the criteria string against which value is needed.   
	 * @param columnName
	 * 				: From which column the value is needed.
	 * @return		
	 * 				: The cell value of string type
	 */
	String getValue(String sheetName, int rowIdentifierColNo,
			String rowIdentifierValue, String columnName);

	/**
	 * This method returns the cell value from the excel sheet, 
	 * here the first column of the sheet is treated as the row identifier column by default.
	 * 
	 * @param sheetName		
	 * 				: Name of the Sheet.
	 * @param rowIdentifierValue
	 * 				: Row Identifier Column Value indicates the criteria string against which value is needed. 
	 * @param columnName
	 * 				: From which column the value is needed.
	 * @return		: The cell value of string type
	 */
	String getValue(String sheetName, String rowIdentifierValue,
			String columnName);


	/**
	 * This method returns the cell value from the excel sheet, 
	 * here the first column of the sheet is treated as the default  row identifier column.
	 * here the first sheet of the workbook is treated as the default sheet .
	 * @param rowIdentifierValue
	 * 				: Row Identifier Column Value indicates the criteria string against which value is needed. 
	 * @param columnName
	 * 				: From which column the value is needed.
	 * @return		: The cell value of string type
	 */
	String getValue(String rowIdentifierValue, String columnName);

	/**
	 * This method returns the cell value from the excel sheet, 
	 * here the first column of the sheet is treated as the default  row identifier column.
	 * here the first sheet of the workbook is treated as the default sheet .
	 * @param rowIdentifierValue
	 * 				: Row Identifier Column Value indicates the criteria string against which value is needed. 
	 * @return		: The cell value of string type
	 */
	String getValue(String rowIdentifierValue);

	/**
	 * This method is used to set the default retrieval column on the basis of column name 
	 * @param columnName
	 * 				: The Column Name
	 */
	void setDefaultRetColumnName(String columnName);
	
	/**
	 * This method is used to set the default retrieval column on the basis of column number 
	 * @param columnNo
	 * 				: The Column index number
	 */
	void setDefaultRetColumnName(int columnNo);
	
	/**
	 * This method is used to set the default row identifier column on the basis of column name  
	 * @param columnName
	 * 				: The Column Name
	 */
	void setRowIdentifierColName(String columnName);

	/**
	 * This method is used to set the default row identifier column on the basis of column number
	 * @param columnNo
	 * 				: The Column index number
	 */
	void setRowIdentifierColName(int columnNo);
	
	/**
	 * This method is used to set the default row identifier column on the basis of column name in a particular sheet index
	 * @param columnNo
	 * 				: The Column index number
	 * @param sheetIndex
	 * 				: The index of sheet
	 */
	
	void setRowIdentifierColName( String columnName,
			 int sheetIndex) ;
	/**
	 * This method is used to set the default row identifier column on the basis of column name in a particular sheet name
	 * @param columnName
	 * 				: The Column name
	 * @param sheetIndex
	 * 				: The index of sheet
	 */
	
	
	void setRowIdentifierColName( String columnName,
			 String sheetName);
	
	
	/**
	 * This method returns all the row identifier values of default sheet  
	 * @return		: String[] of row identifier values
	 */
	String[] getAllRowIdentifierValues();

	/**
	 * This method returns all the row identifier values of the particular sheet
	 * @param sheetName 
	 * 				: Sheet Name
	 * @return		: String[] of row identifier values
	 */
	String[] getAllRowIdentifierValues(String sheetName);
	/**
	 * This method returns the row identifier values on the basis of sheet index
	 * @param sheetIdx
	 * 				:The index of sheet
	 * @return
	 * 				:string[]  the row identifier values .
	 */
	String[] getAllRowIdentifierValues( int sheetIdx);
	/**
	 * This method is to set the default sheet name on the basis of sheet name
	 * @param sheetName
	 * 				: Sheet Name
	 */
	void setDefaultSheetName(String sheetName);

	/**
	 * This method is to set the default sheet name on the basis of sheet index
	 * @param sheetIndex
	 * 				: Index of The Sheet
	 */
	void setDefaultSheetName(int sheetIndex);

}
