export AZURE_TENANT_ID=$(terraform -chdir=./terraform output -raw AZURE_TENANT_ID)
export AZURE_CLIENT_ID=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_ID)
export AZURE_CLIENT_SECRET=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_SECRET)
export USER_PASSWORD=$(terraform -chdir=./terraform output -raw USER_PASSWORD)
export USER_PRINCIPAL_NAME=$(terraform -chdir=./terraform output -raw USER_PRINCIPAL_NAME)
export WEB_API_C_APP_ID_URL=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_ID)

echo AZURE_TENANT_ID=$AZURE_TENANT_ID
echo AZURE_CLIENT_ID=$AZURE_CLIENT_ID
echo AZURE_CLIENT_SECRET=$AZURE_CLIENT_SECRET
echo WEB_API_C_APP_ID_URL=$WEB_API_C_APP_ID_URL

echo "--------created user--------"
echo USER_PRINCIPAL_NAME=$USER_PRINCIPAL_NAME
echo USER_PASSWORD=$USER_PASSWORD