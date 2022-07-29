@echo off
:: BatchGotAdmin
:-------------------------------------
REM  --> Check for permissions
IF "%PROCESSOR_ARCHITECTURE%" EQU "amd64" (
    >nul 2>&1 "%SYSTEMROOT%\SysWOW64\cacls.exe" "%SYSTEMROOT%\SysWOW64\config\system"
) ELSE (
    >nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
)

REM --> If error flag set, we do not have admin.
if '%errorlevel%' NEQ '0' (
    echo Requesting administrative privileges...
    goto UACPrompt
) else ( goto gotAdmin )

:UACPrompt
    echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    set params= %*
    echo UAC.ShellExecute "cmd.exe", "/c %~s0 %params:"=""%", "", "runas", 1 >> "%temp%\getadmin.vbs"

    "%temp%\getadmin.vbs"
    del "%temp%\getadmin.vbs"
    exit /B

:gotAdmin
    pushd "%CD%"
    CD /D "%~dp0"
::--------------------------------------

:: Parameters
SET JAVA=%1
SET JAR=%2
SET ICON=%3

:: Set jar as application for .gmst file
ASSOC .gmst=gmstfile
FTYPE gmstfile=%JAVA% "-Dcom.sun.net.ssl.checkRevocation=false" -jar %JAR% "%%1" "%%*"
REG ADD "HKEY_CLASSES_ROOT\gmstfile\DefaultIcon" /t REG_SZ /d "%ICON%" /f

:: Set jar as application for open gamalaucher protocol
reg add "HKEY_CLASSES_ROOT\gmstlauncher" /t REG_SZ /d "Run gamasenseit client" /f
reg add "HKEY_CLASSES_ROOT\gmstlauncher" /v "URL Protocol" /t REG_SZ /d "" /f
reg add "HKEY_CLASSES_ROOT\gmstlauncher\Shell" /f
reg add "HKEY_CLASSES_ROOT\gmstlauncher\Shell\Open" /f
reg add "HKEY_CLASSES_ROOT\gmstlauncher\Shell\Open\Command" /t REG_EXPAND_SZ /d "%JAVA:"=\"% \"-Dcom.sun.net.ssl.checkRevocation=false\" -jar %JAR:"=\"% \"%%1\" \"%%*\"" /f
REG ADD "HKEY_CLASSES_ROOT\gmstlauncher\DefaultIcon" /t REG_SZ /d "%ICON%" /f
REG ADD "HKEY_CLASSES_ROOT\gmstlauncher\Open\Command" /t REG_SZ /d "URL:gmstlauncher Protocol" /f
REG ADD "HKEY_CLASSES_ROOT\gmstlauncher\Open\Command" /v "URL Protocol" /t REG_SZ /f
