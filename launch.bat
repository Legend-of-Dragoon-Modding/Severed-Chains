@echo off

set compiled="@false@";

if %compiled% NEQ "true" (
  echo You are trying to run the Severed Chains source code. Please see our installation instructions at https://legendofdragoon.org/projects/severed-chains/
  pause
  exit 1
)

echo.%CD% | findstr /C:";" 1>nul

if not errorlevel 1 (
  echo Java cannot launch with a semicolon ^(^;^) in the path. Please rename the directory or move Severed Chains to a different directory.
  echo Current directory: "%CD%"
  pause
  exit 1
)

if exist ".\jdk25\bin\java.exe" (
  goto LAUNCH
)

: DOWNLOAD_JAVA
if exist ".\jdk25\" (
  @rmdir /S /Q ".\jdk25"
)

echo Downloading java...
powershell -Command "$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri 'https://corretto.aws/downloads/resources/25.0.0.36.2/amazon-corretto-25.0.0.36.2-windows-x64-jdk.zip' -OutFile '.\jdk.zip'" || goto DOWNLOAD_FAILED

echo Extracting java...
powershell Expand-Archive ".\jdk.zip" -DestinationPath "." || goto DOWNLOAD_FAILED

echo Cleaning up...
move ".\jdk25.0.0_36" ".\jdk25"
del ".\jdk.zip"

: LAUNCH
".\jdk25\bin\java" -Djoml.fastmath -Djoml.sinLookup -Djoml.useMathFma -cp "lod-game-@version@.jar;@libs@" legend.game.MainWindows -Xmx2G -ea || pause
exit 0

: DOWNLOAD_FAILED
echo Java download failed
pause
exit 2
