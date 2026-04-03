#!/bin/bash
echo "start copy files"
#copy image resources on shard folders
cp -R /tmp/gzoom/themes/* /home/gzoom/legacy/themes/
cp -R /tmp/gzoom/resources/* /home/gzoom/legacy/hot-deploy/base/webapp/resources/
cp -R /tmp/gzoom/images/* /home/gzoom/legacy/framework/images/webapp/images/
echo "end copy files"
echo "start patch"
/opt/apache-ant-1.10.8/bin/ant patch
echo "end patch"
if [ "$INIT_DB" == "true" ]
then
    echo "start run-install-custom-seed-initial"
    /opt/apache-ant-1.10.8/bin/ant run-install-custom-seed-initial
    echo "end run-install-custom-seed-initial"
fi
if [ "$UPDATE_DB" == "true" ]
then
    echo "start run-install-custom-seed"
    /opt/apache-ant-1.10.8/bin/ant run-install-custom-seed
    echo "end run-install-custom-seed"
fi
if [ "$UPDATE_PATH" == "true" ]
then
    echo "start update path"
    java -Xms128M -Xmx512M -XX:MaxPermSize=128m -Dofbiz.home=/home/gzoom/legacy -jar ofbiz.jar -init -service=gzUpdateLocalFilesPath
    echo "end update path"
fi
echo "end patching container"