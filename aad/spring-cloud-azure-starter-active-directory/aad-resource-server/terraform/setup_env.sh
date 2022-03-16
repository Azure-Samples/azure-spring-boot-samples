export AZURE_TENANT_ID=$(terraform -chdir=./terraform output -raw AZURE_TENANT_ID)
export WEB_API_B_CLIENT_ID=$(terraform -chdir=./terraform output -raw WEB_API_B_CLIENT_ID)
export WEB_API_B_APP_ID_URI=api://$WEB_API_B_CLIENT_ID

echo AZURE_TENANT_ID=$AZURE_TENANT_ID
echo WEB_API_B_CLIENT_ID=$WEB_API_B_CLIENT_ID
echo WEB_API_B_APP_ID_URI=$WEB_API_B_APP_ID_URI

