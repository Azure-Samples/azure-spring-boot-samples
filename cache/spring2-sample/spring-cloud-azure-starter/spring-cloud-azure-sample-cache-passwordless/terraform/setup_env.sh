export AZURE_CACHE_REDIS_HOST=$(terraform -chdir=./terraform output -raw AZURE_CACHE_REDIS_HOST)

echo AZURE_CACHE_REDIS_HOST=AZURE_CACHE_REDIS_HOST
