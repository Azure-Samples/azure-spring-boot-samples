output "AZURE_TENANT_ID" {
  value       = data.azuread_client_config.current.tenant_id
  description = "The Azure tenant id."
}

output "AZURE_CLIENT_ID" {
  value       = azuread_application.resourceserver.application_id
  description = "The application id."
}

output "USER_NAME" {
  value       = azuread_user.user.user_principal_name
  description = "The user name of the user created by terraform."
}

output "USER_PASSWORD" {
  value       = azuread_user.user.password
  sensitive   = true
  description = "The password of the user created by terraform."
}
