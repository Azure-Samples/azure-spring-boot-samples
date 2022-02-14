export AZURE_COSMOS_URI_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_endpoint)
export AZURE_COSMOS_KEY_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_primary_key)
export AZURE_COSMOS_SECONDARY_KEY_1=$(terraform -chdir=./terraform output -raw azure_cosmos01_secondary_key)
export AZURE_COSMOS_DATABASE_1=$(terraform -chdir=./terraform output -raw cosmos_database_name)

export AZURE_COSMOS_URI_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_endpoint)
export AZURE_COSMOS_KEY_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_primary_key)
export AZURE_COSMOS_SECONDARY_KEY_2=$(terraform -chdir=./terraform output -raw azure_cosmos02_secondary_key)
export AZURE_COSMOS_DATABASE_2=$(terraform -chdir=./terraform output -raw cosmos_database_name)

echo AZURE_COSMOS_URI_1=$AZURE_COSMOS_URI_1
echo AZURE_COSMOS_KEY_1=$AZURE_COSMOS_KEY_1
echo AZURE_COSMOS_SECONDARY_KEY_1=$AZURE_COSMOS_SECONDARY_KEY_1
echo AZURE_COSMOS_DATABASE_1=$AZURE_COSMOS_DATABASE_1
echo AZURE_COSMOS_URI_2=$AZURE_COSMOS_URI_2
echo AZURE_COSMOS_KEY_2=$AZURE_COSMOS_KEY_2
echo AZURE_COSMOS_SECONDARY_KEY_2=$AZURE_COSMOS_SECONDARY_KEY_2
echo AZURE_COSMOS_DATABASE_2=$AZURE_COSMOS_DATABASE_2
