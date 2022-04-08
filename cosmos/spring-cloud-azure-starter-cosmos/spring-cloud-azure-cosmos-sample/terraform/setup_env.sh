export AZURE_COSMOS_ENDPOINT=$(terraform -chdir=./terraform output -raw azure_cosmos_endpoint)

echo AZURE_COSMOS_ENDPOINT=$AZURE_COSMOS_ENDPOINT
