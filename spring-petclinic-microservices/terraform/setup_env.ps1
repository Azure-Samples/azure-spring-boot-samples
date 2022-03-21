$RESOURCE_GROUP=$(terraform -chdir=terraform output -raw resource_group_name)
$COSMOSDB_URI=$(terraform -chdir=terraform output -raw azure_cosmos_endpoint)
$COSMOS_PRIMARY_KEY=$(terraform -chdir=terraform output -raw azure_cosmos_key)
$LOCATION=$(terraform -chdir=terraform output -raw cosmos_db_location)
$REDIS_NAME=$(terraform -chdir=terraform output -raw redis_name)
$REDIS_HOSTNAME=$(terraform -chdir=terraform output -raw redis_hostname)
$REDIS_PASSWORD=$(terraform -chdir=terraform output -raw redis_password)
$AZURE_KEYVAULT_URI=$(terraform -chdir=terraform output -raw keyvault_url)
$KEYVAULT_NAME=$(terraform -chdir=terraform output -raw azurerm_key_vault_account)
$APP_NAME_FOR_KEYVAULT=$(terraform -chdir=terraform output -raw service_principal_name)
$AZURE_KEYVAULT_CLIENTID=$(terraform -chdir=terraform output -raw azure_key_vault_service_principal_client_id)
$AZURE_KEYVAULT_TENANTID=$(terraform -chdir=terraform output -raw azure_key_vault_tenant_id)
$AZURE_KEYVAULT_CLIENTKEY=$(terraform -chdir=terraform output -raw azure_key_vault_service_principal_client_secret)
# ==== Create Kevvault environment file for Docker containers ====
echo @"
AZURE_KEYVAULT_URI=$AZURE_KEYVAULT_URI
AZURE_KEYVAULT_CLIENTID=$AZURE_KEYVAULT_CLIENTID
AZURE_KEYVAULT_TENANTID=$AZURE_KEYVAULT_TENANTID
AZURE_KEYVAULT_CLIENTKEY=$AZURE_KEYVAULT_CLIENTKEY
"@ > keyvault.env
echo "environment configuration complete"
