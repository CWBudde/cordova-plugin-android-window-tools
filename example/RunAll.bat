@echo off
call .\platforms\android\gradlew --stop
call cordova build --verbose
python DeployAll.py de.pcjv.androidwindowtools.test .\platforms\android\app\build\outputs\apk\debug\app-debug.apk
