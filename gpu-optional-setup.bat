@echo off
setlocal EnableDelayedExpansion

echo.
echo ===========================================
echo  Severed Chains - GPU Setup Helper
echo ===========================================
echo.
echo This tool configures Windows to use your preferred GPU
echo for Severed Chains. You only need to run this once.
echo.
echo Current GPU preference settings are stored in Windows
echo and will persist across game updates.
echo.

rem get the java path used by the game
set "JAVA_PATH="
if exist ".\jdk25\bin\java.exe" (
  set "JAVA_PATH=%CD%\jdk25\bin\java.exe"
) else if exist ".\jdk25\bin\javaw.exe" (
  set "JAVA_PATH=%CD%\jdk25\bin\javaw.exe"
)

if "%JAVA_PATH%"=="" (
  echo ERROR: Could not find Java installation.
  echo Please run the game once first to download Java,
  echo then run this setup again.
  echo.
  pause
  exit /b 1
)

echo Detected Java: %JAVA_PATH%
echo.

:MENU
echo Select GPU preference:
echo.
echo   1 = High Performance ^(Discrete GPU - NVIDIA/AMD^)
echo       Use this for best graphics performance
echo.
echo   2 = Power Saving ^(Integrated GPU - Intel/AMD^)
echo       Use this for better battery life on laptops
echo.
echo   3 = Remove Setting ^(Let Windows decide^)
echo       Removes any custom GPU preference
echo.
echo   4 = Cancel
echo.
set /p "CHOICE=Enter choice (1-4): "

if "%CHOICE%"=="1" goto SET_DISCRETE
if "%CHOICE%"=="2" goto SET_INTEGRATED
if "%CHOICE%"=="3" goto REMOVE_SETTING
if "%CHOICE%"=="4" goto CANCEL
echo Invalid choice. Please enter 1, 2, 3, or 4.
echo.
goto MENU

:SET_DISCRETE
echo.
echo Setting GPU preference to: High Performance (Discrete GPU)
echo.
reg add "HKCU\Software\Microsoft\DirectX\UserGpuPreferences" /v "%JAVA_PATH%" /t REG_SZ /d "GpuPreference=2;" /f >nul 2>&1
if errorlevel 1 (
  echo ERROR: Failed to set GPU preference.
  echo.
  pause
  exit /b 1
)
echo SUCCESS! Windows will now use your discrete GPU for Severed Chains.
echo.
echo Note: If you have multiple discrete GPUs, Windows will choose
echo the most powerful one.
goto DONE

:SET_INTEGRATED
echo.
echo Setting GPU preference to: Power Saving (Integrated GPU)
echo.
reg add "HKCU\Software\Microsoft\DirectX\UserGpuPreferences" /v "%JAVA_PATH%" /t REG_SZ /d "GpuPreference=1;" /f >nul 2>&1
if errorlevel 1 (
  echo ERROR: Failed to set GPU preference.
  echo.
  pause
  exit /b 1
)
echo SUCCESS! Windows will now use your integrated GPU for Severed Chains.
echo.
goto DONE

:REMOVE_SETTING
echo.
echo Removing custom GPU preference...
echo.
reg delete "HKCU\Software\Microsoft\DirectX\UserGpuPreferences" /v "%JAVA_PATH%" /f >nul 2>&1
echo Done. Windows will now decide which GPU to use automatically.
goto DONE

:CANCEL
echo.
echo Cancelled. No changes were made.
echo.
pause
exit /b 0

:DONE
echo.
echo ----------------------------------------
echo You can verify this setting in:
echo   Windows Settings ^> Display ^> Graphics
echo.
echo Look for: %JAVA_PATH%
echo ----------------------------------------
echo.
pause
exit /b 0
