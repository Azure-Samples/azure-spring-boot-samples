$AZURE_KEYVAULT_URI=$(terraform -chdir=terraform output -raw keyvault_url)
$env:RESOURCE_GROUP=$(terraform -chdir=terraform output -raw resource_group_name)
$KEYVAULT_NAME=$(terraform -chdir=terraform output -raw azurerm_key_vault_account)
$COSMOSDB_URI=$(terraform -chdir=terraform output -raw azure_cosmos_endpoint)
$COSMOS_PRIMARY_KEY=$(terraform -chdir=terraform output -raw azure_cosmos_key)
$LOCATION=$(terraform -chdir=terraform output -raw location)
$REDIS_NAME='redis-petclinic-samples' #customize this
$APP_NAME_FOR_KEYVAULT='app-petclinic-samples' #customize this

# ==== Create Redis Cache Account ====
echo "Creating Redis Cache Account, it may take a few minutes"
az redis create --name $REDIS_NAME  --resource-group $env:RESOURCE_GROUP --sku Basic --vm-size c0 --location $LOCATION
$REDIS_HOSTNAME=az redis show --name $REDIS_NAME --resource-group $env:RESOURCE_GROUP | jq -r .hostName
$REDIS_PASSWORD=az redis list-keys --name $REDIS_NAME --resource-group $env:RESOURCE_GROUP | jq -r .primaryKey

# ==== Create service principal ====
echo "Creating service principal, it may take a few minutes"
$SERVICE_PRINCIPAL=az ad sp create-for-rbac --name $APP_NAME_FOR_KEYVAULT --role Contributor

$AZURE_KEYVAULT_CLIENTID=$SERVICE_PRINCIPAL | jq -r .appId
$AZURE_KEYVAULT_TENANTID=$SERVICE_PRINCIPAL | jq -r .tenant
$AZURE_KEYVAULT_CLIENTKEY=$SERVICE_PRINCIPAL | jq -r .password

# set keyvault policy
az keyvault set-policy -n $KEYVAULT_NAME --key-permissions get list --certificate-permissions get list --secret-permissions get list --resource-group $env:RESOURCE_GROUP --spn $AZURE_KEYVAULT_CLIENTID

# ==== add keys to keyvault ====
echo "add keys to keyvault"
az keyvault secret set --vault-name $KEYVAULT_NAME --name cosmosdburi --value $COSMOSDB_URI
az keyvault secret set --vault-name $KEYVAULT_NAME --name cosmosdbkey --value $COSMOS_PRIMARY_KEY
az keyvault secret set --vault-name $KEYVAULT_NAME --name redisuri --value $REDIS_HOSTNAME
az keyvault secret set --vault-name $KEYVAULT_NAME --name redispassword --value $REDIS_PASSWORD

# ==== Create Kevvault environment file for Docker containers ====
echo @"
AZURE_KEYVAULT_URI=$AZURE_KEYVAULT_URI
AZURE_KEYVAULT_CLIENTID=$AZURE_KEYVAULT_CLIENTID
AZURE_KEYVAULT_TENANTID=$AZURE_KEYVAULT_TENANTID
AZURE_KEYVAULT_CLIENTKEY=$AZURE_KEYVAULT_CLIENTKEY
"@ > keyvault.env
echo "environment configuration complete"
