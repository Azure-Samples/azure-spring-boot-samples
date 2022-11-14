$env:AZURE_STORAGE_ACCOUNT=$(terraform -chdir=terraform output -raw azure_storage_account)
$env:STORAGE_SHARE_NAME=$(terraform -chdir=terraform output -raw storage_share_name)
$env:STORAGE_ACCOUNT_KEY=$(terraform -chdir=terraform output -raw storage_account_key)

echo AZURE_STORAGE_ACCOUNT=$env:AZURE_STORAGE_ACCOUNT
echo STORAGE_SHARE_NAME=$env:STORAGE_SHARE_NAME
echo STORAGE_ACCOUNT_KEY=$env:STORAGE_ACCOUNT_KEY
