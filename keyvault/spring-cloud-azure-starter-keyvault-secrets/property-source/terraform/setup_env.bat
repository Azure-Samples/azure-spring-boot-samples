for /f %%i in ('terraform -chdir^=./terraform output -raw KEYVAULT_URI_01') do set ENDPOINT_1=%%i
for /f %%i in ('terraform -chdir^=./terraform output -raw KEYVAULT_URI_02') do set ENDPOINT_2=%%i
echo ENDPOINT_1=%ENDPOINT_1%
echo ENDPOINT_2=%ENDPOINT_2%
