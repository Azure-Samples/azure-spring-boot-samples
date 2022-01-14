export AZURE_KEYVAULT_URI=$(terraform -chdir=./terraform output -raw keyvault_url)
export AZURE_COSMOS_URL=$(terraform -chdir=./terraform output -raw azure_cosmos_endpoint)
export AZURE_COSMOS_DATABASENAME=$(terraform -chdir=./terraform output -raw cosmos_database_name)
export AZURE_COSMOS_key=$(terraform -chdir=./terraform output -raw azure_cosmos_key)
export REDIS_HOSTNAME=$(terraform -chdir=./terraform output -raw azurerm_redis_cache_hostname)
export REDIS_PASSWORD=$(terraform -chdir=./terraform output -raw azurerm_redis_cache_key)
KEYVAULT_NAME=$(terraform -chdir=./terraform output -raw azurerm_key_vault_account)

# ==== add keys to keyvault ====
echo "add keys to keyvault"
az keyvault secret set --name cosmosdburi --value $AZURE_COSMOS_URL --vault-name $KEYVAULT_NAME
az keyvault secret set --name redisuri --value $REDIS_HOSTNAME --vault-name $KEYVAULT_NAME
az keyvault secret set --name redispassword --value $REDIS_PASSWORD --vault-name $KEYVAULT_NAME
az keyvault secret set --name cosmosdatabase --value $AZURE_COSMOS_DATABASENAME --vault-name $KEYVAULT_NAME
az keyvault secret set --name cosmoskey --value $AZURE_COSMOS_key --vault-name $KEYVAULT_NAME