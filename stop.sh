#!/bin/bash

#dirPath=$(pwd)
fileName="netgap.jar"
#获取配置文件名称
#confName=`pwd|cut -d / -f 5`
#获取以“server.port”开头的行，加上^
#port=`sed 's/[ \t]*//g' application-ifaas.properties|grep ^server.port|cut -d = -f 2`
#
pids=`ps aux|grep ${fileName}|grep -v grep|awk '{print $2}'`
#PID=`sudo lsof -i:${port}|grep LISTEN|awk '{print $2}'`
#
#if [ -z "${PID}" ]; then
#	echo "${fileName} is not running..."
#else
#	sudo kill ${PID}
#	echo "\033[31m${fileName} stoped, pid-->${PID} \033[0m"
#fi

#uid=$(id lightdm|cut -d'(' -f1|cut -d'=' -f2)
# pids=`sudo lsof +d $(pwd) | grep ${fileName} | awk '{print $2}' | uniq | grep -v "PID"`

if [ "${pids}" ]
then
    for pid in ${pids}
    do
        sudo kill ${pid}
        echo "\033[31m kill pid-->${pid}\033[0m"
    done
else
    echo "\033[31m ${fileName} is not running...\033[0m"
fi