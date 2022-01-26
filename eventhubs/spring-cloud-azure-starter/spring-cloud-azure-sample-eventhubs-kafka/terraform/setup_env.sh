export AZURE_EVENTHUBS_CONNECTION_STRING=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_CONNECTION_STRING)
export EVENTHUBS_KAFKA=$(terraform -chdir=./terraform output -raw EVENTHUBS_KAFKA)
export AZURE_SUBSCRIPTION_ID=$(terraform -chdir=./terraform output -raw AZURE_SUBSCRIPTION_ID)
export AZURE_EVENTHUBS_RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_RESOURCE_GROUP)
export AZURE_EVENTHUBS_NAMESPACE=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_NAMESPACE)
