#!/usr/bin/env bash

export terraform_path="../../../terraform"

export TENANT_ID=$(terraform -chdir=$terraform_path output -raw TENANT_ID)
export CLIENT_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_ID)
export CLIENT_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_SECRET)
export RESOURCE_SERVER_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_1_CLIENT_ID)


echo "Running apps"
mkdir -p target
nohup java -jar client/target/*.jar  > target/client.log 2>&1 &
nohup java -jar resource-server/target/*.jar  > target/resource-server-1.log 2>&1 &
sleep 10
echo "All apps started"

tail -f target/client.log -f target/resource-server.log

# you can kill the process with port
#  kill -9 $(lsof -t -i tcp:<port>)