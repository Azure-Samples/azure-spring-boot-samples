$env:APPCONFIGURATION_ENDPOINT=$(terraform -chdir=terraform output -raw APPCONFIGURATION_ENDPOINT)

echo APPCONFIGURATION_ENDPOINT=$env:APPCONFIGURATION_ENDPOINT
