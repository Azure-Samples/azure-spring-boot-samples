. terraform\kill-port.ps1 8080
. terraform\kill-port.ps1 8081
. terraform\kill-port.ps1 8082

mvn clean package spring-boot:repackage "-DskipTests -f ../../../pom.xml -pl \
com.azure.spring:spring-cloud-azure-starter-active-directory-resource-server,\
com.azure.spring:spring-cloud-azure-starter-active-directory-resource-server-obo,\
com.azure.spring:spring-cloud-azure-starter-active-directory-webapp"

$env:AZURE_TENANT_ID=$(terraform -chdir=terraform output -raw AZURE_TENANT_ID)
$env:AZURE_CLIENT_ID=$(terraform -chdir=terraform output -raw AZURE_CLIENT_ID)
$env:AZURE_CLIENT_SECRET=$(terraform -chdir=terraform output -raw AZURE_CLIENT_SECRET)
$env:WEB_API_A_CLIENT_ID=$(terraform -chdir=terraform output -raw WEB_API_A_CLIENT_ID)
$env:WEB_API_A_CLIENT_SECRET=$(terraform -chdir=terraform output -raw WEB_API_A_CLIENT_SECRET)
$env:WEB_API_A_APP_ID_URL="api://"+($env:WEB_API_A_CLIENT_ID)
$env:WEB_API_B_CLIENT_ID=$(terraform -chdir=terraform output -raw WEB_API_B_CLIENT_ID)
$env:WEB_API_B_APP_ID_URL="api://"+($env:WEB_API_B_CLIENT_ID)

echo "Running apps"
mkdir -p target
echo "Running aad-resource-server-----------"
Start-Process java -ArgumentList '-jar', ('aad-resource-server/target/'+(Get-ChildItem aad-resource-server/target/*.jar -name)) -RedirectStandardOutput 'target/aad-resource-server.log' -NoNewWindow
sleep 3
echo "Running aad-resource-server-obo-----------"
Start-Process java -ArgumentList '-jar', ('aad-resource-server-obo/target/'+(Get-ChildItem aad-resource-server-obo/target/*.jar -name)) -RedirectStandardOutput 'target/aad-resource-server-obo.log' -NoNewWindow
sleep 3
echo "Running aad-web-application-----------"
Start-Process java -ArgumentList '-jar', ('aad-web-application/target/'+(Get-ChildItem aad-web-application/target/*.jar -name)) -RedirectStandardOutput 'target/aad-web-application.log' -NoNewWindow
sleep 3
echo "All apps started, please check target folder for logs."
echo "You can use the user info below to login."
echo "--------created user--------"
# user
$env:USER_NAME=$(terraform -chdir=terraform  output -raw USER_NAME)
$env:USER_PASSWORD=$(terraform -chdir=terraform  output -raw USER_PASSWORD)

echo USER_NAME=$env:USER_NAME
echo USER_PASSWORD=$env:USER_PASSWORD
echo "Now you should be able to open browser to access http://localhost:8080 with user above."
echo "If you encounter some errors, please refer to target folder for app logs ."
