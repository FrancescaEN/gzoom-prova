**Run SpringBoot with external JAR in classhpath**

See:
* https://docs.spring.io/spring-boot/docs/current/reference/html/executable-jar.html#executable-jar-launching
* https://stackoverflow.com/questions/26140784/spring-boot-is-it-possible-to-use-external-application-properties-files-in-arbi?answertab=active#tab-top

Edit spring-boot-maven-plugin in pom.xml and zip layout configuration:
```
  <plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
        <configuration>  <!-- added -->
            <layout>ZIP</layout> <!-- to use PropertiesLaunchar -->
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
  </plugins>
``` 

Run application with the following command:  
```
java -Dgzoom.conf.dir=$HOME/config -Dserver.port=7000 -Dloader.path=./birt_lib/ -jar report-rest-boot.jar
```  
the parameters order is most important. `-Dloader.path` must be placed before `-jar`


