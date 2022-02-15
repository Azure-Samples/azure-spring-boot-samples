export AZURE_COSMOS_URI_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_endpoint)
export AZURE_COSMOS_KEY_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_primary_key)
export AZURE_COSMOS_SECONDARY_KEY_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_secondary_key)
export AZURE_COSMOS_DATABASE_1=$(terraform -chdir=./terraform output -raw cosmos_database_name)

export AZURE_COSMOS_URI_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_endpoint)
export AZURE_COSMOS_KEY_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_primary_key)
export AZURE_COSMOS_SECONDARY_KEY_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_secondary_key)
export AZURE_COSMOS_DATABASE_2=$(terraform -chdir=./terraform output -raw cosmos_database_name)

export AZURE_MYSQL_USERNAME=$(terraform -chdir=./terraform output -raw mysql_username)
export AZURE_MYSQL_PASSWORD=$(terraform -chdir=./terraform output -raw mysql_password)
export AZURE_MYSQL_HOST=$(terraform -chdir=./terraform output -raw mysql_url)
AZ_RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw azurerm_resource_group_name)
az mysql server firewall-rule create --resource-group $AZ_RESOURCE_GROUP --server-name $AZURE_MYSQL_HOST --name allowip --start-ip-address "0.0.0.0" --end-ip-address "255.255.255.255"

echo AZURE_COSMOS_URI_1=$AZURE_COSMOS_URI_1
echo AZURE_COSMOS_DATABASE_1=$AZURE_COSMOS_DATABASE_1
echo AZURE_COSMOS_URI_2=$AZURE_COSMOS_URI_2
echo AZURE_COSMOS_DATABASE_2=$AZURE_COSMOS_DATABASE_2
echo AZURE_MYSQL_USERNAME=$AZURE_MYSQL_USERNAME
echo AZURE_MYSQL_HOST=$AZURE_MYSQL_HOST
