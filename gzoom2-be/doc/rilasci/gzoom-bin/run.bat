echo on
set JAVA_HOME=C:\Program Files\Java\jdk11.0.8_10
"%JAVA_HOME%\bin\java" -Xms512M -Xmx2048M -Dserver.port=8081 -Dspring.config.additional-location=C:\\data\\gzoom2backend\\config\\gzoom.properties -jar C:\\data\\gzoom2backend\\rest-boot\\target\\rest-boot.jar > C:\\data\\gzoom2backend\\logs\\gzoom2-be.out
