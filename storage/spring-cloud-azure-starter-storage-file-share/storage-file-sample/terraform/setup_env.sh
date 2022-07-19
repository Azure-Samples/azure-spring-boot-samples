export AZURE_STORAGE_ACCOUNT=$(terraform -chdir=./terraform output -raw azure_storage_account)
export STORAGE_SHARE_NAME=$(terraform -chdir=./terraform output -raw storage_share_name)
export STORAGE_ACCOUNT_KEY=$(terraform -chdir=./terraform output -raw storage_account_key)

echo AZURE_STORAGE_ACCOUNT=$AZURE_STORAGE_ACCOUNT
echo STORAGE_SHARE_NAME=$STORAGE_SHARE_NAME
echo STORAGE_ACCOUNT_KEY=$STORAGE_ACCOUNT_KEY
