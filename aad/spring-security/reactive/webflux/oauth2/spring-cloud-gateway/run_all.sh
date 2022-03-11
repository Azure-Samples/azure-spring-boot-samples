#!/usr/bin/env bash


echo "Running apps"
mkdir -p target
nohup java -jar client/target/*.jar  > target/client.log 2>&1 &
nohup java -jar gateway/target/*.jar  > target/gateway.log 2>&1 &
nohup java -jar resource-server-1/target/*.jar  > target/resource-server-1.log 2>&1 &
nohup java -jar resource-server-2/target/*.jar  > target/resource-server-2.log 2>&1 &
echo "All apps started"
tail -f target/client.log -f target/gateway.log -f target/resource-server-1.log  -f target/resource-server-2.log

# you can kill the process with port
#  kill -9 $(lsof -t -i tcp:<port>)