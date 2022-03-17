#!/usr/bin/env bash

mvn clean package spring-boot:repackage -DskipTests

# aad-resource-server 8082
# aad-resource-server 8081
# aad-web-application 8080
kill -9 $(lsof -t -i tcp:8080)
kill -9 $(lsof -t -i tcp:8081)
kill -9 $(lsof -t -i tcp:8082)

