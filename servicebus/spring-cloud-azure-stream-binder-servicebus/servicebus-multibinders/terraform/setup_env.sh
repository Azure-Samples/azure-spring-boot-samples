export AZURE_SERVICEBUS_NAMESPACE_1=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_NAMESPACE_1)
export AZURE_SERVICEBUS_NAMESPACE_2=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_NAMESPACE_2)
export AZURE_SERVICEBUS_TOPIC_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_TOPIC_NAME)
export AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME)
export AZURE_SERVICEBUS_TOPIC_NAME=$(terraform -chdir=./terraform output -raw AZURE_SERVICEBUS_TOPIC_NAME)






