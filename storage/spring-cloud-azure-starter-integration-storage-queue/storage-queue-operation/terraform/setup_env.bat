for /f %%i in ('terraform -chdir^=./terraform output -raw ACCOUNT_NAME') do set ACCOUNT_NAME=%%i
echo ACCOUNT_NAME=%ACCOUNT_NAME%
