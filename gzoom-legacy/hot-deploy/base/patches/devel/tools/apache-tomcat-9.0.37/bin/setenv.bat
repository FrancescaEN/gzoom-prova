rem set CATALINA_OPTS=-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server -Xms2048m -Xmx2048m -XX:NewSize=512m -XX:MaxNewSize=512m -XX:+DisableExplicitGC -Xdebug -Xrunjdwp:transport=dt_socket,address=8091,server=y,suspend=n

set CATALINA_OPTS=-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server @startofbiz.MEMIF@ -XX:+DisableExplicitGC @debugofbiz.REMOTE@

set JAVA_OPTS=%JAVA_OPTS% -Doracle.net.crypto_checksum_client=REQUIRED -Doracle.net.crypto_checksum_types_client=SHA1

echo Find CATALINA_OPTS: "%CATALINA_OPTS%"