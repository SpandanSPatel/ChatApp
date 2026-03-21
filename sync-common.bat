@echo off
echo ===============================
echo Syncing chat-common to projects
echo ===============================

REM Define source
set SOURCE=chat-common\src

REM Define destinations
set CLIENT_DEST=chat-client\src\main\java
set SERVER_DEST=chat-server\src\main\java

echo.
echo Copying to CLIENT...
xcopy %SOURCE%\model %CLIENT_DEST%\model /E /I /Y
xcopy %SOURCE%\util %CLIENT_DEST%\util /E /I /Y

echo.
echo Copying to SERVER...
xcopy %SOURCE%\model %SERVER_DEST%\model /E /I /Y
xcopy %SOURCE%\util %SERVER_DEST%\util /E /I /Y

echo.
echo ✅ Sync complete!
pause