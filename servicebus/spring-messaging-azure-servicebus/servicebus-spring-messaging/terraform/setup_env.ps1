$env:AZURE_SERVICEBUS_NAMESPACE=$(terraform -chdir=terraform output -raw AZURE_SERVICEBUS_NAMESPACE)

echo AZURE_SERVICEBUS_NAMESPACE=$env:AZURE_SERVICEBUS_NAMESPACE
