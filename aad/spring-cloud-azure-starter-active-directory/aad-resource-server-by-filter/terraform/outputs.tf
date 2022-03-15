output "AZURE_CLIENT_ID" {
  value = azuread_application.aadresourceserverbyfilter.application_id
}

output "AZURE_CLIENT_SECRET" {
  value = azuread_application_password.aadresourceserverbyfilter.value
  sensitive = true
}

output "AZURE_TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
}

output "USER_NAME" {
  value = azuread_user.user.user_principal_name
}

output "USER_PASSWORD" {
  value     = azuread_user.user.password
  sensitive = true
}