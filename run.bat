@echo off
setlocal

if not exist bin\br\com\trackfit\app\Main.class (
  call compile.bat
  if errorlevel 1 exit /b 1
)

java -cp bin br.com.trackfit.app.Main
