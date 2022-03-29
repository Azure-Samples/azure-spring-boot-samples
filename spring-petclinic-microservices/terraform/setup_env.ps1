$env:RESOURCE_GROUP=$(terraform -chdir=terraform output -raw resource_group_name)
$env:COSMOSDB_URI=$(terraform -chdir=terraform output -raw azure_cosmos_endpoint)
$env:COSMOS_PRIMARY_KEY=$(terraform -chdir=terraform output -raw azure_cosmos_key)
$env:LOCATION=$(terraform -chdir=terraform output -raw cosmos_db_location)
$env:REDIS_NAME=$(terraform -chdir=terraform output -raw redis_name)
$env:REDIS_HOSTNAME=$(terraform -chdir=terraform output -raw redis_hostname)
$env:REDIS_PASSWORD=$(terraform -chdir=terraform output -raw redis_password)
$env:AZURE_KEYVAULT_URI=$(terraform -chdir=terraform output -raw keyvault_url)
$env:AZURE_KEYVAULT_ACCOUNT_NAME=$(terraform -chdir=terraform output -raw azurerm_key_vault_account)
$env:APP_NAME_FOR_KEYVAULT=$(terraform -chdir=terraform output -raw service_principal_name)
$env:AZURE_KEYVAULT_CLIENTID=$(terraform -chdir=terraform output -raw azure_key_vault_service_principal_client_id)
$env:AZURE_KEYVAULT_TENANTID=$(terraform -chdir=terraform output -raw azure_key_vault_tenant_id)
$env:AZURE_KEYVAULT_CLIENTKEY=$(terraform -chdir=terraform output -raw azure_key_vault_service_principal_client_secret)

echo RESOURCE_GROUP=$env:RESOURCE_GROUP
echo COSMOSDB_URI=$env:COSMOSDB_URI
echo LOCATION=$env:LOCATION
echo REDIS_NAME=$env:REDIS_NAME
echo REDIS_HOSTNAME=$env:REDIS_HOSTNAME
echo AZURE_KEYVAULT_URI=$env:AZURE_KEYVAULT_URI
echo AZURE_KEYVAULT_ACCOUNT_NAME=$env:AZURE_KEYVAULT_ACCOUNT_NAME
echo APP_NAME_FOR_KEYVAULT=$env:APP_NAME_FOR_KEYVAULT
echo AZURE_KEYVAULT_CLIENTID=$env:AZURE_KEYVAULT_CLIENTID
echo AZURE_KEYVAULT_TENANTID=$env:AZURE_KEYVAULT_TENANTID

# ==== Create Kevvault environment file for Docker containers ====
echo @"
AZURE_KEYVAULT_URI=$env:AZURE_KEYVAULT_URI
AZURE_KEYVAULT_CLIENTID=$env:AZURE_KEYVAULT_CLIENTID
AZURE_KEYVAULT_TENANTID=$env:AZURE_KEYVAULT_TENANTID
AZURE_KEYVAULT_CLIENTKEY=$env:AZURE_KEYVAULT_CLIENTKEY
AZURE_KEYVAULT_ACCOUNT_NAME=$env:AZURE_KEYVAULT_ACCOUNT_NAME
"@ > keyvault.env
echo "environment configuration complete"
