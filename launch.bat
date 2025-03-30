@echo off

set compiled="@false@";

if %compiled% NEQ "true" (
  echo You are trying to run the Severed Chains source code. Please see our installation instructions at https://legendofdragoon.org/projects/severed-chains/
  pause
  exit 1
)

echo.%CD% | findstr /C:";" 1>nul

if not errorlevel 1 (
  echo "Java cannot launch with a semicolon (;) in the path. Please rename the directory or move Severed Chains to a different directory."
  echo Current directory: %CD%
  pause
  exit 1
)

if exist ".\jdk21\bin\java.exe" (
  goto LAUNCH
)

: DOWNLOAD_JAVA
if exist ".\jdk21\" (
  @rmdir /S /Q ".\jdk21"
)

echo Downloading java...
powershell -Command "$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri 'https://corretto.aws/downloads/resources/21.0.6.7.1/amazon-corretto-21.0.6.7.1-windows-x64-jdk.zip' -OutFile '.\jdk.zip'" || goto DOWNLOAD_FAILED

echo Extracting java...
powershell Expand-Archive ".\jdk.zip" -DestinationPath "." || goto DOWNLOAD_FAILED

echo Cleaning up...
move ".\jdk21.0.6_7" ".\jdk21"
del ".\jdk.zip"

: LAUNCH
".\jdk21\bin\java" -Djoml.fastmath -Djoml.sinLookup -Djoml.useMathFma -cp "lod-game-@version@.jar;@libs@" legend.game.MainWindows -Xmx2G -ea || pause
exit 0

: DOWNLOAD_FAILED
echo Java download failed
exit 2
