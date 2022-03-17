output "TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
}

output "CLIENT_1_CLIENT_ID" {
  value = azuread_application.client-1.application_id
}


output "CLIENT_1_CLIENT_SECRET" {
  value     = azuread_application_password.client-1.value
  sensitive = true
}


output "USER_NAME" {
  value = azuread_user.user.user_principal_name
}

output "USER_PASSWORD" {
  value     = azuread_user.user.password
  sensitive = true
}
