WEB_API_B_CLIENT_ID=$(terraform output -raw WEB_API_B_CLIENT_ID)

# set identifier_uris
az ad app update --id $WEB_API_B_CLIENT_ID --identifier-uris api://$WEB_API_B_CLIENT_ID
