@echo off

if exist ".\jdk\bin\java.exe" (
  goto LAUNCH
)

: DOWNLOAD_JAVA
if exist ".\jdk\" (
  @rmdir /S /Q ".\jdk"
)

echo Downloading java...
powershell -Command "$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri 'https://corretto.aws/downloads/resources/17.0.7.7.1/amazon-corretto-17.0.7.7.1-windows-x64-jdk.zip' -OutFile '.\jdk.zip'"

echo Extracting java...
powershell Expand-Archive ".\jdk.zip" -DestinationPath "."

echo Cleaning up...
move ".\jdk17.0.7_7" ".\jdk"
del ".\jdk.zip"

: LAUNCH
".\jdk\bin\java" -cp "lod-game-@version@.jar;libs/*" legend.game.MainWindows -Xmx2G -ea -Djoml.fastmath -Djoml.sinLookup -Djoml.useMathFma || pause
