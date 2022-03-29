export AZURE_STORAGE_ACCOUNT=$(terraform -chdir=./terraform output -raw azure_storage_account)
export STORAGE_CONTAINER_NAME=$(terraform -chdir=./terraform output -raw storage_container_name)

echo AZURE_STORAGE_ACCOUNT=$AZURE_STORAGE_ACCOUNT
echo STORAGE_CONTAINER_NAME=$STORAGE_CONTAINER_NAME
