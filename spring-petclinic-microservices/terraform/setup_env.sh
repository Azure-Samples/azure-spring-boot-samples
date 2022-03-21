export RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw resource_group_name)
export COSMOSDB_URI=$(terraform -chdir=./terraform output -raw azure_cosmos_endpoint)
export COSMOS_PRIMARY_KEY=$(terraform -chdir=./terraform output -raw azure_cosmos_key)
export LOCATION=$(terraform -chdir=./terraform output -raw cosmos_db_location)
export REDIS_NAME=$(terraform -chdir=./terraform output -raw redis_name)
export REDIS_HOSTNAME=$(terraform -chdir=./terraform output -raw redis_hostname)
export REDIS_PASSWORD=$(terraform -chdir=./terraform output -raw redis_password)
export AZURE_KEYVAULT_URI=$(terraform -chdir=./terraform output -raw keyvault_url)
export KEYVAULT_NAME=$(terraform -chdir=./terraform output -raw azurerm_key_vault_account)
export APP_NAME_FOR_KEYVAULT=$(terraform -chdir=./terraform output -raw service_principal_name)
export AZURE_KEYVAULT_CLIENTID=$(terraform -chdir=./terraform output -raw azure_key_vault_service_principal_client_id)
export AZURE_KEYVAULT_TENANTID=$(terraform -chdir=./terraform output -raw azure_key_vault_tenant_id)
export AZURE_KEYVAULT_CLIENTKEY=$(terraform -chdir=./terraform output -raw azure_key_vault_service_principal_client_secret)
# ==== Create Kevvault environment file for Docker containers ====
cat > keyvault.env << EOF
AZURE_KEYVAULT_URI=$AZURE_KEYVAULT_URI
AZURE_KEYVAULT_CLIENTID=$AZURE_KEYVAULT_CLIENTID
AZURE_KEYVAULT_TENANTID=$AZURE_KEYVAULT_TENANTID
AZURE_KEYVAULT_CLIENTKEY=$AZURE_KEYVAULT_CLIENTKEY
EOF
echo "environment configuration complete"
