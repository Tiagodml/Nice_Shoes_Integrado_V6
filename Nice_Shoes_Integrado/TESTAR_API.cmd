@echo off
cd /d "%~dp0"
where mvn >nul 2>&1 || (echo Instale o Maven e configure o PATH. & pause & exit /b 1)
call mvn test
pause
