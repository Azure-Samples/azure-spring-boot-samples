for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_STORAGE_CONTAINER_NAME') do set AZURE_STORAGE_CONTAINER_NAME=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_STORAGE_ACCOUNT_NAME') do set AZURE_STORAGE_ACCOUNT_NAME=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw EVENTHUB_NAMESPACE_01') do set EVENTHUB_NAMESPACE_01=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_NAME_01') do set AZURE_EVENTHUB_NAME_01=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_CONSUMER_GROUP_01') do set AZURE_EVENTHUB_CONSUMER_GROUP_01=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw EVENTHUB_NAMESPACE_02') do set EVENTHUB_NAMESPACE_02=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_NAME_02') do set AZURE_EVENTHUB_NAME_02=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw AZURE_EVENTHUB_CONSUMER_GROUP_02') do set AZURE_EVENTHUB_CONSUMER_GROUP_02=%%i
echo AZURE_STORAGE_CONTAINER_NAME=%AZURE_STORAGE_CONTAINER_NAME%
echo AZURE_STORAGE_ACCOUNT_NAME=%AZURE_STORAGE_ACCOUNT_NAME%
echo EVENTHUB_NAMESPACE_01=%EVENTHUB_NAMESPACE_01%
echo AZURE_EVENTHUB_NAME_01=%AZURE_EVENTHUB_NAME_01%
echo AZURE_EVENTHUB_CONSUMER_GROUP_01=%AZURE_EVENTHUB_CONSUMER_GROUP_01%
echo EVENTHUB_NAMESPACE_02=%EVENTHUB_NAMESPACE_02%
echo AZURE_EVENTHUB_NAME_02=%AZURE_EVENTHUB_NAME_02%
echo AZURE_EVENTHUB_CONSUMER_GROUP_02=%AZURE_EVENTHUB_CONSUMER_GROUP_02%
