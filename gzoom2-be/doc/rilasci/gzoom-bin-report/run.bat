echo on
set JAVA_HOME=C:\Program Files\Java\jdk11.0.8_10
"%JAVA_HOME%\bin\java" -Xms512M -Xmx2048M -Dserver.port=7000 -Dloader.path=C:\\data\\gzoom2backend\\config\\birt-lib -Dspring.config.additional-location=C:\\data\\gzoom2backend\\config\\gzoom-report.properties -Dgzoom.conf.dir=C:\\data\\gzoom2backend\\config -jar C:\\data\\gzoom2backend\\report-rest-boot\\target\\report-rest-boot.jar > C:\\data\\gzoom2backend\\logs\\gzoom2-report-be.out
