for /f %%i in ('terraform -chdir^=./terraform output -raw KEYVAULT_URI') do set ENDPOINT=%%i
echo ENDPOINT=%ENDPOINT%
