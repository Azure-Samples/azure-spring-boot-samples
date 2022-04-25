#!/usr/bin/env bash

kill -9 $(lsof -t -i tcp:8080)
kill -9 $(lsof -t -i tcp:8081)
kill -9 $(lsof -t -i tcp:8082)

mvn clean package spring-boot:repackage -DskipTests -f ../../../pom.xml -pl \
com.azure.spring:spring-cloud-azure-starter-active-directory-resource-server,\
com.azure.spring:spring-cloud-azure-starter-active-directory-resource-server-obo,\
com.azure.spring:spring-cloud-azure-starter-active-directory-webapp

export AZURE_TENANT_ID=$(terraform -chdir=./terraform output -raw AZURE_TENANT_ID)
export AZURE_CLIENT_ID=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_ID)
export AZURE_CLIENT_SECRET=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_SECRET)
export WEB_API_A_CLIENT_ID=$(terraform -chdir=./terraform output -raw WEB_API_A_CLIENT_ID)
export WEB_API_A_CLIENT_SECRET=$(terraform -chdir=./terraform output -raw WEB_API_A_CLIENT_SECRET)
export WEB_API_A_APP_ID_URL=api://$WEB_API_A_CLIENT_ID
export WEB_API_B_CLIENT_ID=$(terraform -chdir=./terraform output -raw WEB_API_B_CLIENT_ID)
export WEB_API_B_APP_ID_URL=api://$WEB_API_B_CLIENT_ID

echo "Running apps"
mkdir -p target
echo "Running aad-resource-server-----------"
nohup java -jar aad-resource-server/target/*.jar  > target/aad-resource-server.log 2>&1 &
sleep 3
echo "Running aad-resource-server-obo-----------"
nohup java -jar aad-resource-server-obo/target/*.jar  > target/aad-resource-server-obo.log 2>&1 &
sleep 3
echo "Running aad-web-application-----------"
nohup java -jar aad-web-application/target/*.jar  > target/aad-web-application.log 2>&1 &
sleep 3
echo "All apps started, please check target folder for logs."
echo "You can use the user info below to login."
echo "--------created user--------"
# user
export USER_NAME=$(terraform -chdir=./terraform  output -raw USER_NAME)
export USER_PASSWORD=$(terraform -chdir=./terraform  output -raw USER_PASSWORD)

echo USER_NAME=$USER_NAME
echo USER_PASSWORD=$USER_PASSWORD
echo "Now you should be able to open browser to access http://localhost:8080 with user above."
echo "If you encounter some errors, please refer to target folder for app logs ."
