$env:AZURE_COSMOS_ENDPOINT=$(terraform -chdir=terraform output -raw azure_cosmos_endpoint)
$env:COSMOS_DATABASE=$(terraform -chdir=terraform output -raw cosmos_database_name)

echo AZURE_COSMOS_ENDPOINT=$env:AZURE_COSMOS_ENDPOINT
echo COSMOS_DATABASE=$env:COSMOS_DATABASE
