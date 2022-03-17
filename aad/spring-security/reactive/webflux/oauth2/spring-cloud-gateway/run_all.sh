#!/usr/bin/env bash

kill -9 $(lsof -t -i tcp:8080)
kill -9 $(lsof -t -i tcp:8081)
kill -9 $(lsof -t -i tcp:8082)
kill -9 $(lsof -t -i tcp:8083)

mvn clean package spring-boot:repackage -DskipTests -f ../../../../pom.xml -pl \
com.azure.spring:spring-security-sample-reactive-webflux-oauth2-spring-cloud-gateway-client-application,\
com.azure.spring:spring-security-sample-reactive-webflux-oauth2-spring-cloud-gateway-gateway-application,\
com.azure.spring:spring-security-sample-reactive-webflux-oauth2-spring-cloud-gateway-resource-server-1-application,\
com.azure.spring:spring-security-sample-reactive-webflux-oauth2-spring-cloud-gateway-resource-server-2-application

export terraform_path="./terraform"

export TENANT_ID=$(terraform -chdir=$terraform_path output -raw TENANT_ID)
export CLIENT_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_ID)
export RESOURCE_SERVER_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_1_CLIENT_ID)
export RESOURCE_SERVER_2_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_2_CLIENT_ID)
export CLIENT_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_SECRET)
export RESOURCE_SERVER_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_1_CLIENT_SECRET)
export USER_NAME=$(terraform -chdir=$terraform_path  output -raw USER_NAME)
export USER_PASSWORD=$(terraform -chdir=$terraform_path  output -raw USER_PASSWORD)


echo "--------Running apps--------"
mkdir -p target
sleep 3
echo "--------Running client--------"
nohup java -jar client/target/*.jar  > target/client.log 2>&1 &
sleep 3
echo "--------Running gateway--------"
nohup java -jar gateway/target/*.jar  > target/gateway.log 2>&1 &
sleep 3
echo "--------Running resource-server-1--------"
nohup java -jar resource-server-1/target/*.jar  > target/resource-server-1.log 2>&1 &
sleep 3
echo "--------Running resource-server-2--------"
nohup java -jar resource-server-2/target/*.jar  > target/resource-server-2.log 2>&1 &

echo "All apps started, please check target folder for logs."
echo "You can use the user info below to login."
echo "--------created user--------"
echo USER_NAME=$USER_NAME
echo USER_PASSWORD=$USER_PASSWORD
echo "Now you should be able to open browser to access http://localhost:8080 with user above."
echo "If you encounter some errors, please refer to target folder for app logs ."