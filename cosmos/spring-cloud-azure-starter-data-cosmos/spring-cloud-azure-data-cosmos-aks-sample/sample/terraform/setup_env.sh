export AZURE_COSMOS_ENDPOINT=$(terraform -chdir=./terraform output -raw azure_cosmos_endpoint)
export COSMOS_DATABASE=$(terraform -chdir=./terraform output -raw cosmos_database_name)

echo AZURE_COSMOS_ENDPOINT=$AZURE_COSMOS_ENDPOINT
echo COSMOS_DATABASE=$COSMOS_DATABASE
