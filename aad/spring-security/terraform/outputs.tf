


#CLIENT-1-CLIENT-ID
#CLIENT-1-CLIENT-SECRET
#RESOURCE-SERVER-1-CLIENT-ID
#RESOURCE-SERVER-1-CLIENT-SECRET
#RESOURCE-SERVER-2-CLIENT-ID
#TENANT-ID
#user.email
#user.password


output "TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
}

output "CLIENT_1_CLIENT_ID" {
  value = azuread_application.client-1.application_id
}

output "RESOURCE_SERVER_1_CLIENT_ID" {
  value = azuread_application.resource-server-1.application_id
}

output "RESOURCE_SERVER_2_CLIENT_ID" {
  value = azuread_application.resource-server-2.application_id
}

output "CLIENT_1_CLIENT_SECRET" {
  value     = azuread_application_password.client-1.value
  sensitive = true
}

output "RESOURCE_SERVER_1_CLIENT_SECRET" {
  value     = azuread_application_password.resource-server-1.value
  sensitive = true
}

output "user_password" {
  value     = azuread_user.newuser.password
  sensitive = true
}

output "user_principal_name" {
  value = azuread_user.newuser.user_principal_name
}