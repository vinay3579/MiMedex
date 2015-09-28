Dim fso, f1,ts,WshShell, fso1

Set WshShell = CreateObject("Wscript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")
ApplicationData = WshShell.ExpandEnvironmentStrings("%APPDATA%")

Dim strFolderToSearch, objFSO, objRootFolder, objFolder, colSubfolders, strOutput

strFolderToSearch = ApplicationData & "\Mozilla\Firefox\Profiles\"

Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objRootFolder = objFSO.GetFolder(strFolderToSearch)
Set colSubfolders = objRootFolder.SubFolders
counter = 0
For Each objFolder in colSubfolders
     strOutput = objFolder.name
	Set objFSO = CreateObject("Scripting.FileSystemObject")
	Set objRootFolder = objFSO.GetFolder(strFolderToSearch & strOutput)
	Set colSubfolders1 = objRootFolder.Files
	
	For Each objFolder1 in colSubfolders1
		 strOutput1 = objFolder1.name
		If strOutput1 = "prefs.js" Then
			counter = 1
			Exit for
		End If
	Next
	If counter  = 1 Then
		fso.CreateTextFile (strFolderToSearch & strOutput & "\user.js")		
		Const ForAppending = 8
		Set objTextFile = fso.OpenTextFile (strFolderToSearch & strOutput & "\user.js", ForAppending, True)
		objTextFile.WriteLine("user_pref(" & chr(34) & "dom.disable_open_during_load" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "app.update.enabled" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "extensions.update.enabled" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "browser.shell.checkDefaultBrowser" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "browser.tabs.autoHide" & chr(34) & ", false);")
		objTextFile.Close		
		Exit for
	End If
Next
