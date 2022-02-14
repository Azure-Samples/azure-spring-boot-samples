$env:AZURE_COSMOS_URI=$(terraform -chdir=terraform output -raw azure_cosmos_endpoint)
$env:AZURE_COSMOS_KEY=$(terraform -chdir=terraform output -raw azure_cosmos_primary_key)
$env:AZURE_COSMOS_SECONDARY_KEY=$(terraform -chdir=terraform output -raw azure_cosmos_secondary_key)
$env:AZURE_COSMOS_DATABASE=$(terraform -chdir=terraform output -raw cosmos_database_name)

echo AZURE_COSMOS_URI=$env:AZURE_COSMOS_URI
echo AZURE_COSMOS_KEY=$env:AZURE_COSMOS_KEY
echo AZURE_COSMOS_SECONDARY_KEY=$env:AZURE_COSMOS_SECONDARY_KEY
echo AZURE_COSMOS_DATABASE=$env:AZURE_COSMOS_DATABASE
