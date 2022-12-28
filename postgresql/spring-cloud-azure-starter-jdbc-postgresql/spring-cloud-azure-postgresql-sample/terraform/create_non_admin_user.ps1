echo "Creating users for PostgreSQL database..."
$env:ADMIN_USERNAME=$(terraform -chdir=terraform output -raw aad_admin_user_name)
$env:AZURE_POSTGRESQL_SERVER_NAME=$(terraform -chdir=terraform output -raw azure_postgresql_server_name)
$env:CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName --output tsv)
$env:NON_ADMIN_USERNAME=$(echo $env:CURRENT_USERNAME.Trim(" .-`t`n`r"))
$env:POSTGRESQL_HOST_NAME=-Join($env:AZURE_POSTGRESQL_SERVER_NAME,'.postgres.database.azure.com')
$env:LOGIN_NAME='%%AZ_POSTGRESQL_AD_NON_ADMIN_USERNAME%%'

(Get-Content terraform/postgresql_create_user.sql).replace($env:LOGIN_NAME, $env:NON_ADMIN_USERNAME) | Set-Content tmp_users_processed.sql
Get-Content tmp_users_processed.sql

$env:PGPASSWORD=$(az account get-access-token --resource-type oss-rdbms --output tsv --query accessToken)

psql -h $env:POSTGRESQL_HOST_NAME -U $env:ADMIN_USERNAME -d postgres -p 5432 -a -f tmp_users_processed.sql

del "tmp_users_processed.sql"
