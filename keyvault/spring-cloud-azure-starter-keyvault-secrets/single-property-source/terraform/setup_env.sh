export ENDPOINT=$(terraform -chdir=./terraform output -raw KEYVAULT_URI)
echo ENDPOINT=$ENDPOINT
