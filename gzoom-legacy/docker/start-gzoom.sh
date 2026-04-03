#!/bin/bash
start-apache () {
    cd tools/apache-tomcat-9.0.37/bin
    ./catalina.sh run
}
#copy image resources on shard folders
cp -R /tmp/gzoom/themes/* /home/gzoom/legacy/themes/
cp -R /tmp/gzoom/resources/* /home/gzoom/legacy/hot-deploy/base/webapp/resources/
cp -R /tmp/gzoom/images/* /home/gzoom/legacy/framework/images/webapp/images/
/opt/apache-ant-1.10.8/bin/ant patch
start-apache 
