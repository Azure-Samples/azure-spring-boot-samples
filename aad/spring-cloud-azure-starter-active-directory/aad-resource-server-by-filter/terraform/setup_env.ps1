$env:AZURE_CLIENT_ID=$(terraform -chdir=terraform output -raw AZURE_CLIENT_ID)
$env:AZURE_TENANT_ID=$(terraform -chdir=terraform output -raw AZURE_TENANT_ID)
$env:AZURE_CLIENT_SECRET=$(terraform -chdir=terraform output -raw AZURE_CLIENT_SECRET)
$env:USER_NAME=$(terraform -chdir=terraform output -raw USER_NAME)
$env:USER_PASSWORD=$(terraform -chdir=terraform output -raw USER_PASSWORD)

echo AZURE_CLIENT_ID=$env:AZURE_CLIENT_ID
echo AZURE_TENANT_ID=$env:AZURE_TENANT_ID
echo AZURE_CLIENT_SECRET=$env:AZURE_CLIENT_SECRET

echo "--------created user--------"
echo USER_NAME=$env:USER_NAME
echo USER_PASSWORD=$env:USER_PASSWORD
