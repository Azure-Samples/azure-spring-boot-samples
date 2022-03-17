output "AZURE_TENANT_ID" {
  value       = data.azuread_client_config.current.tenant_id
  description = "The application id."
}

# ------WEB_APP------
output "AZURE_CLIENT_ID" {
  value       = azuread_application.webapp.application_id
  description = "The application id."
}

output "AZURE_CLIENT_SECRET" {
  value       = azuread_application_password.webapp.value
  sensitive   = true
  description = "A secret string the application uses to prove its identity."
}

# ------WebApiA------
output "WEB_API_A_CLIENT_ID" {
  value = azuread_application.webApiA.application_id
}

output "WEB_API_A_CLIENT_SECRET" {
  value     = azuread_application_password.webApiA.value
  sensitive = true
}

# ------WebApiB------
output "WEB_API_B_CLIENT_ID" {
  value = azuread_application.webApiB.application_id
}

# ------WebApiC------
output "WEB_API_C_CLIENT_ID" {
  value = azuread_application.webApiC.application_id
}

# ------User------
output "USER_PASSWORD" {
  value       = azuread_user.user.password
  sensitive   = true
  description = "The password of the user created by terraform."
}

output "USER_NAME" {
  value       = azuread_user.user.user_principal_name
  description = "The user name of the user created by terraform."
}