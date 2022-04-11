for /f %%i in ('terraform -chdir^=./terraform output -raw STORAGE_QUEUE_ACCOUNT_NAME') do set STORAGE_QUEUE_ACCOUNT_NAME=%%i
echo STORAGE_QUEUE_ACCOUNT_NAME=%STORAGE_QUEUE_ACCOUNT_NAME%
