@echo off
setlocal

if exist data\trackfit.ser (
  del /f /q data\trackfit.ser
  echo Banco antigo removido. Rode run.bat para recriar os dados de exemplo.
) else (
  echo Nenhum banco antigo encontrado.
)
