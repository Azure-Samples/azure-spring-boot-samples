export AZURE_TENANT_ID=$(terraform -chdir=./terraform output -raw AZURE_TENANT_ID)

# set identifier_uris
echo "----------update identifier-uris start----------"
az ad app update --id $AZURE_CLIENT_ID --identifier-uris api://$AZURE_CLIENT_ID
echo "----------update identifier-uris completed----------"

export AZURE_CLIENT_ID=$(terraform -chdir=./terraform output -raw AZURE_CLIENT_ID)
export USER_NAME=$(terraform -chdir=./terraform output -raw USER_NAME)
export USER_PASSWORD=$(terraform -chdir=./terraform output -raw USER_PASSWORD)

echo AZURE_CLIENT_ID=${AZURE_CLIENT_ID}
echo AZURE_TENANT_ID=${AZURE_TENANT_ID}

echo "--------created user--------"
echo USER_NAME=${USER_NAME}
echo USER_PASSWORD=${USER_PASSWORD}
