export AZURE_SERVICEBUS_NAMESPACE=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_NAMESPACE)
export AZURE_SERVICEBUS_QUEUE_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_QUEUE_NAME)
