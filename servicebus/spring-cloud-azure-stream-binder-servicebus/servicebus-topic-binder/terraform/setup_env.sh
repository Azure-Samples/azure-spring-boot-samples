export AZURE_SERVICEBUS_NAMESPACE=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_NAMESPACE)
export AZURE_SERVICEBUS_TOPIC_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_TOPIC_NAME)
export AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME)
