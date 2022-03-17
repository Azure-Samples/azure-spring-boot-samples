#!/usr/bin/env bash

export terraform_path="../../../../terraform"

export TENANT_ID=$(terraform -chdir=$terraform_path output -raw TENANT_ID)
export CLIENT_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_ID)
export RESOURCE_SERVER_1_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_1_CLIENT_ID)
export RESOURCE_SERVER_2_CLIENT_ID=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_2_CLIENT_ID)
export CLIENT_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw CLIENT_1_CLIENT_SECRET)
export RESOURCE_SERVER_1_CLIENT_SECRET=$(terraform -chdir=$terraform_path output -raw RESOURCE_SERVER_1_CLIENT_SECRET)


echo "Running apps"
mkdir -p .target
nohup java -jar client/target/*.jar  > target/client.log 2>&1 &
nohup java -jar gateway/target/*.jar  > target/gateway.log 2>&1 &
nohup java -jar resource-server-1/target/*.jar  > target/resource-server-1.log 2>&1 &
nohup java -jar resource-server-2/target/*.jar  > target/resource-server-2.log 2>&1 &
echo "All apps started, please check target folder for logs."

tail -f target/client.log -f target/gateway.log -f target/resource-server-1.log  -f target/resource-server-2.log
