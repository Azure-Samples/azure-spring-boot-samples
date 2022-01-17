export SERVICEBUS_NAMESPACE_CONNECTION_STRING=$(terraform -chdir=./terraform output -raw SERVICEBUS_NAMESPACE_CONNECTION_STRING)
export PRICING_TIER=$(terraform -chdir=./terraform output -raw PRICING_TIER)
