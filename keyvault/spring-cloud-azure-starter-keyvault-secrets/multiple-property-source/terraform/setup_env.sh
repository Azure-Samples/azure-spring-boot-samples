export ENDPOINT_1=$(terraform -chdir=./terraform output -raw keyvault_uri_01)
export ENDPOINT_2=$(terraform -chdir=./terraform output -raw keyvault_uri_02)