export ENDPOINT_1=$(terraform -chdir=./terraform output -raw KEYVAULT_URI_01)
export ENDPOINT_2=$(terraform -chdir=./terraform output -raw KEYVAULT_URI_02)