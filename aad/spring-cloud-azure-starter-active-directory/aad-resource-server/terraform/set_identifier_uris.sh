AZURE_CLIENT_ID=$(terraform output -raw AZURE_CLIENT_ID)

# set identifier_uris
az ad app update --id $AZURE_CLIENT_ID --identifier-uris api://$AZURE_CLIENT_ID
