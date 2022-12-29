echo "Set up environment variables..."

AZ_RESOURCE_GROUP=$(terraform -chdir=./terraform output -raw resource_group_name)
AZ_DATABASE_SERVER_NAME=$(terraform -chdir=./terraform output -raw azure_mysql_server_name)
AZ_DATABASE_NAME=$(terraform -chdir=./terraform output -raw azure_mysql_server_database_name)
AZ_USER_IDENTITY_NAME=$(terraform -chdir=./terraform output -raw user_assigned_identity)
CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName --output tsv)
ADMIN_USERNAME=$(echo $CURRENT_USERNAME | sed -e 's/\r//g')
CURRENT_USERID=$(az ad signed-in-user show --query id --output tsv)
ADMIN_USER_OBJECTID=$(echo $CURRENT_USERID | sed -e 's/\r//g')
NON_ADMIN_USERNAME=$(terraform -chdir=./terraform output -raw aad_user_name)

echo "Assign the identity to the MySQL server..."

az mysql flexible-server identity assign \
    --resource-group $AZ_RESOURCE_GROUP \
    --server-name $AZ_DATABASE_SERVER_NAME \
    --identity $AZ_USER_IDENTITY_NAME

echo "Set the Azure AD admin user..."

az mysql flexible-server ad-admin create \
    --resource-group $AZ_RESOURCE_GROUP \
    --server-name $AZ_DATABASE_SERVER_NAME \
    --display-name $ADMIN_USERNAME \
    --object-id $ADMIN_USER_OBJECTID \
    --identity $AZ_USER_IDENTITY_NAME

echo "Add a non admin users for MySQL database..."

LOGIN_NAME=%%AZ_MYSQL_AD_NON_ADMIN_USERNAME%%
LOGIN_ID=%%AZ_MYSQL_AD_NON_ADMIN_USERID%%
DB_NAME=%%dbname%%

sed "s|$LOGIN_NAME|$NON_ADMIN_USERNAME|g" terraform/mysql_create_user.sql | sed "s|$LOGIN_ID|$ADMIN_USER_OBJECTID|g" | sed "s|$DB_NAME|$AZ_DATABASE_NAME|g">tmp_users_processed.sql
cat tmp_users_processed.sql && echo

mysql -h $AZ_DATABASE_SERVER_NAME.mysql.database.azure.com --user $ADMIN_USERNAME --enable-cleartext-plugin --password="$(az account get-access-token --resource-type oss-rdbms --output tsv --query accessToken)" < tmp_users_processed.sql

rm -f tmp_users_processed.sql
