http://blog.dub.podval.org/2010/01/maven-in-project-repository.html

mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle  -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=ojdbc7.jar -DgroupId=com.oracle  -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar -DgeneratePom=true