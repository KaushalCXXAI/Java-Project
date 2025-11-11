@echo off
echo ========================================
echo Library Management System - Desktop App
echo ========================================
echo.

echo Building project...
call mvn clean package -DskipTests

if errorlevel 1 (
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Starting Desktop Application...
echo.

cd target
java -cp "library-management-1.0.0.jar;lib/*" com.library.swing.LibraryApp

pause
