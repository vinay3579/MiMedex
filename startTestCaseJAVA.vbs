'' Path of Test Automation Folder
strTestAutomationPath  = "D:\JavaSeleniumR&D\JavaSeAPdistributedExecutionWorkingWithoutIE\dist\"

'' Initializing test suite path
strTestSuite = strTestAutomationPath & "TestSuite\"
'' Initializing Run Manager executable path
strRunManagerPath = strTestAutomationPath 
''&  "RunManagerSolution\RunManagerLibraries\bin\Release"
'' Initializing HUB path
strHubBatPath = strTestAutomationPath & "Hub.bat"
'' Initializing Config file path
strConfigFilePath = strTestAutomationPath &  "Datasheets\Config.xls"

'' Query to reterieve Config values from the config sheet
strSQLQueryGetTestSuite = "Select * from [Config$]"

'' Getting all values from config file
getDataFromExcel strConfigFilePath,strSQLQueryGetTestSuite,arrHeaders,arrTS

'' Extracting value of TestSuiteFileName
For i = 0 to UBound(arrTS,2)
	If arrTS(0,i) = "TestSuiteFileName" Then
		strTSFileName = arrTS(1,i)
		Exit For
	End If
Next

''Initializing value of test suite file name
strTestSuitePath = strTestSuite & strTSFileName & ".xls"
'' Query to reterieve Machine IPs from the Config file
strSQlQuerytoGetMachines = "Select * from [MachineIPs$]"

'' Getting Machine IPs from the Config file
getDataFromExcel strConfigFilePath,strSQlQuerytoGetMachines,arrHeaders,arrNodeIP

'' Creating arrary for the Machine IPs which are fetched from TestSuite file
arrIP = Array()
For i=0 to Ubound(arrNodeIP, 2) 'rows
	For j=0 to UBound(arrHeaders)
		ReDim Preserve arrIP(i)
		arrIP(i) = arrNodeIP(j, i)
	Next
Next

For i = 1 to UBound(arrIP)
	startStopNode "stop",arrIP(i)
Next

'' Starting Hub
startHub(strHubBatPath)

'' Starting Node on the IPs fetched from TestSuite file
For i = 1 to UBound(arrIP)
	startStopNode "start",arrIP(i)
Next

Set Wsh2 = CreateObject("wscript.Shell")

WScript.Sleep 10000

'' Getting sheet names from the TestSuite file.
getSheetNames strTestSuitePath,arrSheetName

'' Looping through the sheet names to execute the test cases
For k = 1 to UBound(arrSheetName)

	If Instr(1,arrSheetName(k),"MachineIPs",1)  = 0  AND Instr(1,arrSheetName(k),"Database",1)  = 0 Then
		'' Creating query to fetch the test cases from the sheet where Run is Y
		strSQlQuery = "Select TestCaseId,TestCaseName,Iteration from [" & arrSheetName(k) &"] where Run='Y'"
		'WScript.Sleep 5000
		''Fetching data from the test suite file where Run is set as Y
		
		getDataFromExcel strTestSuitePath,strSQLQuery,arrHeaders,arrRS

		'' Checking if record set fetched is empty
		If Not isEmpty(arrRS) Then
			'' Creating array of test case name
        	arrTestCaseName = Array()
			For i=0 to Ubound(arrRS,2) 'rows
'					print arrRS(0,i) & " " &arrRS(1,i) & " " & arrRS(2,i) 
					'msgbox arrIP(0)
					Wsh2.run "LauncherHub.bat" & " " & arrRS(0,i) & " " & arrRS(1,i) & " " & arrRS(2,i) & " " & arrSheetName(k) & " "& strTSFileName &" "& arrIP(0) & ":4444"
					MsgBox(arrRS(0,i) & " " & arrRS(1,i) & " " & arrRS(2,i) & " " & arrSheetName(k) & " "& strTSFileName &" "& arrIP(0) & ":4444")
					 '':4444" & " " & chr(34) & Replace(arrSheetName(k),"$","") & chr(34) & " " & chr(34) & arrRS(0,i)  & chr(34) & " " & chr(34) & arrRS(1,i) & chr(34) & " " & arrRS(2,i)
					'MsgBox(strRunManagerPath & "LauncherHub.bat")
					'MsgBox(arrIP)
					WScript.Sleep 10000
			Next		
		End If
		arrRS = Empty
	End If
Next
Set Wsh2 = Nothing

''-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Function getSheetNames(strExcelPath,arrSheetName)
	'' This code it to get the sheet names from the test suite files.
	set ado = CreateObject("ADODB.Connection")
	set rec = CreateObject("ADODB.RecordSet")
		
	ado.ConnectionString = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" & strExcelPath &";Extended Properties=Excel 12.0"
	ado.Open
	
	set ExcelTables = ado.OpenSchema(20)
	
	ExcelTables.MoveFirst
	
	arrSheetName = Array()
	
	For i = 0 to ExcelTables.Fields.Count
		ReDim Preserve arrSheetName(i)
		arrSheetName(i) = ExcelTables.Fields("table_name")	
		ExcelTables.MoveNext
	
		If ExcelTables.EOF Then
			Exit For
		End If
	Next
	
	Set ExcelTables = Nothing
	Set rec = Nothing
	Set ado = Nothing
End Function

''----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
'' This code is to get the test cases names where run is set to "Yes"        
Function getDataFromExcel(strExcelPath,strSQLQuery,arrHeaders,arrRS)
	'on error resume next
	Set objConn = CreateObject("ADODB.connection")
	Set objRS = CreateObject("ADODB.recordset")
	'msgbox strExcelPath
	'objConn.ConnectionString = "Driver={Microsoft Excel Driver (*.xls)};DBQ=" & strExcelPath & "; ReadOnly=True;FirstRowHasNames=1MaxScanRows=0"
	objConn.ConnectionString = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" & strExcelPath &";Extended Properties=Excel 12.0"
	objConn.Mode = 1 ''1 for Read and 3 for read/write
	objConn.CommandTimeout=360	
	objConn.Open()
	
	If objConn.State <> 1 Then
		err.clear()
	End If
		
	''@Query DB
	objRs.Open strSQlQuery, objConn, 2, 2, 1
	
	''@Get headers
	arrHeaders = Array(1) 
	For i=0 to objRS.Fields.Count-1
		ReDim Preserve arrHeaders(i)
		arrHeaders(i) = objRS.Fields(i).Name
	Next

	''@Get records if present
	If objRS.EOF = true Then
		''@if record count = 0
'		executeQuery = 3
	Else
		''@Get data into a 2D array
		objRS.MoveFirst			
		arrRS = objRS.GetRows()
	End If
	
	''@Close recordset and DB connection
	objRs.Close()
	objConn.Close()
	'err.clear()
	
	Set objRS = Nothing
	Set objConn = Nothing
	
End Function

'-----------------------------------------------------------------------------------------------------------------------------------------
''This code is used to start or stop the hub and node
Function startStopNode(strtype,IP)
	
	Set ie = CreateObject("internetexplorer.application")
	If Lcase(strType) = "start" Then
		
    	ie.Navigate "http://" & IP &"/seleniumnode/webservice1.asmx/startnode?port=4444"
	elseif lcase(strType) = "stop" Then
		ie.Navigate "http://" & IP &"/seleniumnode/webservice1.asmx/stopnode?port=4444"
	End If
	'sleep 2
	ie.Visible=False
	Set ie = Nothing
End Function


Function startHub(strHubBatPath)
	Set Wsh1 = CreateObject("WScript.Shell")
	Wsh1.Run strHubBatPath
	Set Wsh1 = Nothing
End Function

