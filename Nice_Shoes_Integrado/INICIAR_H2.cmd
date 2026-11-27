@echo off
cd /d "%~dp0"
where java >nul 2>&1 || (echo Instale o JDK 17 ou superior. & pause & exit /b 1)
where mvn >nul 2>&1 || (echo Instale o Maven e configure o PATH. & pause & exit /b 1)
echo Abra http://localhost:8080 quando a API terminar de iniciar.
call mvn spring-boot:run -Dspring-boot.run.profiles=local,demo
pause
