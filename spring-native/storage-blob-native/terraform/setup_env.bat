for /f %%i in ('terraform -chdir^=./terraform output -raw azure_storage_account') do set AZURE_STORAGE_ACCOUNT=%%i
echo AZURE_STORAGE_ACCOUNT=%AZURE_STORAGE_ACCOUNT%
