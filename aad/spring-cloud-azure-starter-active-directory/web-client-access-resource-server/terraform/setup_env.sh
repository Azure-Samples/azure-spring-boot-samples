terraformpath=./terraform
export AZURE_TENANT_ID=$(terraform -chdir=$terraformpath output -raw AZURE_TENANT_ID)

# WEB_APP
export AZURE_CLIENT_ID=$(terraform -chdir=$terraformpath output -raw AZURE_CLIENT_ID)
export AZURE_CLIENT_SECRET=$(terraform -chdir=$terraformpath output -raw AZURE_CLIENT_SECRET)

# WEB_API_A
export WEB_API_A_CLIENT_ID=$(terraform -chdir=$terraformpath output -raw WEB_API_A_CLIENT_ID)
export WEB_API_A_CLIENT_SECRET=$(terraform -chdir=$terraformpath output -raw WEB_API_A_CLIENT_SECRET)
export WEB_API_A_APP_ID_URL=api://$WEB_API_A_CLIENT_ID

# WEB_API_B
export WEB_API_B_CLIENT_ID=$(terraform -chdir=$terraformpath output -raw WEB_API_B_CLIENT_ID)
export WEB_API_B_APP_ID_URL=api://$WEB_API_B_CLIENT_ID

# WEB_API_C
export WEB_API_C_CLIENT_ID=$(terraform -chdir=$terraformpath output -raw WEB_API_C_CLIENT_ID)
export WEB_API_C_APP_ID_URL=api://$WEB_API_C_CLIENT_ID

# user
export USER_PASSWORD=$(terraform -chdir=$terraformpath output -raw USER_PASSWORD)
export USER_NAME=$(terraform -chdir=$terraformpath output -raw USER_NAME)

# echo================
echo AZURE_TENANT_ID=$AZURE_TENANT_ID

# WEB_APP
echo "================WEB_APP================"
echo AZURE_CLIENT_ID=$AZURE_CLIENT_ID
echo AZURE_CLIENT_SECRET=$AZURE_CLIENT_SECRET

echo "================WEB_API_A================"
# WEB_API_A
echo WEB_API_A_CLIENT_ID=$WEB_API_A_CLIENT_ID
echo WEB_API_A_CLIENT_SECRET=$WEB_API_A_CLIENT_SECRET
echo WEB_API_A_APP_ID_URL=$WEB_API_A_APP_ID_URL

# WEB_API_B
echo "================WEB_API_B================"
echo WEB_API_B_CLIENT_ID=$WEB_API_B_CLIENT_ID
echo WEB_API_B_APP_ID_URL=$WEB_API_B_APP_ID_URL

# WEB_API_C
echo "================WEB_API_C================"
echo WEB_API_C_CLIENT_ID=$WEB_API_C_CLIENT_ID
echo WEB_API_C_APP_ID_URL=$WEB_API_C_APP_ID_URL

# user
echo "===================================="
echo "--------created user--------"
echo USER_NAME=$USER_NAME
echo USER_PASSWORD=$USER_PASSWORD
