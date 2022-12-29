export AZ_DATABASE_SERVER_NAME=$(terraform -chdir=./terraform output -raw azure_mysql_server_name)
export AZ_DATABASE_NAME=$(terraform -chdir=./terraform output -raw azure_mysql_server_database_name)
export AZ_MYSQL_AD_NON_ADMIN_USERNAME=$(terraform -chdir=./terraform output -raw aad_user_name)

echo AZ_DATABASE_SERVER_NAME=$AZ_DATABASE_SERVER_NAME
echo AZ_DATABASE_NAME=$AZ_DATABASE_NAME
echo AZ_MYSQL_AD_NON_ADMIN_USERNAME=$AZ_MYSQL_AD_NON_ADMIN_USERNAME
