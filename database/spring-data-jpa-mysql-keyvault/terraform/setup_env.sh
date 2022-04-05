export KEYVAULTURI=$(terraform -chdir=terraform output -raw keyvault_url)

echo KEYVAULTURI=$KEYVAULTURI