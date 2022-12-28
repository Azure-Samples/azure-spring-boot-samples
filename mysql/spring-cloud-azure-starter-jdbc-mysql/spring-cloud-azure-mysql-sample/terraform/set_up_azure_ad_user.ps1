echo "Set up environment variables..."

$env:AZ_RESOURCE_GROUP=$(terraform -chdir=terraform output -raw resource_group_name)
$env:AZ_MYSQL_SERVER_NAME=$(terraform -chdir=terraform output -raw azure_mysql_server_name)
$env:AZ_DATABASE_NAME=$(terraform -chdir=terraform output -raw azure_mysql_server_database_name)
$env:AZ_USER_IDENTITY_NAME=$(terraform -chdir=terraform output -raw user_assigned_identity)
$env:CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName --output tsv)
$env:ADMIN_USERNAME=$(echo $env:CURRENT_USERNAME.Trim(" .-`t`n`r"))
$env:CURRENT_USERID=$(az ad signed-in-user show --query id --output tsv)
$env:ADMIN_USER_OBJECTID=$(echo $env:CURRENT_USERID.Trim(" .-`t`n`r"))
$env:NON_ADMIN_USERNAME=$(terraform -chdir=terraform output -raw aad_user_name)
$env:MYSQL_HOST_NAME=-Join($env:AZ_MYSQL_SERVER_NAME,'.mysql.database.azure.com')

echo "Assign the identity to the MySQL server..."

az mysql flexible-server identity assign --resource-group $env:AZ_RESOURCE_GROUP --server-name $env:AZ_MYSQL_SERVER_NAME --identity $env:AZ_USER_IDENTITY_NAME

echo "Set the Azure AD admin user..."

az mysql flexible-server ad-admin create --resource-group $env:AZ_RESOURCE_GROUP --server-name $env:AZ_MYSQL_SERVER_NAME --display-name $env:ADMIN_USERNAME --object-id $env:ADMIN_USER_OBJECTID --identity $env:AZ_USER_IDENTITY_NAME

echo "Add a non admin users for MySQL database..."

$env:LOGIN_NAME='%%AZ_MYSQL_AD_NON_ADMIN_USERNAME%%'
$env:LOGIN_ID='%%AZ_MYSQL_AD_NON_ADMIN_USERID%%'
$env:DB_NAME='%%dbname%%'

(Get-Content terraform/mysql_create_user.sql).replace($env:LOGIN_NAME, $env:NON_ADMIN_USERNAME).replace($env:LOGIN_ID, $env:ADMIN_USER_OBJECTID).replace($env:DB_NAME, $env:AZ_DATABASE_NAME) | Set-Content tmp_users_processed.sql
Get-Content tmp_users_processed.sql

mysql -h $env:MYSQL_HOST_NAME --user $env:ADMIN_USERNAME --enable-cleartext-plugin --password="$(az account get-access-token --resource-type oss-rdbms --output tsv --query accessToken)" -e "source tmp_users_processed.sql"

del "tmp_users_processed.sql"
