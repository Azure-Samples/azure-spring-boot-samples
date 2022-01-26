export ACCOUNT_NAME=$(terraform -chdir=./terraform output -raw ACCOUNT_NAME)

echo ACCOUNT_NAME=$ACCOUNT_NAME
