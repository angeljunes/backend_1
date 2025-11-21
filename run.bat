@echo off
echo ========================================
echo    RCAS Backend - Sistema de Alertas
echo ========================================
echo.

cd /d "C:\Users\Edgar\OneDrive\Documentos\NetBeansProjects\backend"

echo [1/3] Limpiando proyecto anterior...
mvn clean -q

echo [2/3] Compilando proyecto...
mvn compile -q

echo [3/3] Iniciando servidor Spring Boot...
echo.
echo ✅ Servidor iniciandose en: http://localhost:8081/api
echo ✅ Health check: http://localhost:8081/api/test/health
echo ✅ Paises: http://localhost:8081/api/location/countries
echo.
echo Presiona Ctrl+C para detener el servidor
echo ========================================

mvn spring-boot:run
