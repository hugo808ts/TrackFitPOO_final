@echo off
setlocal

if not exist bin mkdir bin
set "SOURCES_FILE=%TEMP%\trackfit-sources.txt"
dir /s /b src\*.java > "%SOURCES_FILE%"
javac -encoding UTF-8 -d bin @"%SOURCES_FILE%"
del "%SOURCES_FILE%" >nul 2>&1

if errorlevel 1 (
  echo.
  echo Erro ao compilar. Verifique se o JDK esta instalado e se o javac esta no PATH.
  exit /b 1
)

echo.
echo Compilacao concluida. Use run.bat para abrir o sistema.
