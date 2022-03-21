RESOURCE_SERVER_1_CLIENT_ID=$(terraform output -raw RESOURCE_SERVER_1_CLIENT_ID)
RESOURCE_SERVER_2_CLIENT_ID=$(terraform output -raw RESOURCE_SERVER_2_CLIENT_ID)

# set identifier_uris
echo "----------update identifier-uris for RESOURCE_SERVER_1----------"
az ad app update --id $RESOURCE_SERVER_1_CLIENT_ID --identifier-uris api://$RESOURCE_SERVER_1_CLIENT_ID

echo "----------update identifier-uris for RESOURCE_SERVER_2----------"
az ad app update --id $RESOURCE_SERVER_2_CLIENT_ID --identifier-uris api://$RESOURCE_SERVER_2_CLIENT_ID

echo "----------update identifier-uris completed----------"
