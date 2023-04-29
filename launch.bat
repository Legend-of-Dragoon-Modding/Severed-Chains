@echo off

:: Is Java on the path?
where java 2>NUL
if errorlevel 1 goto NO_JAVA

:: Check Java version
setlocal EnableDelayedExpansion
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do set version=%%i

for /f "tokens=1,2 delims=." %%a in (%version%) do (
   set major=%%a
   set minor=%%b
)

if %major% equ 1 (if %minor% lss 17 goto OLD_JAVA) else (if %major% lss 17 goto OLD_JAVA)

java -cp "lod-game-1.0-SNAPSHOT.jar;libs/*" legend.game.MainWindows -ea || pause

goto EOF

: NO_JAVA
echo Severed Chains requires Java 17, but Java is not installed.
echo Please download and install it using the link below.
echo https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

pause
goto EOF

: OLD_JAVA
echo Severed Chains requires Java 17, but Java %minor% is installed.
echo Please uninstall your current version of Java and download Java 17.
echo https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

pause
goto EOF
