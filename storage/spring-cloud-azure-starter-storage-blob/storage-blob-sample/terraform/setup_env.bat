for /f %%i in ('terraform -chdir^=./terraform output -raw azure_storage_account') do set AZURE_STORAGE_ACCOUNT=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw storage_container_name') do set STORAGE_CONTAINER_NAME=%%i
echo AZURE_STORAGE_ACCOUNT=%AZURE_STORAGE_ACCOUNT%
echo STORAGE_CONTAINER_NAME=%STORAGE_CONTAINER_NAME%
