@echo off
chcp 65001 > nul
echo ==================================================
echo   ResumeForge - PDF Uretici (Temiz Derleme)
echo ==================================================

REM Eski derlenmis class dosyalarini temizle
if exist "target\classes" (
    rd /s /q "target\classes"
)
mkdir "target\classes"

if not exist "output" mkdir "output"

echo [1/3] Main.java dosya kayit zamani kontrol ediliyor...
for %%F in ("src\main\java\com\resume\Main.java") do (
    echo       Dosya son kaydedilme tarihi: %%~tF
)

echo [2/3] Java dosyalari sifirdan derleniyor...
javac -encoding UTF-8 -cp "lib\openpdf-2.0.3.jar" -d target\classes src\main\java\com\resume\model\*.java src\main\java\com\resume\service\*.java src\main\java\com\resume\Main.java

if errorlevel 1 (
    echo.
    echo [HATA] Derleme basarisiz oldu! Kodda yazim hatasi olabilir.
    pause
    exit /b 1
)

echo [3/3] Main calistiriliyor ve PDF uretiliyor...
java -cp "target\classes;lib\openpdf-2.0.3.jar;src\main\resources" com.resume.Main

if errorlevel 1 (
    echo.
    echo [UYARI] PDF olusturulamadi!
    echo Eger 'output\ozgecmis.pdf' aciksa lutfen kapatin ve tekrar deneyin.
    pause
    exit /b 1
)

echo.
echo ==================================================
echo [BASARILI] Yeni PDF hazir: output\ozgecmis.pdf
echo ==================================================
pause
