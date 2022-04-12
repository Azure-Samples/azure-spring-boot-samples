for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUBS_NAMESPACE') do set AZURE_EVENTHUBS_NAMESPACE=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_STORAGE_CONTAINER_NAME') do set AZURE_STORAGE_CONTAINER_NAME=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_STORAGE_ACCOUNT_NAME') do set AZURE_STORAGE_ACCOUNT_NAME=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_NAME') do set AZURE_EVENTHUB_NAME=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_CONSUMER_GROUP') do set AZURE_EVENTHUB_CONSUMER_GROUP=%%i
echo AZURE_EVENTHUBS_NAMESPACE=%AZURE_EVENTHUBS_NAMESPACE%
echo AZURE_STORAGE_CONTAINER_NAME=%AZURE_STORAGE_CONTAINER_NAME%
echo AZURE_STORAGE_ACCOUNT_NAME=%AZURE_STORAGE_ACCOUNT_NAME%
echo AZURE_EVENTHUB_NAME=%AZURE_EVENTHUB_NAME%
echo AZURE_EVENTHUB_CONSUMER_GROUP=%AZURE_EVENTHUB_CONSUMER_GROUP%
