output "TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
  description = "The tenant id."
}

output "CLIENT_1_CLIENT_ID" {
  value = azuread_application.client-1.application_id
  description = "The application id of web app."
}

output "CLIENT_1_CLIENT_SECRET" {
  value     = azuread_application_password.client-1.value
  sensitive = true
  description = "The client secret of web app."
}

output "USER_NAME" {
  value = azuread_user.user.user_principal_name
  description = "The user name of the user created by terraform."
}

output "USER_PASSWORD" {
  value     = azuread_user.user.password
  sensitive = true
  description = "The password of the user created by terraform."
}
