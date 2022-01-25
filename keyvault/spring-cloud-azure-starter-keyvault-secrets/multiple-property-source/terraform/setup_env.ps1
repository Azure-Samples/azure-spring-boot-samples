$env:ENDPOINT_1=$(terraform -chdir=terraform output -raw KEYVAULT_URI_01)
$env:ENDPOINT_2=$(terraform -chdir=terraform output -raw KEYVAULT_URI_02)