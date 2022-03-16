output "AZURE_TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
}

output "AZURE_CLIENT_ID" {
  value = azuread_application.webapp_resourceserver.application_id
}

output "AZURE_CLIENT_SECRET" {
  value = azuread_application_password.webapp_resourceserver.value
  sensitive = true
}

output "USER_PASSWORD" {
  value     = azuread_user.user.password
  sensitive = true
}

output "USER_PRINCIPAL_NAME" {
  value = azuread_user.user.user_principal_name
}