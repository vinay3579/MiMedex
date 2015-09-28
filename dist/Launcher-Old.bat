rem D:\Softwares\Java\jdk1.7.0_01\bin\java -cp JavaSeAP.jar;./ com.pyramidconsulting.Launcher -env Production -appName 7Up
chdir %TEMP%\Solution
"%JAVA_HOME%"\bin\java -Dwebdriver.chrome.driver=./chromedriver.exe -cp c:/;./templates;./;./SeAP.jar; com.pyramidconsulting.Launcher %*;
rem pause