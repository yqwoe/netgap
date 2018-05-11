#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
#获取配置文件名称
confName=`pwd|cut -d / -f 5`

nohup java -XX:+UseConcMarkSweepGC -Xmx1024m -Xms1024m -Xss256k  -XX:+PrintGCDateStamps -XX:+PrintGCDetails  -Xloggc:./logs/java_gc.log  -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/ -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8  -jar netgap.jar --spring.profiles.active=${confName} --isJar=true > /dev/null 2>&1 &
