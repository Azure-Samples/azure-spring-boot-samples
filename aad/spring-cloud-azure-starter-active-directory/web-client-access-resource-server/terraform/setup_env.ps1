# set identifier_uris
# webApiA  WEB_API_A_CLIENT_ID
$env:WEB_API_A_CLIENT_ID=$(terraform -chdir=terraform output -raw WEB_API_A_CLIENT_ID)
echo "----------update identifier-uris for WEB_API_A----------"
az ad app update --id $env:WEB_API_A_CLIENT_ID --identifier-uris api://$env:WEB_API_A_CLIENT_ID

# webApiB  WEB_API_B_CLIENT_ID
$env:WEB_API_B_CLIENT_ID=$(terraform -chdir=terraform output -raw WEB_API_B_CLIENT_ID)
echo "----------update identifier-uris for WEB_API_B----------"
az ad app update --id $env:WEB_API_B_CLIENT_ID --identifier-uris api://$env:WEB_API_B_CLIENT_ID

$env:AZURE_TENANT_ID=$(terraform -chdir=terraform output -raw AZURE_TENANT_ID)

# WEB_APP
$env:AZURE_CLIENT_ID=$(terraform -chdir=terraform output -raw AZURE_CLIENT_ID)
$env:AZURE_CLIENT_SECRET=$(terraform -chdir=terraform output -raw AZURE_CLIENT_SECRET)

# WEB_API_A
$env:WEB_API_A_CLIENT_SECRET=$(terraform -chdir=terraform output -raw WEB_API_A_CLIENT_SECRET)
$env:WEB_API_A_APP_ID_URL="api://"+($env:WEB_API_A_CLIENT_ID)

# WEB_API_B
$env:WEB_API_B_APP_ID_URL="api://"+($env:WEB_API_B_CLIENT_ID)

# user
$env:USER_PASSWORD=$(terraform -chdir=terraform output -raw USER_PASSWORD)
$env:USER_NAME=$(terraform -chdir=terraform output -raw USER_NAME)

# echo================
echo AZURE_TENANT_ID=$env:AZURE_TENANT_ID

# WEB_APP
echo "================WEB_APP================"
echo AZURE_CLIENT_ID=$env:AZURE_CLIENT_ID
echo AZURE_CLIENT_SECRET=$env:AZURE_CLIENT_SECRET

echo "================WEB_API_A================"
# WEB_API_A
echo WEB_API_A_CLIENT_ID=$env:WEB_API_A_CLIENT_ID
echo WEB_API_A_CLIENT_SECRET=$env:WEB_API_A_CLIENT_SECRET
echo WEB_API_A_APP_ID_URL=$env:WEB_API_A_APP_ID_URL

# WEB_API_B
echo "================WEB_API_B================"
echo WEB_API_B_CLIENT_ID=$env:WEB_API_B_CLIENT_ID
echo WEB_API_B_APP_ID_URL=$env:WEB_API_B_APP_ID_URL

# user
echo "===================================="
echo "--------created user--------"
echo USER_NAME=$env:USER_NAME
echo USER_PASSWORD=$env:USER_PASSWORD
