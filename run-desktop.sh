#!/bin/bash

echo "========================================"
echo "Library Management System - Desktop App"
echo "========================================"
echo ""

echo "Building project..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed! Please check the errors above."
    exit 1
fi

echo ""
echo "Starting Desktop Application..."
echo ""

cd target
java -cp "library-management-1.0.0.jar:lib/*" com.library.swing.LibraryApp
