# set identifier_uris
# webApiA  WEB_API_A_CLIENT_ID
WEB_API_A_CLIENT_ID=$(terraform output -raw WEB_API_A_CLIENT_ID)
echo "----------update identifier-uris for WEB_API_A----------"
az ad app update --id $WEB_API_A_CLIENT_ID --identifier-uris api://$WEB_API_A_CLIENT_ID

# webApiB  WEB_API_B_CLIENT_ID
WEB_API_B_CLIENT_ID=$(terraform output -raw WEB_API_B_CLIENT_ID)
echo "----------update identifier-uris for WEB_API_B----------"
az ad app update --id $WEB_API_B_CLIENT_ID --identifier-uris api://$WEB_API_B_CLIENT_ID

# webApiC  WEB_API_C_CLIENT_ID
WEB_API_C_CLIENT_ID=$(terraform output -raw WEB_API_C_CLIENT_ID)
echo "----------update identifier-uris for WEB_API_C----------"
az ad app update --id $WEB_API_C_CLIENT_ID --identifier-uris api://$WEB_API_C_CLIENT_ID