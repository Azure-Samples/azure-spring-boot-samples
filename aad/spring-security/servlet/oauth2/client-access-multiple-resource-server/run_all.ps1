. terraform\kill-port.ps1 8080
. terraform\kill-port.ps1 8081
. terraform\kill-port.ps1 8082

mvn clean package spring-boot:repackage "-DskipTests -pl com.azure.spring:servlet-oauth2-client-access-multiple-resource-server-client-application,\
com.azure.spring:servlet-oauth2-client-access-multiple-resource-server-resource-server-1-application,\
com.azure.spring:servlet-oauth2-client-access-multiple-resource-server-resource-server-2-application"

$env:TENANT_ID=$(terraform -chdir=terraform output -raw TENANT_ID)
$env:CLIENT_1_CLIENT_ID=$(terraform -chdir=terraform output -raw CLIENT_1_CLIENT_ID)
$env:RESOURCE_SERVER_1_CLIENT_ID=$(terraform -chdir=terraform output -raw RESOURCE_SERVER_1_CLIENT_ID)
$env:RESOURCE_SERVER_2_CLIENT_ID=$(terraform -chdir=terraform output -raw RESOURCE_SERVER_2_CLIENT_ID)
$env:CLIENT_1_CLIENT_SECRET=$(terraform -chdir=terraform output -raw CLIENT_1_CLIENT_SECRET)
$env:RESOURCE_SERVER_1_CLIENT_SECRET=$(terraform -chdir=terraform output -raw RESOURCE_SERVER_1_CLIENT_SECRET)
$env:USER_NAME=$(terraform -chdir=terraform  output -raw USER_NAME)
$env:USER_PASSWORD=$(terraform -chdir=terraform  output -raw USER_PASSWORD)


echo "--------Running apps--------"
mkdir -p target
sleep 3
echo "--------Running client--------"
Start-Process java -ArgumentList '-jar', ('client/target/'+(Get-ChildItem client/target/*.jar -name)) -RedirectStandardOutput 'target/client.log' -NoNewWindow
sleep 3
echo "--------resource-server-1--------"
Start-Process java -ArgumentList '-jar', ('resource-server-1/target/'+(Get-ChildItem resource-server-1/target/*.jar -name)) -RedirectStandardOutput 'target/resource-server-1.log' -NoNewWindow
sleep 3
echo "--------resource-server-2--------"
Start-Process java -ArgumentList '-jar', ('resource-server-2/target/'+(Get-ChildItem resource-server-2/target/*.jar -name)) -RedirectStandardOutput 'target/resource-server-2.log' -NoNewWindow
sleep 3

echo "All apps started, please check target folder for logs."
echo "You can use the user info below to login."
echo "--------created user--------"
echo USER_NAME=$env:USER_NAME
echo USER_PASSWORD=$env:USER_PASSWORD
echo "Now you should be able to open browser to access http://localhost:8080 with user above."
echo "If you encounter some errors, please refer to target folder for app logs ."
