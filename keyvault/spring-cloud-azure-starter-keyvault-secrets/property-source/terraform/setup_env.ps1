$env:ENDPOINT_1=$(terraform -chdir=terraform output -raw KEYVAULT_URI_01)
$env:ENDPOINT_2=$(terraform -chdir=terraform output -raw KEYVAULT_URI_02)

echo ENDPOINT_1=$env:ENDPOINT_1
echo ENDPOINT_2=$env:ENDPOINT_2
