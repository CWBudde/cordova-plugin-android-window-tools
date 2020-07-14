@echo off
call .\platforms\android\gradlew --stop
call cordova build --verbose
if %ERRORLEVEL% GEQ 1 EXIT /B 1
python DeployAll.py de.pcjv.androidwindowtools.test .\platforms\android\app\build\outputs\apk\debug\app-debug.apk
