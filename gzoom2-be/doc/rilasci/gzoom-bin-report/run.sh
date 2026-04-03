#!/bin/bash

export JAVA_HOME="/usr/lib/jvm/java-11-oracle"
export j=$JAVA_HOME/bin/java
nohup  $j -Dgzoom.conf.dir=$HOME/config -Dserver.port=7000 -Dloader.path=./birt_lib/ -jar report-rest-boot.jar  &> $HOME/logs/gzoom2-report-be.out &

cpid=$!
echo "Gzoom 2 Report started with PID: $cpid"
echo $cpid > $HOME/logs/gzoom2-report-be.pid