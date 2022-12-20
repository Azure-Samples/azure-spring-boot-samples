echo "Creating users for PostgreSQL database."

ADMIN_USERNAME=$(terraform -chdir=./terraform output -raw aad_admin_user_name)
AZURE_POSTGRESQL_SERVER_NAME=$(terraform -chdir=./terraform output -raw azure_postgresql_server_name)
CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName --output tsv)
NON_ADMIN_USERNAME=$(echo $CURRENT_USERNAME | sed -e 's/\r//g')
LOGIN_NAME=%%AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME%%

sed "s|$LOGIN_NAME|$NON_ADMIN_USERNAME|g" terraform/postgresql_create_user.sql>tmp_users_processed.sql
cat tmp_users_processed.sql && echo
psql "host=$AZURE_POSTGRESQL_SERVER_NAME.postgres.database.azure.com user=$ADMIN_USERNAME dbname=postgres port=5432 password=$(az account get-access-token --resource-type oss-rdbms --output tsv --query accessToken) sslmode=require" < tmp_users_processed.sql

rm -f tmp_users_processed.sql
