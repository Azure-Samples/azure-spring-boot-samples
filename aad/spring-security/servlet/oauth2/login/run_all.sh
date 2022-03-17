#!/usr/bin/env bash

kill -9 $(lsof -t -i tcp:8080)

mvn clean package spring-boot:repackage -DskipTests -f ../../../pom.xml -pl \
com.azure.spring:servlet-oauth2-login

export terraform_path="../../../terraform"

export TENANT_ID=$(terraform -chdir=$terraform_path output -raw TENANT_ID)
export CLIENT_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_ID)
export CLIENT_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_SECRET)

export USER_NAME=$(terraform -chdir=$terraform_path  output -raw USER_NAME)
export USER_PASSWORD=$(terraform -chdir=$terraform_path  output -raw USER_PASSWORD)

echo "Running apps"
mkdir -p .target
echo "Running login---------"
sleep 5
nohup java -jar target/*.jar  > target/login.log 2>&1 &


echo "App login started, please check target folder for logs."
echo "You can use the user info below to login."
echo "--------created user--------"
echo USER_NAME=$USER_NAME
echo USER_PASSWORD=$USER_PASSWORD
