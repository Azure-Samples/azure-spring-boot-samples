output "TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
}

output "AZURE_CLIENT_ID" {
  value = azuread_application.webapp.application_id
}

output "AZURE_CLIENT_SECRET" {
  value     = azuread_application_password.webapp.value
  sensitive = true
}

output "USER_NAME" {
  value = azuread_user.user.user_principal_name
}

output "USER_PASSWORD" {
  value     = azuread_user.user.password
  sensitive = true
}
