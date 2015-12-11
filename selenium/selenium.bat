for %%i in (*.jar) do set SELENIUM=%%i
java -Dwebdriver.ie.driver=IEDriverServer.exe -jar %SELENIUM%