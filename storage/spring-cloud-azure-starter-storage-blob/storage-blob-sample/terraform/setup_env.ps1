$env:AZURE_STORAGE_ACCOUNT=$(terraform -chdir=terraform output -raw azure_storage_account)
$env:STORAGE_CONTAINER_NAME=$(terraform -chdir=terraform output -raw storage_container_name)

echo AZURE_STORAGE_ACCOUNT=$env:AZURE_STORAGE_ACCOUNT
echo STORAGE_CONTAINER_NAME=$env:STORAGE_CONTAINER_NAME
