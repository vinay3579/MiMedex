
@echo OFF

setlocal ENABLEEXTENSIONS
set KEY_NAME="HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\chrome.exe"
set VALUE_NAME=Path

FOR /F "usebackq tokens=1-3" %%A IN (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) DO (
    set ValueName=%%A
    set PathValue=%%C
)

if defined ValueName (
    @echo PathValue = %PathValue%
) else (
    @echo %KEY_NAME%\%VALUE_NAME% not found.
)

REM :  Read version of Chrome installed

set KEY_NAME="HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Uninstall\Google Chrome"
set VALUE_NAME=Version

FOR /F "usebackq tokens=1-3" %%A IN (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) DO (
    set ValueName=%%A
    set VersionValue=%%C
)

if defined ValueName (
    @echo VersionValue = %VersionValue%
) else (
    @echo %KEY_NAME%\%VALUE_NAME% not found.
)

if not exist "%PathValue%"\"%VersionValue%"\"Extensions\old_external_extensions.json" goto Error

REM : Got to the Chrome path
cd "%PathValue%"\"%VersionValue%"\"Extensions"

REM : Delete old_external_extensions.json
del "%PathValue%"\"%VersionValue%"\"Extensions\external_extensions.json" /F
del "%PathValue%"\"%VersionValue%"\"Extensions\extension_2_0.crx" /F

REM : Rename old file for use
ren "old_external_extensions.json" "external_extensions.json" 



REM : Enable Mixed content
echo Internet
set CONTENT=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3
reg ADD "%CONTENT%" /v 1609 /t REG_DWORD /d 1 /f
echo Intranet
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 1 /f
echo Restricted Sites
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 1 /f
echo Trusted Sites
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 1 /f

REM : Enable Protected mode 
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v 2500 /t REG_DWORD /d 0 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v 2500 /t REG_DWORD /d 0 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v 2500 /t REG_DWORD /d 0 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v 2500 /t REG_DWORD /d 0 /f


REM : Enable Cookies
reg ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v PrivacyAdvanced /t REG_DWORD /d 0 /f

goto :eof

:Error
echo The File "%PathValue%"\"%VersionValue%"\"Extensions\old_external_extensions.json" does not exist!
goto :eof

