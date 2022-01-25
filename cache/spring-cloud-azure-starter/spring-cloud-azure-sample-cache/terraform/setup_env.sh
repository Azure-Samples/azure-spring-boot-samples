export AZURE_SUBSCRIPTION_ID=$(terraform -chdir=./terraform output -raw AZURE_SUBSCRIPTION_ID)
export AZURE_RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw AZURE_RESOURCE_GROUP)
export AZURE_CACHE_REDIS_NAME=$(terraform -chdir=./terraform output -raw AZURE_CACHE_REDIS_NAME)
