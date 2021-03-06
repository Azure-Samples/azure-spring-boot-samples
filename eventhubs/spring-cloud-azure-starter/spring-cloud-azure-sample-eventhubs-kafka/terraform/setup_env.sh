export AZURE_EVENTHUBS_CONNECTION_STRING=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_CONNECTION_STRING)
export EVENTHUBS_KAFKA=$(terraform -chdir=./terraform output -raw EVENTHUBS_KAFKA)
export AZURE_EVENTHUBS_SUBSCRIPTION_ID=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_SUBSCRIPTION_ID)
export AZURE_EVENTHUBS_RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_RESOURCE_GROUP)
export AZURE_EVENTHUBS_NAMESPACE=$(terraform -chdir=./terraform output -raw AZURE_EVENTHUBS_NAMESPACE)


echo AZURE_EVENTHUBS_CONNECTION_STRING=$AZURE_EVENTHUBS_CONNECTION_STRING
echo EVENTHUBS_KAFKA=$EVENTHUBS_KAFKA
echo AZURE_EVENTHUBS_SUBSCRIPTION_ID=AZURE_EVENTHUBS_SUBSCRIPTION_ID
echo AZURE_EVENTHUBS_RESOURCE_GROUP=AZURE_EVENTHUBS_RESOURCE_GROUP
echo AZURE_EVENTHUBS_NAMESPACE=AZURE_EVENTHUBS_NAMESPACE
