# storgae
export AZURE_STORAGE_CONTAINER_NAME=$(terraform -chdir=./terraform output -raw AZURE_STORAGE_CONTAINER_NAME)
export AZURE_STORAGE_ACCOUNT_NAME=$(terraform -chdir=./terraform output -raw AZURE_STORAGE_ACCOUNT_NAME)

# namespace_01
export EVENTHUB_NAMESPACE_01=$(terraform -chdir=./terraform output -raw EVENTHUB_NAMESPACE_01)
export AZURE_EVENTHUB_NAME_01=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUB_NAME_01)
export AZURE_EVENTHUB_CONSUMER_GROUP_01=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUB_CONSUMER_GROUP_01)

# namespace_02
export EVENTHUB_NAMESPACE_02=$(terraform -chdir=./terraform output -raw EVENTHUB_NAMESPACE_02)
export AZURE_EVENTHUB_NAME_02=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUB_NAME_02)
export AZURE_EVENTHUB_CONSUMER_GROUP_02=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUB_CONSUMER_GROUP_02)

