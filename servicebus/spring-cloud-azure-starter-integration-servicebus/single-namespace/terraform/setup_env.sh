export SERVICEBUS_NAMESPACE=$(terraform -chdir=./terraform output -raw SERVICEBUS_NAMESPACE)

echo SERVICEBUS_NAMESPACE=$SERVICEBUS_NAMESPACE
