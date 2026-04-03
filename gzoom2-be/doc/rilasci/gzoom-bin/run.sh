#!/bin/bash

export JAVA_HOME="/usr/lib/jvm/java-11-oracle"
export j=$JAVA_HOME/bin/java
nohup $j -Dgzoom.conf.dir=$HOME/config -Dserver.port=6000 -jar rest-boot.jar &> $HOME/logs/gzoom2-be.out &

cpid=$!
echo "Gzoom 2 Backend started with PID: $cpid"
echo $cpid > $HOME/logs/gzoom2-be.pid

