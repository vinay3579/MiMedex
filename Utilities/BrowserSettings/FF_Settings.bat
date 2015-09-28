REM : Call FF_Settings.vbs

FF_Settings.vbs

REM : Disable Mixed content
echo Display Mixed Content: Enable
echo Internet
set CONTENT=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3
reg ADD "%CONTENT%" /v 1609 /t REG_DWORD /d 0 /f
echo Intranet
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 0 /f
echo Restricted Sites
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 0 /f
echo Trusted Sites
set INTRA=HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2
reg ADD "%INTRA%" /v 1609 /t REG_DWORD /d 0 /f

REM : Disable Protected mode 
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v 2500 /t REG_DWORD /d 3 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v 2500 /t REG_DWORD /d 3 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v 2500 /t REG_DWORD /d 3 /f
reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v 2500 /t REG_DWORD /d 3 /f

