AZURE_CLIENT_ID=$(terraform output -raw AZURE_CLIENT_ID)

# set identifier_uris
echo "----------update identifier-uris start----------"
az ad app update --id $AZURE_CLIENT_ID --identifier-uris api://$AZURE_CLIENT_ID
echo "----------update identifier-uris completed----------"
