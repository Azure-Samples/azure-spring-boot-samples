export AZURE_STORAGE_ACCOUNT=$(terraform -chdir=./terraform output -raw azure_storage_account)
echo AZURE_STORAGE_ACCOUNT=$AZURE_STORAGE_ACCOUNT
