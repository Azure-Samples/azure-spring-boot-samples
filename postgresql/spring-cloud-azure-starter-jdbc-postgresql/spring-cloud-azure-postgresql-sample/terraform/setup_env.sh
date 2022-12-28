export AZ_DATABASE_SERVER_NAME=$(terraform -chdir=./terraform output -raw azure_postgresql_server_name)
export AZ_DATABASE_NAME=$(terraform -chdir=./terraform output -raw azure_postgresql_server_database_name)
export AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME=$(echo $(az ad signed-in-user show --query userPrincipalName --output tsv) | sed -e 's/\r//g')

echo AZ_DATABASE_SERVER_NAME=$AZ_DATABASE_SERVER_NAME
echo AZ_DATABASE_NAME=$AZ_DATABASE_NAME
echo AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME=$AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME
