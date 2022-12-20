$env:AZ_DATABASE_SERVER_NAME=$(terraform -chdir=terraform output -raw azure_postgresql_server_name)
$env:AZ_DATABASE_NAME=$(terraform -chdir=terraform output -raw azure_postgresql_server_database_name)
$env:AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME=$(az ad signed-in-user show --query userPrincipalName -o tsv)

echo AZ_DATABASE_SERVER_NAME=$env:AZ_DATABASE_SERVER_NAME
echo AZ_DATABASE_NAME=$env:AZ_DATABASE_NAME
echo AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME=$env:AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME
