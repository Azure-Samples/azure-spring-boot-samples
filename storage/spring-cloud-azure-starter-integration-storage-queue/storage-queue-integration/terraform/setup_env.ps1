$env:ACCOUNT_NAME=$(terraform -chdir=terraform output -raw ACCOUNT_NAME)

echo ACCOUNT_NAME=$env:ACCOUNT_NAME
