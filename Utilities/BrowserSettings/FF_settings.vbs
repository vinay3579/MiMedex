Dim fso, f1,ts,WshShell, fso1

Set WshShell = CreateObject("Wscript.Shell")
WshShell.Run "Reverse_IE_Settings.bat"
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
		objTextFile.WriteLine("user_pref(" & chr(34) & "dom.disable_open_during_load" & chr(34) & ", false);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "app.update.enabled" & chr(34) & ", false);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "extensions.update.enabled" & chr(34) & ", false);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "browser.shell.checkDefaultBrowser" & chr(34) & ", false);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "browser.tabs.autoHide" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "places.history.enabled" & chr(34) & ", false);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "services.sync.prefs.sync.privacy.clearOnShutdown.history" & chr(34) & ", true);")
 		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.cache" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.cookies" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.downloads" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.sessions" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.passwords" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.offlineApps" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.history" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.clearOnShutdown.formdata" & chr(34) & ", true);")
		objTextFile.WriteLine("user_pref(" & chr(34) & "privacy.sanitize.sanitizeOnShutDown" & chr(34) & ", true);")
		objTextFile.Close		
		Exit for
	End If
Next


