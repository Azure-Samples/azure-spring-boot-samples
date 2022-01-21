export AZURE_KEYVAULT_URI=$(terraform -chdir=./terraform output -raw keyvault_url)
#export AZURE_COSMOS_DATABASENAME=$(terraform -chdir=./terraform output -raw cosmos_database_name)
export RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw resource_group_name)
export KEYVAULT_NAME=$(terraform -chdir=./terraform output -raw azurerm_key_vault_account)
export APP_NAME_FOR_KEYVAULT=hui-appkeyvault

# ==== Create service principal ====
#SERVICE_PRINCIPAL=$(az ad sp create-for-rbac --name ${APP_NAME_FOR_KEYVAULT} --role Contributor)

AZURE_KEYVAULT_CLIENTID=$(echo $SERVICE_PRINCIPAL | jq -r .appId)
AZURE_KEYVAULT_TENANTID=$(echo $SERVICE_PRINCIPAL | jq -r .tenant)
AZURE_KEYVAULT_CLIENTKEY=$(echo $SERVICE_PRINCIPAL | jq -r .password)

# set keyvault policy
az keyvault set-policy -n $KEYVAULT_NAME --key-permissions get list \
 --certificate-permissions get list \
 --secret-permissions get list \
 --resource-group $RESOURCE_GROUP \
 --spn $AZURE_KEYVAULT_CLIENTID

# ==== Create Kevvault environment file for Docker containers ====
cat > keyvault.env << EOF
AZURE_KEYVAULT_URI=$AZURE_KEYVAULT_URI
AZURE_KEYVAULT_CLIENTID=$AZURE_KEYVAULT_CLIENTID
AZURE_KEYVAULT_TENANTID=$AZURE_KEYVAULT_TENANTID
AZURE_KEYVAULT_CLIENTKEY=$AZURE_KEYVAULT_CLIENTKEY
EOF
echo "environment configuration complete"