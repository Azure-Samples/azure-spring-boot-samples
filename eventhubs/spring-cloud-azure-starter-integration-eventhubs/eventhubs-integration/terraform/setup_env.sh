export AZURE_EVENTHUBS_NAMESPACE=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_NAMESPACE)
export AZURE_STORAGE_CONTAINER_NAME=$(terraform -chdir=./terraform output -raw AZURE_STORAGE_CONTAINER_NAME)
export AZURE_STORAGE_ACCOUNT_NAME=$(terraform -chdir=./terraform output -raw AZURE_STORAGE_ACCOUNT_NAME)

echo AZURE_EVENTHUBS_NAMESPACE=$AZURE_EVENTHUBS_NAMESPACE
echo AZURE_STORAGE_CONTAINER_NAME=$AZURE_STORAGE_CONTAINER_NAME
echo AZURE_STORAGE_ACCOUNT_NAME=$AZURE_STORAGE_ACCOUNT_NAME
