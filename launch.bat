@echo off

if exist ".\jdk\bin\java.exe" (
  goto LAUNCH
)

: DOWNLOAD_JAVA
if exist ".\jdk\" (
  @rmdir /S /Q ".\jdk"
)

echo Downloading java...
powershell -Command "$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri 'https://corretto.aws/downloads/latest/amazon-corretto-17-x64-windows-jdk.zip' -OutFile '.\jdk.zip'"

echo Extracting java...
powershell Expand-Archive ".\jdk.zip" -DestinationPath "."

echo Cleaning up...
move ".\jdk17.0.7_7" ".\jdk"
del ".\jdk.zip"

: LAUNCH
".\jdk\bin\java" -cp "lod-game-@version@.jar;libs/*" legend.game.MainWindows -ea -Djoml.fastmath -Djoml.sinLookup -Djoml.useMathFma || pause
