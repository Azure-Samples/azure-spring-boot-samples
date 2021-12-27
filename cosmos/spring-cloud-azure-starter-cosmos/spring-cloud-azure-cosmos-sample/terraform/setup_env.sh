export AZURE_COSMOS_ENDPOINT=$(terraform -chdir=./terraform output -raw azure_cosmos_endpoint)
azure_cosmos_account=$(terraform -chdir=./terraform output -raw azure_cosmos_account)
# resourceGroupName
resourceGroupName=$(terraform -chdir=./terraform output -raw resource_group_name)
principalId=$(terraform -chdir=./terraform output -raw object_id)
readOnlyRoleDefinitionId=$(terraform -chdir=./terraform output -raw cosmos_application_id)/sqlRoleDefinitions/00000000-0000-0000-0000-000000000001
writeOnlyRoleDefinitionId=$(terraform -chdir=./terraform output -raw cosmos_application_id)/sqlRoleDefinitions/00000000-0000-0000-0000-000000000002
# assign current Cosmos DB Built-in Data Reader
# https://docs.microsoft.com/azure/cosmos-db/how-to-setup-rbac#using-the-azure-cli-1
az cosmosdb sql role assignment create --account-name $azure_cosmos_account --resource-group $resourceGroupName --scope "/" --principal-id $principalId --role-definition-id $readOnlyRoleDefinitionId
# assign current Cosmos DB Built-in Data Contributor
az cosmosdb sql role assignment create --account-name $azure_cosmos_account --resource-group $resourceGroupName --scope "/" --principal-id $principalId --role-definition-id $writeOnlyRoleDefinitionId