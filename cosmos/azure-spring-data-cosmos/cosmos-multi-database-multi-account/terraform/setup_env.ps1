$env:AZURE_COSMOS_URI_1=$(terraform -chdir=terraform output -raw azure_cosmos01_endpoint)
$env:AZURE_COSMOS_KEY_1=$(terraform -chdir=terraform output -raw azure_cosmos01_primary_key)
$env:AZURE_COSMOS_SECONDARY_KEY_1=$(terraform -chdir=terraform output -raw azure_cosmos01_secondary_key)
$env:AZURE_COSMOS_DATABASE_1=$(terraform -chdir=terraform output -raw cosmos_database_name)

$env:AZURE_COSMOS_URI_2=$(terraform -chdir=terraform output -raw azure_cosmos02_endpoint)
$env:AZURE_COSMOS_KEY_2=$(terraform -chdir=terraform output -raw azure_cosmos02_primary_key)
$env:AZURE_COSMOS_SECONDARY_KEY_2=$(terraform -chdir=terraform output -raw azure_cosmos02_secondary_key)
$env:AZURE_COSMOS_DATABASE_2=$(terraform -chdir=terraform output -raw cosmos_database_name)

echo AZURE_COSMOS_URI_1=$env:AZURE_COSMOS_URI_1
echo AZURE_COSMOS_KEY_1=$env:AZURE_COSMOS_KEY_1
echo AZURE_COSMOS_SECONDARY_KEY_1=$env:AZURE_COSMOS_SECONDARY_KEY_1
echo AZURE_COSMOS_DATABASE_1=$env:AZURE_COSMOS_DATABASE_1
echo AZURE_COSMOS_URI_2=$env:AZURE_COSMOS_URI_2
echo AZURE_COSMOS_KEY_2=$env:AZURE_COSMOS_KEY_2
echo AZURE_COSMOS_SECONDARY_KEY_2=$env:AZURE_COSMOS_SECONDARY_KEY_2
echo AZURE_COSMOS_DATABASE_2=$env:AZURE_COSMOS_DATABASE_2
